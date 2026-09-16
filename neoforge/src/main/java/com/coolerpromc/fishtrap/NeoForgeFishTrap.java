package com.coolerpromc.fishtrap;

import com.coolerpromc.fishtrap.platform.NeoForgeRegistryHelper;
import com.coolerpromc.fishtrap.platform.Services;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

@Mod(Constants.MODID)
@EventBusSubscriber(modid = Constants.MODID)
public class NeoForgeFishTrap {
    public NeoForgeFishTrap(IEventBus eventBus) {
        FishTrap.init();
        NeoForgeRegistryHelper.register(eventBus);
    }

    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        FishTrap.initEntityAttribute();
        Services.REGISTRY.applyEntityAttributeRegistrations(event::put);
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        FishTrap.initCapability();
        Services.CAPABILITIES.applyRegistrations(event);
    }

    @SubscribeEvent
    public static void onRegisterBrewingRecipes(RegisterBrewingRecipesEvent event) {
        FishTrap.initBrewingRecipe();
        Services.REGISTRY.applyBrewingRecipeRegistrations(event.getBuilder()::addContainerRecipe);
    }

    @SubscribeEvent
    public static void onDataPackRegistryNewRegistry(DataPackRegistryEvent.NewRegistry event) {
        FishTrap.initDatapackRegistry();
        Services.REGISTRY.applyDatapackRegistryRegistrations(event::dataPackRegistry);
    }

    @SubscribeEvent
    public static void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
        FishTrap.initPayloadType();
        PayloadRegistrar registrar = event.registrar("1");
        Services.REGISTRY.applyClientboundPayloadRegistrations(registrar::playToClient);
    }

    @SubscribeEvent
    public static void onAddServerReloadListeners(AddServerReloadListenersEvent event) {
        FishTrap.initReloadListener();
        Services.REGISTRY.applyServerReloadListenerRegistrations(event::addListener);
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        FishTrap.initCommand();
        Services.REGISTRY.applyCommandRegistrations(event.getDispatcher(), event.getBuildContext());
    }

    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        event.getRelevantPlayers().forEach(FishTrap::onDatapackSync);
    }
}
