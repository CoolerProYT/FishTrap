package com.coolerpromc.fishtrap.compat.jei;

import com.coolerpromc.fishtrap.Constants;
import com.coolerpromc.fishtrap.bait.BaitType;
import com.coolerpromc.fishtrap.block.ModBlocks;
import com.coolerpromc.fishtrap.client.FishTrapTooltips;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

/** Speed and luck of each bait item. */
public class BaitCategory extends AbstractRecipeCategory<BaitRecipe> {
    public static final IRecipeType<BaitRecipe> TYPE = IRecipeType.create(Constants.id("fish_trap_bait"), BaitRecipe.class);
    private static final int TEXT_COLOR = 0xFF404040;

    public BaitCategory(IGuiHelper guiHelper) {
        super(TYPE, Component.translatable("jei.fishtrap.bait"), guiHelper.createDrawableItemLike(ModBlocks.FISH_TRAP), 140, 36);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, BaitRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 10)
                .setStandardSlotBackground()
                .add(new ItemStack(recipe.bait().item()));
    }

    @Override
    public void draw(BaitRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        Font font = Minecraft.getInstance().font;
        BaitType bait = recipe.bait();
        guiGraphics.text(font, FishTrapTooltips.catchTime(bait), 24, 7, TEXT_COLOR, false);
        guiGraphics.text(font, FishTrapTooltips.luck(bait), 24, 20, TEXT_COLOR, false);
    }
}
