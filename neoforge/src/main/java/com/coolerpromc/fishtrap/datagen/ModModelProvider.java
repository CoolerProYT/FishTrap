package com.coolerpromc.fishtrap.datagen;

import com.coolerpromc.fishtrap.Constants;
import com.coolerpromc.fishtrap.block.ModBlocks;
import com.coolerpromc.fishtrap.block.custom.FishTrapBlock;
import com.coolerpromc.fishtrap.item.ModItems;
import com.coolerpromc.fishtrap.upgrade.NetStyle;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, Constants.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        // The trap geometry is hand built (see blockbench/fish_trap.bbmodel) and lives in common/src/main/resources,
        // together with one child model per net style that only swaps the funnel texture. The block item uses the plain model.
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(ModBlocks.FISH_TRAP.get())
                .with(PropertyDispatch.initial(FishTrapBlock.NET).generate(style -> BlockModelGenerators.plainVariant(netModel(style)))));

        ModItems.BAITS.forEach(bait -> itemModels.generateFlatItem(bait.get(), ModelTemplates.FLAT_ITEM));
        ModItems.NETS.forEach(net -> itemModels.generateFlatItem(net.get(), ModelTemplates.FLAT_ITEM));
        ModItems.RAW_CATCHES.forEach(item -> itemModels.generateFlatItem(item.get(), ModelTemplates.FLAT_ITEM));
        ModItems.COOKED_CATCHES.forEach(item -> itemModels.generateFlatItem(item.get(), ModelTemplates.FLAT_ITEM));
        itemModels.generateFlatItem(ModItems.RAINBOW_FISH.get(), ModelTemplates.FLAT_ITEM);
    }

    private static Identifier netModel(NetStyle style) {
        return Constants.id(style == NetStyle.PLASTIC ? "block/fish_trap" : "block/fish_trap_" + style.getSerializedName());
    }
}
