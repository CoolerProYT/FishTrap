package com.coolerpromc.fishtrap.upgrade;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;

/** How the funnel and bait tube look; a blockstate property, so each style has its own block model. */
public enum NetStyle implements StringRepresentable {
    PLASTIC("plastic"),
    COPPER("copper"),
    IRON("iron"),
    GOLD("gold"),
    DIAMOND("diamond"),
    NETHERITE("netherite");

    public static final Codec<NetStyle> CODEC = StringRepresentable.fromEnum(NetStyle::values);
    public static final StreamCodec<ByteBuf, NetStyle> STREAM_CODEC = ByteBufCodecs.VAR_INT.map(id -> values()[id], NetStyle::ordinal);

    private final String name;

    NetStyle(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
