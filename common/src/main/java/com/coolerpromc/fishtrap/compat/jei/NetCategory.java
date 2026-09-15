package com.coolerpromc.fishtrap.compat.jei;

import com.coolerpromc.fishtrap.Constants;
import com.coolerpromc.fishtrap.block.ModBlocks;
import com.coolerpromc.fishtrap.client.FishTrapTooltips;
import com.coolerpromc.fishtrap.upgrade.NetType;
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

/** Catch speed, luck and bonus catch chance of each net. */
public class NetCategory extends AbstractRecipeCategory<NetRecipe> {
    public static final IRecipeType<NetRecipe> TYPE = IRecipeType.create(Constants.id("fish_trap_net"), NetRecipe.class);
    private static final int TEXT_COLOR = 0xFF404040;

    public NetCategory(IGuiHelper guiHelper) {
        super(TYPE, Component.translatable("jei.fishtrap.net"), guiHelper.createDrawableItemLike(ModBlocks.FISH_TRAP), 150, 46);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, NetRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 15)
                .setStandardSlotBackground()
                .add(new ItemStack(recipe.net().item()));
    }

    @Override
    public void draw(NetRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        Font font = Minecraft.getInstance().font;
        NetType net = recipe.net();
        guiGraphics.text(font, FishTrapTooltips.netSpeed(net), 24, 4, TEXT_COLOR, false);
        guiGraphics.text(font, FishTrapTooltips.luck(net.luck()), 24, 18, TEXT_COLOR, false);
        guiGraphics.text(font, FishTrapTooltips.netBonus(net), 24, 32, TEXT_COLOR, false);
    }
}
