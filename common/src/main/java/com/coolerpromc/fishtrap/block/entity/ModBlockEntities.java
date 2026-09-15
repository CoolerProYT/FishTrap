package com.coolerpromc.fishtrap.block.entity;

import com.coolerpromc.fishtrap.block.ModBlocks;
import com.coolerpromc.fishtrap.platform.Services;
import com.coolerpromc.fishtrap.platform.util.RegistryHandler;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.List;
import java.util.function.Supplier;

public class ModBlockEntities {
    public static final RegistryHandler<BlockEntityType<?>, BlockEntityType<FishTrapBlockEntity>> FISH_TRAP = Services.REGISTRY.registerBlockEntityType("fish_trap", FishTrapBlockEntity::new, List.<Supplier<? extends Block>>of(ModBlocks.FISH_TRAP));

    public static void load() {
    }
}
