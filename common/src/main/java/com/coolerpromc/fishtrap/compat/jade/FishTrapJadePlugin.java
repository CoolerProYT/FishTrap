package com.coolerpromc.fishtrap.compat.jade;

import com.coolerpromc.fishtrap.block.custom.FishTrapBlock;
import com.coolerpromc.fishtrap.block.entity.FishTrapBlockEntity;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class FishTrapJadePlugin implements IWailaPlugin {
    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(FishTrapJadeDataProvider.INSTANCE, FishTrapBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(FishTrapJadeProvider.INSTANCE, FishTrapBlock.class);
    }
}
