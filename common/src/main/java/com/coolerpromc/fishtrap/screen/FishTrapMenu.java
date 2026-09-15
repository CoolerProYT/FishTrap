package com.coolerpromc.fishtrap.screen;

import com.coolerpromc.fishtrap.advancement.ModCriteriaTriggers;
import com.coolerpromc.fishtrap.block.ModBlocks;
import com.coolerpromc.fishtrap.block.entity.FishTrapBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class FishTrapMenu extends AbstractContainerMenu {
    public static final int IMAGE_HEIGHT = 178;
    // Bait and net slots stack beside the progress arrow; together with the 3x3 output grid they form a group centred in the GUI.
    public static final int BAIT_SLOT_X = 35;
    public static final int BAIT_SLOT_Y = 24;
    public static final int NET_SLOT_X = 35;
    public static final int NET_SLOT_Y = 46;
    public static final int OUTPUT_SLOT_X = 89;
    public static final int OUTPUT_SLOT_Y = 17;
    public static final int INVENTORY_Y = 96;

    private static final int BAIT_SLOT = 0;
    private static final int NET_SLOT = 1;
    private static final int INV_SLOT_START = 2 + FishTrapBlockEntity.OUTPUT_SLOT_COUNT;
    private static final int INV_SLOT_END = INV_SLOT_START + 27;
    private static final int HOTBAR_SLOT_END = INV_SLOT_END + 9;

    private final ContainerData data;
    private final ContainerLevelAccess access;

    public FishTrapMenu(int containerId, Inventory playerInventory, BlockPos pos) {
        this(containerId, playerInventory, new SimpleContainer(1), new SimpleContainer(1), new SimpleContainer(FishTrapBlockEntity.OUTPUT_SLOT_COUNT),
                new SimpleContainerData(FishTrapBlockEntity.DATA_COUNT), ContainerLevelAccess.NULL);
    }

    public FishTrapMenu(int containerId, Inventory playerInventory, Container bait, Container net, Container output, ContainerData data, ContainerLevelAccess access) {
        super(ModMenuTypes.FISH_TRAP.get(), containerId);
        checkContainerSize(bait, 1);
        checkContainerSize(net, 1);
        checkContainerSize(output, FishTrapBlockEntity.OUTPUT_SLOT_COUNT);
        checkContainerDataCount(data, FishTrapBlockEntity.DATA_COUNT);
        this.data = data;
        this.access = access;

        // The block entity's containers check the bait/net registries; client-side containers accept anything and get corrected by the server.
        this.addSlot(new Slot(bait, 0, BAIT_SLOT_X, BAIT_SLOT_Y) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return bait.canPlaceItem(this.getContainerSlot(), stack);
            }
        });

        this.addSlot(new Slot(net, 0, NET_SLOT_X, NET_SLOT_Y) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return net.canPlaceItem(this.getContainerSlot(), stack);
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                this.addSlot(new Slot(output, col + row * 3, OUTPUT_SLOT_X + col * 18, OUTPUT_SLOT_Y + row * 18) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return false;
                    }

                    @Override
                    public void onTake(Player player, ItemStack stack) {
                        super.onTake(player, stack);
                        if (player instanceof ServerPlayer serverPlayer && !stack.isEmpty()) {
                            ModCriteriaTriggers.FISH_TRAP_CATCH.get().trigger(serverPlayer, stack);
                        }
                    }
                });
            }
        }

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        this.addDataSlots(data);
    }

    /** Progress of the current catch cycle from 0 to 1, or 0 while idle. */
    public float getCatchProgress() {
        int timer = this.data.get(FishTrapBlockEntity.DATA_TIMER);
        int duration = this.data.get(FishTrapBlockEntity.DATA_DURATION);
        if (timer < 0 || duration <= 0) {
            return 0.0F;
        }
        return (duration - timer) / (float) duration;
    }

    public int getStatus() {
        return this.data.get(FishTrapBlockEntity.DATA_STATUS);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack original = slot.getItem();
            result = original.copy();

            if (index < INV_SLOT_START) {
                if (!this.moveItemStackTo(original, INV_SLOT_START, HOTBAR_SLOT_END, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.slots.get(BAIT_SLOT).mayPlace(original) && this.moveItemStackTo(original, BAIT_SLOT, BAIT_SLOT + 1, false)) {
                // Moved (at least partially) into the bait slot.
            } else if (this.slots.get(NET_SLOT).mayPlace(original) && this.moveItemStackTo(original, NET_SLOT, NET_SLOT + 1, false)) {
                // Moved into the net slot.
            } else if (index < INV_SLOT_END) {
                if (!this.moveItemStackTo(original, INV_SLOT_END, HOTBAR_SLOT_END, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(original, INV_SLOT_START, INV_SLOT_END, false)) {
                return ItemStack.EMPTY;
            }

            if (original.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (original.getCount() == result.getCount()) {
                return ItemStack.EMPTY;
            }

            // The moved copy, since the original may now be empty (catch advancements check the item).
            slot.onTake(player, result);
        }
        return result;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, ModBlocks.FISH_TRAP.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, INVENTORY_Y + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, INVENTORY_Y + 58));
        }
    }
}
