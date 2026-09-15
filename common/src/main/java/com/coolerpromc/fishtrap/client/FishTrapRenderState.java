package com.coolerpromc.fishtrap.client;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;

import java.util.List;

public class FishTrapRenderState extends BlockEntityRenderState {
    public ItemStackRenderState bait = new ItemStackRenderState();
    public List<ItemStackRenderState> catches = List.of();
    public float baitSpin;
    public int seed;
}
