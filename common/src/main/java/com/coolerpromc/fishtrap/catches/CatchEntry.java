package com.coolerpromc.fishtrap.catches;

import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * One {@code minecraft:item} entry of a fish trap loot table, as shown to players.
 *
 * @param requiredTools items from a {@code minecraft:match_tool} condition (the trap's net); empty when the entry has none
 */
public record CatchEntry(Holder<Item> item, int weight, int quality, List<Holder<Item>> requiredTools) {
    public static final StreamCodec<RegistryFriendlyByteBuf, CatchEntry> STREAM_CODEC = StreamCodec.composite(
            Item.STREAM_CODEC, CatchEntry::item,
            ByteBufCodecs.VAR_INT, CatchEntry::weight,
            ByteBufCodecs.INT, CatchEntry::quality,
            Item.STREAM_CODEC.apply(ByteBufCodecs.list()), CatchEntry::requiredTools,
            CatchEntry::new
    );

    /** Same formula vanilla loot pools use: {@code max(floor(weight + quality * luck), 0)}. */
    public int effectiveWeight(float luck) {
        return Math.max(Mth.floor(this.weight + this.quality * luck), 0);
    }

    /** Whether the entry can roll with this tool in the trap (an empty stack means no net). */
    public boolean availableWith(ItemStack tool) {
        return this.requiredTools.isEmpty() || this.requiredTools.stream().anyMatch(tool::is);
    }
}
