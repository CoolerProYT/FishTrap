package com.coolerpromc.fishtrap.datagen;

import com.coolerpromc.fishtrap.block.ModBlocks;
import com.coolerpromc.fishtrap.item.ModItems;
import com.coolerpromc.fishtrap.loot.FishTrapLootTables;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public final class ModLootTableProvider {
    private ModLootTableProvider() {
    }

    public static LootTableProvider create(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        return new LootTableProvider(output, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(BlockLoot::new, LootContextParamSets.BLOCK),
                new LootTableProvider.SubProviderEntry(FishTrapLoot::new, LootContextParamSets.FISHING)
        ), lookupProvider);
    }

    private static class BlockLoot extends BlockLootSubProvider {
        BlockLoot(HolderLookup.Provider registries) {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
        }

        @Override
        protected void generate() {
            dropSelf(ModBlocks.FISH_TRAP.get());
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return List.of(ModBlocks.FISH_TRAP.get());
        }
    }

    private record FishTrapLoot(HolderLookup.Provider registries) implements LootTableSubProvider {
        @Override
        public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
            output.accept(FishTrapLootTables.DEFAULT, table(
                    entry(Items.COD, 45, -1),
                    entry(Items.SALMON, 20, -1),
                    entry(ModItems.TROUT, 12, 0),
                    entry(Items.PUFFERFISH, 8, 0),
                    entry(ModItems.CRAYFISH, 5, 0),
                    entry(Items.TROPICAL_FISH, 3, 1),
                    entry(ModItems.WORM_BAIT, 4, -1),
                    entry(Items.STICK, 6, -2),
                    entry(Items.STRING, 5, -2),
                    entry(Items.BONE, 5, -2),
                    entry(Items.ROTTEN_FLESH, 5, -2),
                    damaged(Items.LEATHER_BOOTS, 3, -2),
                    entry(Items.NAME_TAG, 1, 2),
                    entry(Items.SADDLE, 1, 2),
                    rainbowFish(1)
            ));

            output.accept(FishTrapLootTables.RIVER, table(
                    entry(Items.SALMON, 40, -1),
                    entry(ModItems.TROUT, 25, 0),
                    entry(Items.COD, 15, -1),
                    entry(ModItems.CRAYFISH, 10, 0),
                    entry(ModItems.CATFISH, 6, 1),
                    entry(ModItems.WORM_BAIT, 5, -1),
                    entry(ModItems.PLANT_BAIT, 3, -2),
                    entry(Items.CLAY_BALL, 5, -1),
                    entry(Items.STICK, 6, -2),
                    entry(Items.LILY_PAD, 6, -2),
                    entry(Items.STRING, 4, -2),
                    entry(Items.LEATHER, 4, -2),
                    entry(Items.BOWL, 3, -2),
                    entry(Items.GOLD_NUGGET, 3, 1),
                    entry(Items.NAME_TAG, 1, 2),
                    rainbowFish(1)
            ));

            output.accept(FishTrapLootTables.SWAMP, table(
                    entry(ModItems.CATFISH, 35, 0),
                    entry(ModItems.CRAYFISH, 20, 0),
                    entry(Items.SALMON, 5, -1),
                    entry(ModItems.WORM_BAIT, 5, -1),
                    entry(ModItems.PLANT_BAIT, 3, -2),
                    entry(Items.LILY_PAD, 10, -2),
                    entry(Items.SLIME_BALL, 4, -1),
                    entry(Items.STICK, 6, -2),
                    entry(Items.BONE, 4, -2),
                    entry(Items.ROTTEN_FLESH, 5, -2),
                    damaged(Items.LEATHER_BOOTS, 2, -2),
                    entry(Items.GOLD_NUGGET, 2, 1),
                    entry(Items.NAME_TAG, 1, 2),
                    rainbowFish(1)
            ));

            output.accept(FishTrapLootTables.OCEAN, table(
                    entry(Items.COD, 35, -1),
                    entry(ModItems.MACKEREL, 30, 0),
                    entry(Items.SALMON, 8, -1),
                    entry(Items.PUFFERFISH, 12, 0),
                    entry(ModItems.CRAB, 10, 1),
                    entry(Items.TROPICAL_FISH, 4, 1),
                    entry(ModItems.FISH_CHUM, 3, 0),
                    entry(Items.KELP, 8, -2),
                    entry(Items.SEAGRASS, 6, -2),
                    entry(Items.BONE, 4, -2),
                    entry(Items.INK_SAC, 4, -1),
                    damaged(Items.LEATHER_BOOTS, 2, -2),
                    entry(Items.PRISMARINE_SHARD, 3, 1),
                    entry(Items.PRISMARINE_CRYSTALS, 2, 1),
                    entry(ModItems.PRISMARINE_LURE, 1, 2),
                    entry(Items.NAUTILUS_SHELL, 1, 2),
                    entry(Items.HEART_OF_THE_SEA, 1, 1),
                    rainbowFish(1)
            ));

            output.accept(FishTrapLootTables.COLD_OCEAN, table(
                    entry(Items.COD, 35, -1),
                    entry(Items.SALMON, 20, -1),
                    entry(ModItems.MACKEREL, 15, 0),
                    entry(ModItems.LOBSTER, 8, 1),
                    entry(ModItems.CRAB, 4, 1),
                    entry(ModItems.FISH_CHUM, 3, 0),
                    entry(Items.KELP, 8, -2),
                    entry(Items.BONE, 5, -2),
                    entry(Items.INK_SAC, 5, -1),
                    entry(ModItems.PRISMARINE_LURE, 1, 2),
                    entry(Items.NAUTILUS_SHELL, 1, 2),
                    entry(Items.HEART_OF_THE_SEA, 1, 1),
                    rainbowFish(1)
            ));

            output.accept(FishTrapLootTables.WARM_OCEAN, table(
                    entry(Items.TROPICAL_FISH, 40, 0),
                    entry(Items.PUFFERFISH, 25, 0),
                    entry(ModItems.CRAB, 12, 0),
                    entry(Items.COD, 10, -1),
                    entry(ModItems.GLOW_BAIT, 2, 1),
                    entry(Items.SEA_PICKLE, 6, -1),
                    entry(Items.SEAGRASS, 6, -2),
                    entry(Items.KELP, 4, -2),
                    entry(Items.PRISMARINE_CRYSTALS, 2, 1),
                    entry(ModItems.PRISMARINE_LURE, 1, 2),
                    entry(Items.NAUTILUS_SHELL, 2, 2),
                    entry(Items.HEART_OF_THE_SEA, 1, 1),
                    rainbowFish(2)
            ));
        }

        private LootPoolSingletonContainer.Builder<?> rainbowFish(int weight) {
            return LootItem.lootTableItem(ModItems.RAINBOW_FISH).setWeight(weight).setQuality(1)
                    .when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(this.registries.lookupOrThrow(Registries.ITEM), ModItems.NETHERITE_NET)));
        }

        @SafeVarargs
        private static LootTable.Builder table(LootPoolSingletonContainer.Builder<?>... entries) {
            LootPool.Builder pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F));
            for (LootPoolSingletonContainer.Builder<?> entry : entries) {
                pool.add(entry);
            }
            return LootTable.lootTable().withPool(pool);
        }

        private static LootPoolSingletonContainer.Builder<?> entry(ItemLike item, int weight, int quality) {
            return LootItem.lootTableItem(item).setWeight(weight).setQuality(quality);
        }

        private static LootPoolSingletonContainer.Builder<?> damaged(ItemLike item, int weight, int quality) {
            return LootItem.lootTableItem(item).setWeight(weight).setQuality(quality)
                    .apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.0F, 0.9F)));
        }
    }
}
