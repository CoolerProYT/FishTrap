package com.coolerpromc.fishtrap.compat.jade;

import com.coolerpromc.fishtrap.Constants;
import com.coolerpromc.fishtrap.block.entity.FishTrapBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum FishTrapJadeProvider implements IBlockComponentProvider {
    INSTANCE;

    static final Identifier UID = Constants.id("fish_trap");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (accessor.getBlockEntity() instanceof FishTrapBlockEntity trap) {
            ItemStack bait = trap.getBaitStack();
            if (!bait.isEmpty()) {
                tooltip.add(Component.translatable("jade.fishtrap.bait", bait.getHoverName(), bait.getCount()));
            }
            ItemStack net = trap.getNetStack();
            if (!net.isEmpty()) {
                tooltip.add(Component.translatable("jade.fishtrap.net", net.getHoverName()));
            }
        }

        CompoundTag data = accessor.getServerData();
        if (!data.contains(FishTrapJadeDataProvider.STATUS)) {
            return;
        }
        switch (data.getIntOr(FishTrapJadeDataProvider.STATUS, FishTrapBlockEntity.STATUS_NO_BAIT)) {
            case FishTrapBlockEntity.STATUS_NOT_SUBMERGED -> tooltip.add(Component.translatable("gui.fishtrap.fish_trap.not_submerged").withStyle(ChatFormatting.RED));
            case FishTrapBlockEntity.STATUS_NO_BAIT -> tooltip.add(Component.translatable("gui.fishtrap.fish_trap.no_bait").withStyle(ChatFormatting.YELLOW));
            default -> tooltip.add(Component.translatable("jade.fishtrap.progress", Math.round(data.getFloatOr(FishTrapJadeDataProvider.PROGRESS, 0.0F) * 100.0F)));
        }
    }

    @Override
    public Identifier getUid() {
        return UID;
    }
}
