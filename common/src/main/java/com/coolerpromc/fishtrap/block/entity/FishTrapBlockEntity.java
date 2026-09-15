package com.coolerpromc.fishtrap.block.entity;

import com.coolerpromc.fishtrap.bait.BaitRegistry;
import com.coolerpromc.fishtrap.bait.BaitType;
import com.coolerpromc.fishtrap.block.custom.FishTrapBlock;
import com.coolerpromc.fishtrap.loot.FishTrapLootTables;
import com.coolerpromc.fishtrap.screen.FishTrapMenu;
import com.coolerpromc.fishtrap.upgrade.NetRegistry;
import com.coolerpromc.fishtrap.upgrade.NetStyle;
import com.coolerpromc.fishtrap.upgrade.NetType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class FishTrapBlockEntity extends BlockEntity implements MenuProvider {
    public static final int OUTPUT_SLOT_COUNT = 9;

    public static final int DATA_TIMER = 0;
    public static final int DATA_DURATION = 1;
    public static final int DATA_STATUS = 2;
    public static final int DATA_COUNT = 3;

    public static final int STATUS_WORKING = 0;
    public static final int STATUS_NOT_SUBMERGED = 1;
    public static final int STATUS_NO_BAIT = 2;

    /** Timer value while no catch cycle is running. */
    public static final int IDLE = -1;

    private static final Component NAME = Component.translatable("container.fishtrap.fish_trap");

    /** Only registered bait may be inserted, by players or automation. */
    private final SimpleContainer bait = new TrapContainer(1) {
        @Override
        public boolean canPlaceItem(int slot, ItemStack stack) {
            return BaitRegistry.isBait(stack);
        }
    };

    /** Optional net upgrade; one at a time. */
    private final SimpleContainer net = new TrapContainer(1) {
        @Override
        public boolean canPlaceItem(int slot, ItemStack stack) {
            return NetRegistry.isNet(stack);
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }
    };

    /** Filled by the trap itself; automation may extract but never insert. */
    private final SimpleContainer output = new TrapContainer(OUTPUT_SLOT_COUNT) {
        @Override
        public boolean canPlaceItem(int slot, ItemStack stack) {
            return false;
        }
    };

    private int catchTimer = IDLE;
    private int catchDuration;
    private int status = STATUS_NO_BAIT;

    private final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case DATA_TIMER -> FishTrapBlockEntity.this.catchTimer;
                case DATA_DURATION -> FishTrapBlockEntity.this.catchDuration;
                case DATA_STATUS -> FishTrapBlockEntity.this.status;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case DATA_TIMER -> FishTrapBlockEntity.this.catchTimer = value;
                case DATA_DURATION -> FishTrapBlockEntity.this.catchDuration = value;
                case DATA_STATUS -> FishTrapBlockEntity.this.status = value;
                default -> {
                }
            }
        }

        @Override
        public int getCount() {
            return DATA_COUNT;
        }
    };

    public FishTrapBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FISH_TRAP.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, FishTrapBlockEntity trap) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        NetType netType = NetRegistry.get(trap.net.getItem(0));
        // Checked every tick rather than on slot change, so a /reload that changes net styles also updates the look.
        updateNetStyle(level, pos, state, netType);

        // Out of water the trap simply pauses; the running timer is kept.
        if (!isSubmerged(level, pos, state)) {
            trap.status = STATUS_NOT_SUBMERGED;
            return;
        }

        BaitType baitType = BaitRegistry.get(trap.bait.getItem(0));
        if (baitType == null) {
            trap.status = STATUS_NO_BAIT;
            if (trap.catchTimer != IDLE) {
                trap.resetTimer();
                trap.setChanged();
            }
            return;
        }
        trap.status = STATUS_WORKING;

        if (trap.catchTimer == IDLE) {
            trap.startTimer(baitType, netType, serverLevel);
            trap.setChanged();
            return;
        }

        if (--trap.catchTimer > 0) {
            // Persist progress without the comparator/neighbour updates a full setChanged() triggers every tick.
            level.blockEntityChanged(pos);
            return;
        }

        // Bait is always consumed per cycle, whatever the roll yields.
        trap.bait.removeItem(0, 1);
        trap.rollCatch(serverLevel, pos, baitType, netType);

        if (trap.bait.getItem(0).isEmpty()) {
            trap.resetTimer();
        } else {
            trap.startTimer(baitType, netType, serverLevel);
        }
        trap.markUpdated();
    }

    /** Waterlogged (so the trap's own space is water) with water above, i.e. fully under the surface. */
    public static boolean isSubmerged(Level level, BlockPos pos, BlockState state) {
        return state.getValueOrElse(FishTrapBlock.WATERLOGGED, false) && level.getFluidState(pos.above()).is(FluidTags.WATER);
    }

    private static void updateNetStyle(Level level, BlockPos pos, BlockState state, @Nullable NetType netType) {
        NetStyle style = netType == null ? NetStyle.PLASTIC : netType.style();
        if (state.hasProperty(FishTrapBlock.NET) && state.getValue(FishTrapBlock.NET) != style) {
            level.setBlock(pos, state.setValue(FishTrapBlock.NET, style), Block.UPDATE_ALL);
        }
    }

    private void startTimer(BaitType baitType, @Nullable NetType netType, ServerLevel level) {
        int ticks = baitType.rollTicks(level.getRandom());
        this.catchDuration = netType == null ? ticks : netType.applyCatchTime(ticks);
        this.catchTimer = this.catchDuration;
    }

    private void resetTimer() {
        this.catchTimer = IDLE;
        this.catchDuration = 0;
    }

    private void rollCatch(ServerLevel level, BlockPos pos, BaitType baitType, @Nullable NetType netType) {
        LootTable table = FishTrapLootTables.resolve(level, pos);
        // The net is the loot "tool", so tables can gate catches on it with minecraft:match_tool.
        ItemStack tool = netType == null ? new ItemStack(Items.FISHING_ROD) : this.net.getItem(0).copyWithCount(1);
        LootParams params = new LootParams.Builder(level)
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                .withParameter(LootContextParams.TOOL, tool)
                .withLuck(baitType.luck() + (netType == null ? 0.0F : netType.luck()))
                .create(LootContextParamSets.FISHING);

        int rolls = netType != null && level.getRandom().nextFloat() < netType.bonusCatchChance() ? 2 : 1;
        for (int roll = 0; roll < rolls; roll++) {
            for (ItemStack stack : table.getRandomItems(params)) {
                ItemStack remainder = this.insertIntoOutput(stack);
                if (!remainder.isEmpty()) {
                    Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, remainder);
                }
            }
        }

        level.sendParticles(ParticleTypes.BUBBLE_COLUMN_UP, pos.getX() + 0.5, pos.getY() + 0.4, pos.getZ() + 0.5, 12 * rolls, 0.3, 0.15, 0.2, 0.05);
        level.playSound(null, pos, SoundEvents.FISHING_BOBBER_SPLASH, SoundSource.BLOCKS, 0.35F, 0.8F + level.getRandom().nextFloat() * 0.4F);
    }

    /**
     * Merges into matching output stacks first, then empty slots. Returns whatever did not fit.
     * Writes the list directly because the output container refuses {@link Container#canPlaceItem} insertion.
     */
    private ItemStack insertIntoOutput(ItemStack stack) {
        NonNullList<ItemStack> items = this.output.getItems();
        for (int slot = 0; slot < items.size() && !stack.isEmpty(); slot++) {
            ItemStack existing = items.get(slot);
            if (!existing.isEmpty() && ItemStack.isSameItemSameComponents(existing, stack)) {
                int moved = Math.min(stack.getCount(), existing.getMaxStackSize() - existing.getCount());
                if (moved > 0) {
                    existing.grow(moved);
                    stack.shrink(moved);
                }
            }
        }
        for (int slot = 0; slot < items.size() && !stack.isEmpty(); slot++) {
            if (items.get(slot).isEmpty()) {
                items.set(slot, stack.split(stack.getMaxStackSize()));
            }
        }
        return stack;
    }

    /** Side-aware item handler: the bottom exposes the catch, every other side (and no side) the bait slot. */
    public Container getContainer(@Nullable Direction side) {
        return side == Direction.DOWN ? this.output : this.bait;
    }

    /** Every container, used when the capability is queried without a side. */
    public Container[] getContainers() {
        return new Container[]{this.bait, this.net, this.output};
    }

    public ItemStack getBaitStack() {
        return this.bait.getItem(0);
    }

    public ItemStack getNetStack() {
        return this.net.getItem(0);
    }

    /** Non-empty catch stacks; synced to clients for the renderer. */
    public List<ItemStack> getCatches() {
        return this.output.getItems().stream().filter(stack -> !stack.isEmpty()).toList();
    }

    public int getStatus() {
        return this.status;
    }

    /** Progress of the current catch cycle from 0 to 1, or 0 while idle. Server side only. */
    public float getCatchProgress() {
        if (this.catchTimer < 0 || this.catchDuration <= 0) {
            return 0.0F;
        }
        return (this.catchDuration - this.catchTimer) / (float) this.catchDuration;
    }

    /** Saves and sends the new contents to watching clients so the renderer shows the bait and catch. */
    private void markUpdated() {
        this.setChanged();
        if (this.level != null && !this.level.isClientSide()) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveCustomOnly(registries);
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        if (this.level != null) {
            Containers.dropContents(this.level, pos, this.bait);
            Containers.dropContents(this.level, pos, this.net);
            Containers.dropContents(this.level, pos, this.output);
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        loadItems(input.childOrEmpty("Bait"), this.bait.getItems());
        loadItems(input.childOrEmpty("Net"), this.net.getItems());
        loadItems(input.childOrEmpty("Output"), this.output.getItems());
        this.catchTimer = input.getIntOr("CatchTimer", IDLE);
        this.catchDuration = input.getIntOr("CatchDuration", 0);
    }

    private static void loadItems(ValueInput input, NonNullList<ItemStack> items) {
        Collections.fill(items, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(input, items);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output.child("Bait"), this.bait.getItems());
        ContainerHelper.saveAllItems(output.child("Net"), this.net.getItems());
        ContainerHelper.saveAllItems(output.child("Output"), this.output.getItems());
        output.putInt("CatchTimer", this.catchTimer);
        output.putInt("CatchDuration", this.catchDuration);
    }

    @Override
    public Component getDisplayName() {
        return NAME;
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new FishTrapMenu(containerId, inventory, this.bait, this.net, this.output, this.dataAccess, ContainerLevelAccess.create(this.level, this.worldPosition));
    }

    /** Marks the block entity dirty (and syncs it) on any change and closes menus once the player walks away. */
    private class TrapContainer extends SimpleContainer {
        TrapContainer(int size) {
            super(size);
        }

        @Override
        public void setChanged() {
            super.setChanged();
            FishTrapBlockEntity.this.markUpdated();
        }

        @Override
        public boolean stillValid(Player player) {
            return Container.stillValidBlockEntity(FishTrapBlockEntity.this, player);
        }
    }
}
