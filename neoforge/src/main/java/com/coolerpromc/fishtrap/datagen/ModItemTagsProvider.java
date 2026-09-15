package com.coolerpromc.fishtrap.datagen;

import com.coolerpromc.fishtrap.Constants;
import com.coolerpromc.fishtrap.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Constants.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        // Lets cats, recipes that take any fish (like fish chum) and other mods treat trap catches as fish.
        TagAppender<Item, Item> fishes = tag(ItemTags.FISHES);
        ModItems.RAW_CATCHES.forEach(item -> fishes.add(item.get()));
        ModItems.COOKED_CATCHES.forEach(item -> fishes.add(item.get()));
    }
}
