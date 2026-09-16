package com.coolerpromc.fishtrap.datagen;

import com.coolerpromc.fishtrap.Constants;
import com.coolerpromc.fishtrap.bait.BaitRegistry;
import com.coolerpromc.fishtrap.bait.BaitType;
import com.coolerpromc.fishtrap.item.ModItems;
import com.coolerpromc.fishtrap.platform.util.RegistryHandler;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.JsonCodecProvider;

import java.util.concurrent.CompletableFuture;

public class ModBaitProvider extends JsonCodecProvider<BaitType> {
    public ModBaitProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, PackOutput.Target.DATA_PACK, BaitRegistry.DIRECTORY, BaitType.CODEC, lookupProvider, Constants.MODID);
    }

    @Override
    protected void gather() {
        bait(ModItems.PLANT_BAIT, 2400, 4800, -1.0F);
        bait(ModItems.WORM_BAIT, 1600, 3200, 0.0F);
        bait(ModItems.FISH_CHUM, 1200, 2000, 1.0F);
        bait(ModItems.GLOW_BAIT, 900, 1400, 2.0F);
        bait(ModItems.PRISMARINE_LURE, 600, 900, 3.0F);
        bait(ModItems.NAUTILUS_LURE, 400, 600, 5.0F);
    }

    private void bait(RegistryHandler.Items<Item> item, int minTicks, int maxTicks, float luck) {
        unconditional(item.id(), new BaitType(item.get(), minTicks, maxTicks, luck));
    }

    @Override
    public String getName() {
        return "Fish Trap bait";
    }
}
