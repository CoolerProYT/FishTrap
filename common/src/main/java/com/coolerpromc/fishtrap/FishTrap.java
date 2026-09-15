package com.coolerpromc.fishtrap;

import com.coolerpromc.fishtrap.advancement.ModCriteriaTriggers;
import com.coolerpromc.fishtrap.bait.BaitRegistry;
import com.coolerpromc.fishtrap.block.ModBlocks;
import com.coolerpromc.fishtrap.block.entity.FishTrapBlockEntity;
import com.coolerpromc.fishtrap.block.entity.ModBlockEntities;
import com.coolerpromc.fishtrap.item.ModCreativeTabs;
import com.coolerpromc.fishtrap.item.ModItems;
import com.coolerpromc.fishtrap.network.FishTrapDataPayload;
import com.coolerpromc.fishtrap.network.HandledCustomPacketPayload;
import com.coolerpromc.fishtrap.platform.Services;
import com.coolerpromc.fishtrap.screen.ModMenuTypes;
import com.mojang.serialization.Codec;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jspecify.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class FishTrap {
    public static void init() {
        ModBlocks.load();
        ModItems.load();
        ModBlockEntities.load();
        ModMenuTypes.load();
        ModCreativeTabs.load();
        ModCriteriaTriggers.load();
    }

    public static void initCapability(){
        registerCapability(ModBlockEntities.FISH_TRAP, FishTrapBlockEntity::getContainer, FishTrapBlockEntity::getContainers);
    }

    public static void initEntityAttribute(){

    }

    public static void initBiomeModifier(){

    }

    public static void initBrewingRecipe(){

    }

    public static void initDatapackRegistry(){

    }

    public static void initPayloadType(){
        registerClientboundPayload(FishTrapDataPayload.TYPE, FishTrapDataPayload.STREAM_CODEC);
    }

    private static boolean reloadListenersCollected;
    private static boolean commandsCollected;

    public static void initReloadListener(){
        if (reloadListenersCollected) {
            return;
        }
        reloadListenersCollected = true;

        Services.REGISTRY.registerServerReloadListener(BaitRegistry.ID, new BaitRegistry());
        Services.REGISTRY.registerServerReloadListener(com.coolerpromc.fishtrap.upgrade.NetRegistry.ID, new com.coolerpromc.fishtrap.upgrade.NetRegistry());
    }

    public static void initCommand(){
        if (commandsCollected) {
            return;
        }
        commandsCollected = true;
    }

    /** Called by each loader when datapack contents are synced to a player (join and {@code /reload}). */
    public static void onDatapackSync(ServerPlayer player){
        Services.NETWORK.sendToPlayer(player, FishTrapDataPayload.create(player.level().getServer()));
    }

    private static <T extends BlockEntity> void registerCapability(Supplier<BlockEntityType<T>> type, BiFunction<T, @Nullable Direction, Container> provider, Function<T, Container[]> containers){
        Services.CAPABILITIES.registerBlockEntityItemStorage(type, provider, containers);
    }

    private static void registerEntityAttribute(EntityType<? extends LivingEntity> entityType, AttributeSupplier supplier){
        Services.REGISTRY.registerEntityAttribute(entityType, supplier);
    }

    private static void registerBiomeModifier(TagKey<Biome> biomeTagKey, GenerationStep.Decoration step, ResourceKey<PlacedFeature> placedFeatureKey){
        Services.REGISTRY.registerFeatureBiomeModifier(biomeTagKey, step, placedFeatureKey);
    }

    private static void registerBrewingRecipe(Item from, Item ingredient, Item to){
        Services.REGISTRY.registerBrewingRecipe(from, ingredient, to);
    }

    private static <T> void registerDatapackRegistry(ResourceKey<Registry<T>> resourceKey, Codec<T> serverCodec, Codec<T> clientCodec){
        Services.REGISTRY.registerDatapackRegistry(resourceKey, serverCodec, clientCodec);
    }

    private static <T extends HandledCustomPacketPayload> void registerClientboundPayload(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec){
        Services.REGISTRY.registerClientboundPayload(type, streamCodec);
    }
}
