package com.coolerpromc.fishtrap.datagen;

import com.coolerpromc.fishtrap.Constants;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = Constants.MODID)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event) {
        event.createProvider(ModModelProvider::new);
        event.createProvider(ModLanguageProvider::new);
        event.createProvider(ModBlockTagsProvider::new);
        event.createProvider(ModRecipeProvider.Runner::new);
        event.createProvider(ModLootTableProvider::create);
        event.createProvider(ModBaitProvider::new);
        event.createProvider(ModNetProvider::new);
        event.createProvider(ModItemTagsProvider::new);
        event.createProvider(ModBiomeTagsProvider::new);
        event.createProvider(ModAdvancementProvider::create);
    }
}
