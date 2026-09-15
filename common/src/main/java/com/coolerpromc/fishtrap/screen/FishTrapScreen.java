package com.coolerpromc.fishtrap.screen;

import com.coolerpromc.fishtrap.Constants;
import com.coolerpromc.fishtrap.block.entity.FishTrapBlockEntity;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

public class FishTrapScreen extends AbstractContainerScreen<FishTrapMenu> {
    public static final Identifier TEXTURE = Constants.id("textures/gui/container/fish_trap.png");
    private static final int ARROW_X = 60;
    private static final int ARROW_Y = 36;
    private static final int ARROW_U = 176;
    private static final int ARROW_V = 0;
    private static final int ARROW_WIDTH = 22;
    private static final int ARROW_HEIGHT = 15;
    private static final int STATUS_Y = 67;
    private static final int STATUS_COLOR = 0xFFB02E26;
    private static final Component NOT_SUBMERGED = Component.translatable("gui.fishtrap.fish_trap.not_submerged");
    private static final Component NO_BAIT = Component.translatable("gui.fishtrap.fish_trap.no_bait");

    public FishTrapScreen(FishTrapMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, 176, FishTrapMenu.IMAGE_HEIGHT);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);

        int progress = Mth.ceil(this.menu.getCatchProgress() * ARROW_WIDTH);
        if (progress > 0) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos + ARROW_X, this.topPos + ARROW_Y, ARROW_U, ARROW_V, progress, ARROW_HEIGHT, 256, 256);
        }
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractLabels(graphics, mouseX, mouseY);
        Component status = switch (this.menu.getStatus()) {
            case FishTrapBlockEntity.STATUS_NOT_SUBMERGED -> NOT_SUBMERGED;
            case FishTrapBlockEntity.STATUS_NO_BAIT -> NO_BAIT;
            default -> null;
        };
        if (status != null) {
            // Centred under the bait and net slots (16px item area starting at BAIT_SLOT_X).
            int centerX = FishTrapMenu.BAIT_SLOT_X + 8;
            graphics.text(this.font, status, centerX - this.font.width(status) / 2, STATUS_Y, STATUS_COLOR, false);
        }
    }
}
