package com.coolerpromc.fishtrap;

import com.coolerpromc.fishtrap.platform.Services;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.server.packs.PackType;

public class FabricFishTrap implements ModInitializer {

    @Override
    public void onInitialize() {
        FishTrap.init();
        FishTrap.initCapability();
        FishTrap.initEntityAttribute();
        FishTrap.initBiomeModifier();
        FishTrap.initDatapackRegistry();
        FishTrap.initPayloadType();
        FishTrap.initReloadListener();
        FishTrap.initCommand();

        Services.REGISTRY.applyEntityAttributeRegistrations(FabricDefaultAttributeRegistry::register);
        Services.REGISTRY.applyBiomeModifierRegistrations((biomeTagKey, step, placedFeatureKey) -> BiomeModifications.addFeature(BiomeSelectors.tag(biomeTagKey), step, placedFeatureKey));

        Services.CAPABILITIES.applyRegistrations(null);

        Services.REGISTRY.applyDatapackRegistryRegistrations(DynamicRegistries::registerSynced);

        Services.REGISTRY.applyClientboundPayloadRegistrations(PayloadTypeRegistry.clientboundPlay()::register);

        Services.REGISTRY.applyServerReloadListenerRegistrations(ResourceLoader.get(PackType.SERVER_DATA)::registerReloadListener);
        CommandRegistrationCallback.EVENT.register((dispatcher, context, selection) -> Services.REGISTRY.applyCommandRegistrations(dispatcher, context));

        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register((player, joined) -> FishTrap.onDatapackSync(player));
    }
}
