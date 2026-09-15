package com.coolerpromc.fishtrap.upgrade;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;

/**
 * A fish trap net upgrade, loaded from {@code data/<namespace>/net/<name>.json}.
 *
 * @param item                the item that goes in the net slot
 * @param style               the funnel look shown on the trap
 * @param catchTimeMultiplier multiplies the bait's rolled wait (0.5 = twice as fast)
 * @param luck                added to the bait's luck for the catch roll
 * @param bonusCatchChance    chance (0 to 1) of rolling the catch table a second time in the same cycle
 */
public record NetType(Item item, NetStyle style, float catchTimeMultiplier, float luck, float bonusCatchChance) {
    public static final Codec<NetType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(NetType::item),
            NetStyle.CODEC.fieldOf("style").forGetter(NetType::style),
            Codec.floatRange(0.05F, 10.0F).optionalFieldOf("catchTimeMultiplier", 1.0F).forGetter(NetType::catchTimeMultiplier),
            Codec.FLOAT.optionalFieldOf("luck", 0.0F).forGetter(NetType::luck),
            Codec.floatRange(0.0F, 1.0F).optionalFieldOf("bonusCatchChance", 0.0F).forGetter(NetType::bonusCatchChance)
    ).apply(instance, NetType::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, NetType> STREAM_CODEC = StreamCodec.composite(
            Item.STREAM_CODEC, net -> net.item().builtInRegistryHolder(),
            NetStyle.STREAM_CODEC, NetType::style,
            ByteBufCodecs.FLOAT, NetType::catchTimeMultiplier,
            ByteBufCodecs.FLOAT, NetType::luck,
            ByteBufCodecs.FLOAT, NetType::bonusCatchChance,
            (item, style, catchTimeMultiplier, luck, bonusCatchChance) -> new NetType(item.value(), style, catchTimeMultiplier, luck, bonusCatchChance)
    );

    public int applyCatchTime(int ticks) {
        return Math.max(1, Math.round(ticks * this.catchTimeMultiplier));
    }
}
