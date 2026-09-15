package com.coolerpromc.fishtrap.compat.jei;

import com.coolerpromc.fishtrap.Constants;
import com.coolerpromc.fishtrap.bait.BaitRegistry;
import com.coolerpromc.fishtrap.bait.BaitType;
import com.coolerpromc.fishtrap.block.ModBlocks;
import com.coolerpromc.fishtrap.catches.CatchEntry;
import com.coolerpromc.fishtrap.catches.CatchTable;
import com.coolerpromc.fishtrap.client.FishTrapTooltips;
import com.coolerpromc.fishtrap.upgrade.NetRegistry;
import com.coolerpromc.fishtrap.upgrade.NetType;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Consumer;

/** One page per catch table: every possible catch, with its chance for each bait in the tooltip. */
public class CatchTableCategory extends AbstractRecipeCategory<CatchTableRecipe> {
    public static final IRecipeType<CatchTableRecipe> TYPE = IRecipeType.create(Constants.id("fish_trap_catch"), CatchTableRecipe.class);
    private static final int COLUMNS = 9;
    private static final int ROWS = 3;
    private static final int GRID_Y = 12;

    public CatchTableCategory(IGuiHelper guiHelper) {
        super(TYPE, Component.translatable("jei.fishtrap.catches"), guiHelper.createDrawableItemLike(ModBlocks.FISH_TRAP), COLUMNS * 18, GRID_Y + ROWS * 18);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CatchTableRecipe recipe, IFocusGroup focuses) {
        CatchTable table = recipe.table();
        List<CatchEntry> entries = table.entries();
        for (int i = 0; i < Math.min(entries.size(), COLUMNS * ROWS); i++) {
            CatchEntry entry = entries.get(i);
            builder.addSlot(RecipeIngredientRole.OUTPUT, (i % COLUMNS) * 18 + 1, GRID_Y + (i / COLUMNS) * 18 + 1)
                    .setStandardSlotBackground()
                    .add(new ItemStack(entry.item()))
                    .addRichTooltipCallback((slotView, tooltip) -> appendChances(tooltip::add, table, entry));
        }
    }

    @Override
    public void draw(CatchTableRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        guiGraphics.text(Minecraft.getInstance().font, recipe.table().displayName(), 0, 1, 0xFF404040, false);
    }

    /**
     * Chances per bait. Entries that need a net are shown with that net fitted (its luck included);
     * everything else is shown without a net.
     */
    private static void appendChances(Consumer<Component> tooltip, CatchTable table, CatchEntry entry) {
        tooltip.accept(Component.translatable("jei.fishtrap.weight", entry.weight(), entry.quality()).withStyle(ChatFormatting.GRAY));

        ItemStack tool = entry.requiredTools().isEmpty() ? ItemStack.EMPTY : new ItemStack(entry.requiredTools().getFirst());
        NetType net = NetRegistry.get(tool);
        float netLuck = net == null ? 0.0F : net.luck();
        if (!tool.isEmpty()) {
            tooltip.accept(Component.translatable("jei.fishtrap.requires", tool.getHoverName()).withStyle(ChatFormatting.GOLD));
        }

        List<BaitType> baits = BaitRegistry.sorted();
        if (baits.isEmpty()) {
            tooltip.accept(Component.translatable("jei.fishtrap.chance", FishTrapTooltips.percent(table.chance(entry, netLuck, tool))).withStyle(ChatFormatting.GRAY));
            return;
        }
        tooltip.accept(Component.translatable(tool.isEmpty() ? "jei.fishtrap.chance_by_bait" : "jei.fishtrap.chance_by_bait_with_net").withStyle(ChatFormatting.DARK_AQUA));
        for (BaitType bait : baits) {
            double chance = table.chance(entry, bait.luck() + netLuck, tool);
            tooltip.accept(Component.translatable("jei.fishtrap.bait_chance", new ItemStack(bait.item()).getHoverName(), FishTrapTooltips.percent(chance))
                    .withStyle(ChatFormatting.GRAY));
        }
    }
}
