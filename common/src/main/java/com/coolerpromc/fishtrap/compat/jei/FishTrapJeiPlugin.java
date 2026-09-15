package com.coolerpromc.fishtrap.compat.jei;

import com.coolerpromc.fishtrap.Constants;
import com.coolerpromc.fishtrap.bait.BaitRegistry;
import com.coolerpromc.fishtrap.block.ModBlocks;
import com.coolerpromc.fishtrap.catches.CatchTables;
import com.coolerpromc.fishtrap.upgrade.NetRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.List;

@JeiPlugin
public class FishTrapJeiPlugin implements IModPlugin {
    private static @Nullable IJeiRuntime runtime;
    private static boolean listening;
    private static List<CatchTableRecipe> shownCatches = List.of();
    private static List<BaitRecipe> shownBaits = List.of();
    private static List<NetRecipe> shownNets = List.of();

    @Override
    public Identifier getPluginUid() {
        return Constants.id("jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new CatchTableCategory(guiHelper), new BaitCategory(guiHelper), new NetCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        shownCatches = catchRecipes();
        shownBaits = baitRecipes();
        shownNets = netRecipes();
        registration.addRecipes(CatchTableCategory.TYPE, shownCatches);
        registration.addRecipes(BaitCategory.TYPE, shownBaits);
        registration.addRecipes(NetCategory.TYPE, shownNets);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(CatchTableCategory.TYPE, ModBlocks.FISH_TRAP);
        registration.addCraftingStation(BaitCategory.TYPE, ModBlocks.FISH_TRAP);
        registration.addCraftingStation(NetCategory.TYPE, ModBlocks.FISH_TRAP);
    }

    /** The data sync can arrive after JEI has started, and again on {@code /reload}, so recipes are swapped at runtime. */
    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        runtime = jeiRuntime;
        if (!listening) {
            listening = true;
            CatchTables.onSync(FishTrapJeiPlugin::refresh);
        }
    }

    @Override
    public void onRuntimeUnavailable() {
        runtime = null;
    }

    private static void refresh() {
        IJeiRuntime current = runtime;
        if (current == null) {
            return;
        }
        IRecipeManager recipeManager = current.getRecipeManager();
        recipeManager.hideRecipes(CatchTableCategory.TYPE, shownCatches);
        recipeManager.hideRecipes(BaitCategory.TYPE, shownBaits);
        recipeManager.hideRecipes(NetCategory.TYPE, shownNets);
        shownCatches = catchRecipes();
        shownBaits = baitRecipes();
        shownNets = netRecipes();
        recipeManager.addRecipes(CatchTableCategory.TYPE, shownCatches);
        recipeManager.addRecipes(BaitCategory.TYPE, shownBaits);
        recipeManager.addRecipes(NetCategory.TYPE, shownNets);
    }

    private static List<CatchTableRecipe> catchRecipes() {
        return CatchTables.synced().stream().map(CatchTableRecipe::new).toList();
    }

    private static List<BaitRecipe> baitRecipes() {
        return BaitRegistry.sorted().stream().map(BaitRecipe::new).toList();
    }

    private static List<NetRecipe> netRecipes() {
        return NetRegistry.sorted().stream().map(NetRecipe::new).toList();
    }
}
