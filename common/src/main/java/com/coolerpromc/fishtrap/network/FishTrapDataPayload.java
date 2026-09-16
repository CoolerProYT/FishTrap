package com.coolerpromc.fishtrap.network;

import com.coolerpromc.fishtrap.Constants;
import com.coolerpromc.fishtrap.bait.BaitRegistry;
import com.coolerpromc.fishtrap.bait.BaitType;
import com.coolerpromc.fishtrap.catches.CatchTable;
import com.coolerpromc.fishtrap.catches.CatchTables;
import com.coolerpromc.fishtrap.platform.util.PayloadContext;
import com.coolerpromc.fishtrap.upgrade.NetRegistry;
import com.coolerpromc.fishtrap.upgrade.NetType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;

import java.util.List;

public record FishTrapDataPayload(List<BaitType> baits, List<NetType> nets, List<CatchTable> tables) implements HandledCustomPacketPayload {
    public static final Type<FishTrapDataPayload> TYPE = new Type<>(Constants.id("fish_trap_data"));

    public static final StreamCodec<RegistryFriendlyByteBuf, FishTrapDataPayload> STREAM_CODEC = StreamCodec.composite(
            BaitType.STREAM_CODEC.apply(ByteBufCodecs.list()), FishTrapDataPayload::baits,
            NetType.STREAM_CODEC.apply(ByteBufCodecs.list()), FishTrapDataPayload::nets,
            CatchTable.STREAM_CODEC.apply(ByteBufCodecs.list()), FishTrapDataPayload::tables,
            FishTrapDataPayload::new
    );

    public static FishTrapDataPayload create(MinecraftServer server) {
        return new FishTrapDataPayload(List.copyOf(BaitRegistry.all()), List.copyOf(NetRegistry.all()), CatchTables.collect(server));
    }

    @Override
    public void handle(PayloadContext context) {
        context.execute(() -> {
            BaitRegistry.acceptSynced(this.baits);
            NetRegistry.acceptSynced(this.nets);
            CatchTables.acceptSynced(this.tables);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
