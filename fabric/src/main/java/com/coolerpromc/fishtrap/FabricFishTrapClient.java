package com.coolerpromc.fishtrap;

import com.coolerpromc.fishtrap.network.HandledCustomPacketPayload;
import com.coolerpromc.fishtrap.platform.ServicesClient;
import com.coolerpromc.fishtrap.platform.util.FabricPayloadContext;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperties;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperties;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.packs.PackType;

public class FabricFishTrapClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        FishTrapClient.initAll();
        ServicesClient.REGISTRY.applyEntityRendererRegistrations(EntityRenderers::register);
        ServicesClient.REGISTRY.applyEntityModelLayerRegistrations((layer, definition) -> ModelLayerRegistry.registerModelLayer(layer, definition::get));
        ServicesClient.REGISTRY.applyBlockEntityRendererRegistrations(BlockEntityRenderers::register);
        ServicesClient.REGISTRY.applyMenuScreenRegistrations(MenuScreens::register);
        ServicesClient.REGISTRY.applyGuiLayerRegistrations((id, layer) -> HudElementRegistry.addLast(id, layer::render));
        ServicesClient.REGISTRY.applyItemTintSourceRegistrations(ItemTintSources.ID_MAPPER::put);
        ServicesClient.REGISTRY.applyItemConditionRegistrations(ConditionalItemModelProperties.ID_MAPPER::put);
        ServicesClient.REGISTRY.applyItemSelectRegistrations(SelectItemModelProperties.ID_MAPPER::put);
        ServicesClient.REGISTRY.applySpecialModelRendererRegistrations(SpecialModelRenderers.ID_MAPPER::put);
        ServicesClient.REGISTRY.applyClientPayloadReceiverRegistrations(FabricFishTrapClient::registerPayloadReceiver);
        ServicesClient.REGISTRY.applyClientReloadListenerRegistrations(ResourceLoader.get(PackType.CLIENT_RESOURCES)::registerReloadListener);

        ItemTooltipCallback.EVENT.register((stack, context, flag, lines) -> FishTrapClient.appendTooltip(stack, lines));
    }

    private static <T extends HandledCustomPacketPayload> void registerPayloadReceiver(CustomPacketPayload.Type<T> type) {
        ClientPlayNetworking.registerGlobalReceiver(type, (payload, context) -> payload.handle(new FabricPayloadContext(context)));
    }
}
