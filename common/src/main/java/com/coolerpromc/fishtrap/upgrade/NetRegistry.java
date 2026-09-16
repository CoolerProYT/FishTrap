package com.coolerpromc.fishtrap.upgrade;

import com.coolerpromc.fishtrap.Constants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class NetRegistry extends SimpleJsonResourceReloadListener<NetType> {
    public static final String DIRECTORY = "net";
    public static final Identifier ID = Constants.id(DIRECTORY);

    private static volatile Map<Item, NetType> nets = Map.of();

    public NetRegistry() {
        super(NetType.CODEC, FileToIdConverter.json(DIRECTORY));
    }

    @Override
    protected void apply(Map<Identifier, NetType> entries, ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<Item, NetType> byItem = new IdentityHashMap<>();
        new TreeMap<>(entries).forEach((id, net) -> {
            if (byItem.put(net.item(), net) != null) {
                Constants.LOG.warn("Fish trap net {} overrides an earlier entry for item {}", id, BuiltInRegistries.ITEM.getKey(net.item()));
            }
        });
        nets = Collections.unmodifiableMap(byItem);
        Constants.LOG.info("Loaded {} fish trap nets", byItem.size());
    }

    public static void acceptSynced(Collection<NetType> synced) {
        Map<Item, NetType> byItem = new IdentityHashMap<>();
        synced.forEach(net -> byItem.put(net.item(), net));
        nets = Collections.unmodifiableMap(byItem);
    }

    public static @Nullable NetType get(ItemStack stack) {
        return stack.isEmpty() ? null : nets.get(stack.getItem());
    }

    public static boolean isNet(ItemStack stack) {
        return get(stack) != null;
    }

    public static Collection<NetType> all() {
        return nets.values();
    }

    public static List<NetType> sorted() {
        return nets.values().stream()
                .sorted(Comparator.comparingDouble(NetType::luck).thenComparing(NetType::catchTimeMultiplier, Comparator.reverseOrder()))
                .toList();
    }
}
