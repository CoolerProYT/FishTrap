package com.coolerpromc.fishtrap.upgrade;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;

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
