package com.coolerpromc.fishtrap.datagen;

import com.coolerpromc.fishtrap.Constants;
import com.coolerpromc.fishtrap.block.ModBlocks;
import com.coolerpromc.fishtrap.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(HolderLookup.Provider lookupProvider, RecipeOutput output) {
        super(lookupProvider, output);
    }

    @Override
    protected void buildRecipes() {
        shaped(RecipeCategory.MISC, ModBlocks.FISH_TRAP)
                .define('S', Items.STICK)
                .define('T', Items.STRING)
                .pattern("STS")
                .pattern("T T")
                .pattern("STS")
                .unlockedBy(getHasName(Items.STRING), has(Items.STRING))
                .save(output);

        shapeless(RecipeCategory.MISC, ModItems.PLANT_BAIT, 4)
                .requires(Items.WHEAT_SEEDS, 2)
                .requires(Items.BONE_MEAL)
                .unlockedBy(getHasName(Items.WHEAT_SEEDS), has(Items.WHEAT_SEEDS))
                .save(output);

        shapeless(RecipeCategory.MISC, ModItems.WORM_BAIT, 4)
                .requires(ModItems.PLANT_BAIT, 2)
                .requires(Items.ROTTEN_FLESH)
                .requires(Items.DIRT)
                .unlockedBy(getHasName(ModItems.PLANT_BAIT), has(ModItems.PLANT_BAIT))
                .save(output);

        shapeless(RecipeCategory.MISC, ModItems.FISH_CHUM, 4)
                .requires(ModItems.WORM_BAIT, 2)
                .requires(tag(ItemTags.FISHES))
                .requires(Items.BONE_MEAL)
                .unlockedBy(getHasName(ModItems.WORM_BAIT), has(ModItems.WORM_BAIT))
                .save(output);

        shapeless(RecipeCategory.MISC, ModItems.GLOW_BAIT, 2)
                .requires(ModItems.FISH_CHUM, 2)
                .requires(Items.GLOW_BERRIES)
                .requires(Items.GLOW_INK_SAC)
                .unlockedBy(getHasName(ModItems.FISH_CHUM), has(ModItems.FISH_CHUM))
                .save(output);

        shaped(RecipeCategory.MISC, ModItems.PRISMARINE_LURE, 2)
                .define('C', Items.PRISMARINE_CRYSTALS)
                .define('S', Items.PRISMARINE_SHARD)
                .define('G', ModItems.GLOW_BAIT)
                .pattern(" C ")
                .pattern("SGS")
                .pattern(" C ")
                .unlockedBy(getHasName(ModItems.GLOW_BAIT), has(ModItems.GLOW_BAIT))
                .save(output);

        shaped(RecipeCategory.MISC, ModItems.NAUTILUS_LURE, 4)
                .define('N', Items.NAUTILUS_SHELL)
                .define('L', ModItems.PRISMARINE_LURE)
                .define('G', Items.GOLD_INGOT)
                .pattern(" N ")
                .pattern("LGL")
                .pattern(" L ")
                .unlockedBy(getHasName(Items.NAUTILUS_SHELL), has(Items.NAUTILUS_SHELL))
                .save(output);

        shaped(RecipeCategory.TOOLS, ModItems.COPPER_NET)
                .define('S', Items.STRING)
                .define('I', Items.COPPER_INGOT)
                .pattern("SIS")
                .pattern("ISI")
                .pattern("SIS")
                .unlockedBy(getHasName(Items.COPPER_INGOT), has(Items.COPPER_INGOT))
                .save(output);
        netUpgrade(ModItems.IRON_NET, ModItems.COPPER_NET, Items.IRON_INGOT);
        netUpgrade(ModItems.GOLD_NET, ModItems.IRON_NET, Items.GOLD_INGOT);
        netUpgrade(ModItems.DIAMOND_NET, ModItems.GOLD_NET, Items.DIAMOND);
        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(ModItems.DIAMOND_NET), Ingredient.of(Items.NETHERITE_INGOT),
                        RecipeCategory.TOOLS, ModItems.NETHERITE_NET.get())
                .unlocks(getHasName(Items.NETHERITE_INGOT), has(Items.NETHERITE_INGOT))
                .save(output, Constants.MODID + ":netherite_net_smithing");

        for (int i = 0; i < ModItems.RAW_CATCHES.size(); i++) {
            cook(ModItems.RAW_CATCHES.get(i), ModItems.COOKED_CATCHES.get(i));
        }
    }

    private void netUpgrade(ItemLike result, ItemLike previous, ItemLike material) {
        shaped(RecipeCategory.TOOLS, result)
                .define('S', Items.STRING)
                .define('I', material)
                .define('N', previous)
                .pattern("SIS")
                .pattern("INI")
                .pattern("SIS")
                .unlockedBy(getHasName(previous), has(previous))
                .save(output);
    }

    private void cook(ItemLike raw, ItemLike cooked) {
        String name = Constants.MODID + ":" + getItemName(cooked);
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(raw), RecipeCategory.FOOD, CookingBookCategory.FOOD, cooked, 0.35F, 200)
                .unlockedBy(getHasName(raw), has(raw))
                .save(output);
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(raw), RecipeCategory.FOOD, cooked, 0.35F, 100)
                .unlockedBy(getHasName(raw), has(raw))
                .save(output, name + "_from_smoking");
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(raw), RecipeCategory.FOOD, cooked, 0.35F, 600)
                .unlockedBy(getHasName(raw), has(raw))
                .save(output, name + "_from_campfire_cooking");
    }

    public static final class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, lookupProvider);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider lookupProvider, RecipeOutput output) {
            return new ModRecipeProvider(lookupProvider, output);
        }

        @Override
        public String getName() {
            return "Fish Trap recipes";
        }
    }
}
