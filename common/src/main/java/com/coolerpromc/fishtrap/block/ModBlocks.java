package com.coolerpromc.fishtrap.block;

import com.coolerpromc.fishtrap.block.custom.FishTrapBlock;
import com.coolerpromc.fishtrap.platform.Services;
import com.coolerpromc.fishtrap.platform.util.RegistryHandler;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class ModBlocks {
    public static final RegistryHandler.Blocks<FishTrapBlock> FISH_TRAP = Services.REGISTRY.registerBlock("fish_trap", FishTrapBlock::new,
            BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(1.5F).sound(SoundType.WOOD).noOcclusion());

    public static void load() {
    }
}
