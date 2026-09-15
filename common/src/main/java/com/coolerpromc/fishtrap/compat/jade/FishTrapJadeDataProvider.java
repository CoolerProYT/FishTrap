package com.coolerpromc.fishtrap.compat.jade;

import com.coolerpromc.fishtrap.block.entity.FishTrapBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IServerDataProvider;

/**
 * Server half: status and catch progress only exist on the server. Jade (since 1.21.6) requires this to be a
 * separate class from the client-side {@link FishTrapJadeProvider}; both share the same uid.
 */
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
