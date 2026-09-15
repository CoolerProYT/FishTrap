package com.coolerpromc.fishtrap.bait;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;

/**
 * A fish trap bait definition, loaded from {@code data/<namespace>/bait/<name>.json}.
 *
 * @param item     the item that acts as bait
 * @param minTicks the shortest possible catch cycle, inclusive
 * @param maxTicks the longest possible catch cycle, inclusive
 * @param luck     passed to the loot roll as {@code LootContextParams} luck, scaling entry weights by their quality
 */
public record BaitType(Item item, int minTicks, int maxTicks, float luck) {
    public static final Codec<BaitType> CODEC = RecordCodecBuilder.<BaitType>create(instance -> instance.group(
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(BaitType::item),
            ExtraCodecs.POSITIVE_INT.fieldOf("minTicks").forGetter(BaitType::minTicks),
            ExtraCodecs.POSITIVE_INT.fieldOf("maxTicks").forGetter(BaitType::maxTicks),
            Codec.FLOAT.optionalFieldOf("luck", 0.0F).forGetter(BaitType::luck)
    ).apply(instance, BaitType::new)).validate(BaitType::validate);

    /** Synced to clients for tooltips and recipe viewers. */
    public static final StreamCodec<RegistryFriendlyByteBuf, BaitType> STREAM_CODEC = StreamCodec.composite(
            Item.STREAM_CODEC, bait -> bait.item().builtInRegistryHolder(),
            ByteBufCodecs.VAR_INT, BaitType::minTicks,
            ByteBufCodecs.VAR_INT, BaitType::maxTicks,
            ByteBufCodecs.FLOAT, BaitType::luck,
            (item, minTicks, maxTicks, luck) -> new BaitType(item.value(), minTicks, maxTicks, luck)
    );

    private static DataResult<BaitType> validate(BaitType bait) {
        if (bait.maxTicks < bait.minTicks) {
            return DataResult.error(() -> "maxTicks (" + bait.maxTicks + ") must not be lower than minTicks (" + bait.minTicks + ")");
        }
        return DataResult.success(bait);
    }

    public int rollTicks(RandomSource random) {
        return random.nextIntBetweenInclusive(this.minTicks, this.maxTicks);
    }
}
