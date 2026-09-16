package com.coolerpromc.fishtrap.catches;

import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record CatchEntry(Holder<Item> item, int weight, int quality, List<Holder<Item>> requiredTools) {
    public static final StreamCodec<RegistryFriendlyByteBuf, CatchEntry> STREAM_CODEC = StreamCodec.composite(
            Item.STREAM_CODEC, CatchEntry::item,
            ByteBufCodecs.VAR_INT, CatchEntry::weight,
            ByteBufCodecs.INT, CatchEntry::quality,
            Item.STREAM_CODEC.apply(ByteBufCodecs.list()), CatchEntry::requiredTools,
            CatchEntry::new
    );

    public int effectiveWeight(float luck) {
        return Math.max(Mth.floor(this.weight + this.quality * luck), 0);
    }

    public boolean availableWith(ItemStack tool) {
        return this.requiredTools.isEmpty() || this.requiredTools.stream().anyMatch(tool::is);
    }
}
