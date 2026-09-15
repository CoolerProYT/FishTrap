package com.coolerpromc.fishtrap;

import com.coolerpromc.fishtrap.network.HandledCustomPacketPayload;
import com.coolerpromc.fishtrap.platform.ServicesClient;
import com.coolerpromc.fishtrap.platform.util.NeoForgePayloadContext;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@Mod(dist = Dist.CLIENT, value = Constants.MODID)
@EventBusSubscriber(modid = Constants.MODID, value = Dist.CLIENT)
public class NeoForgeFishTrapClient {
    public NeoForgeFishTrapClient(IEventBus eventBus){
        FishTrapClient.init();
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        FishTrapClient.initRenderer();
        ServicesClient.REGISTRY.applyEntityRendererRegistrations(event::registerEntityRenderer);
        ServicesClient.REGISTRY.applyBlockEntityRendererRegistrations(event::registerBlockEntityRenderer);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        FishTrapClient.initModelLayer();
        ServicesClient.REGISTRY.applyEntityModelLayerRegistrations(event::registerLayerDefinition);
    }

    @SubscribeEvent
    public static void onRegisterGuiLayer(RegisterGuiLayersEvent event) {
        FishTrapClient.initGuiLayer();
        ServicesClient.REGISTRY.applyGuiLayerRegistrations((id, layer) -> event.registerAboveAll(id, layer::render));
    }

    @SubscribeEvent
    public static void onRegisterColorHandlersItemTintSources(RegisterColorHandlersEvent.ItemTintSources event) {
        FishTrapClient.initItemTintSource();
        ServicesClient.REGISTRY.applyItemTintSourceRegistrations(event::register);
    }

    @SubscribeEvent
    public static void onRegisterConditionalItemModelProperty(RegisterConditionalItemModelPropertyEvent event) {
        FishTrapClient.initItemCondition();
        ServicesClient.REGISTRY.applyItemConditionRegistrations(event::register);
    }

    @SubscribeEvent
    public static void onRegisterSelectItemModelProperty(RegisterSelectItemModelPropertyEvent event) {
        FishTrapClient.initItemSelect();
        ServicesClient.REGISTRY.applyItemSelectRegistrations(event::register);
    }

    @SubscribeEvent
    public static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
        FishTrapClient.initMenuScreen();
        ServicesClient.REGISTRY.applyMenuScreenRegistrations(event::register);
    }

    @SubscribeEvent
    public static void onRegisterSpecialModelRenderer(RegisterSpecialModelRendererEvent event) {
        FishTrapClient.initSpecialModelRenderer();
        ServicesClient.REGISTRY.applySpecialModelRendererRegistrations(event::register);
    }

    @SubscribeEvent
    public static void onAddClientReloadListeners(AddClientReloadListenersEvent event) {
        FishTrapClient.initClientReloadListener();
        ServicesClient.REGISTRY.applyClientReloadListenerRegistrations(event::addListener);
    }

    @SubscribeEvent
    public static void onRegisterClientPayloadHandlers(RegisterClientPayloadHandlersEvent event) {
        FishTrapClient.initClientPayloadHandler();
        ServicesClient.REGISTRY.applyClientPayloadReceiverRegistrations(new NeoForgePayloadRegistrar(event)::register);
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        FishTrapClient.appendTooltip(event.getItemStack(), event.getToolTip());
    }

    private record NeoForgePayloadRegistrar(RegisterClientPayloadHandlersEvent event) {
        private <T extends HandledCustomPacketPayload> void register(CustomPacketPayload.Type<T> type) {
            event.register(type, (payload, context) -> payload.handle(new NeoForgePayloadContext(context)));
        }
    }
}
