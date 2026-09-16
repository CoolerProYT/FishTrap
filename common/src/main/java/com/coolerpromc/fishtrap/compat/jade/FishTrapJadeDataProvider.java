package com.coolerpromc.fishtrap.compat.jade;

import com.coolerpromc.fishtrap.block.entity.FishTrapBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IServerDataProvider;

public enum FishTrapJadeDataProvider implements IServerDataProvider<BlockAccessor> {
    INSTANCE;

    static final String STATUS = "Status";
    static final String PROGRESS = "Progress";

    @Override
    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        if (accessor.getBlockEntity() instanceof FishTrapBlockEntity trap) {
            data.putInt(STATUS, trap.getStatus());
            data.putFloat(PROGRESS, trap.getCatchProgress());
        }
    }

    @Override
    public Identifier getUid() {
        return FishTrapJadeProvider.UID;
    }
}
