package com.coolerpromc.fishtrap.client;

import com.coolerpromc.fishtrap.block.entity.FishTrapBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FishTrapRenderer implements BlockEntityRenderer<FishTrapBlockEntity, FishTrapRenderState> {
    private static final float BAIT_X = 4.0F;
    private static final float BAIT_Y = 5.5F;
    private static final float BAIT_Z = 8.0F;
    private static final float BAIT_SCALE = 0.15F;
    private static final float CATCH_Y = 0.6F;
    private static final float CATCH_SCALE = 0.22F;
    private static final float[][] CATCH_SPOTS = {{3.5F, 5.2F}, {7.0F, 10.8F}, {10.5F, 5.2F}, {3.5F, 10.8F}, {7.0F, 5.2F}, {10.5F, 10.8F}};

    private final ItemModelResolver itemModelResolver;

    public FishTrapRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public FishTrapRenderState createRenderState() {
        return new FishTrapRenderState();
    }

    @Override
    public void extractRenderState(FishTrapBlockEntity trap, FishTrapRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(trap, state, partialTicks, cameraPosition, breakProgress);
        Level level = trap.getLevel();
        int seed = (int) trap.getBlockPos().asLong();

        state.bait = new ItemStackRenderState();
        this.itemModelResolver.updateForTopItem(state.bait, trap.getBaitStack(), ItemDisplayContext.FIXED, level, null, seed);

        List<ItemStack> catches = trap.getCatches();
        List<ItemStackRenderState> catchStates = new ArrayList<>();
        for (int i = 0; i < Math.min(catches.size(), CATCH_SPOTS.length); i++) {
            ItemStackRenderState itemState = new ItemStackRenderState();
            this.itemModelResolver.updateForTopItem(itemState, catches.get(i), ItemDisplayContext.FIXED, level, null, seed + i + 1);
            catchStates.add(itemState);
        }
        state.catches = catchStates;
        state.baitSpin = level == null ? 0.0F : (level.getGameTime() % 360L + partialTicks) * 2.0F;
        state.seed = seed;
    }

    @Override
    public void submit(FishTrapRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (!state.bait.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(BAIT_X / 16.0F, BAIT_Y / 16.0F, BAIT_Z / 16.0F);
            poseStack.mulPose(Axis.YP.rotationDegrees(state.baitSpin));
            poseStack.scale(BAIT_SCALE, BAIT_SCALE, BAIT_SCALE);
            state.bait.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }

        for (int i = 0; i < state.catches.size(); i++) {
            ItemStackRenderState itemState = state.catches.get(i);
            if (itemState.isEmpty()) {
                continue;
            }
            float[] spot = CATCH_SPOTS[i];
            poseStack.pushPose();
            poseStack.translate(spot[0] / 16.0F, CATCH_Y / 16.0F, spot[1] / 16.0F);
            poseStack.mulPose(Axis.YP.rotationDegrees(Math.floorMod(state.seed * 31 + i * 97, 360)));
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
            poseStack.scale(CATCH_SCALE, CATCH_SCALE, CATCH_SCALE);
            itemState.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }
    }
}
