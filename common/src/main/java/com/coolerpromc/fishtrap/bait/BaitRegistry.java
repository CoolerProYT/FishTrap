package com.coolerpromc.fishtrap.bait;

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

/**
 * Datapack driven bait lookup. Rebuilt from {@code data/<namespace>/bait/*.json} on every server data reload,
 * so {@code /reload} picks up changes without a restart. Clients receive a copy through the data sync payload.
 */
public class BaitRegistry extends SimpleJsonResourceReloadListener<BaitType> {
    public static final String DIRECTORY = "bait";
    public static final Identifier ID = Constants.id(DIRECTORY);

    private static volatile Map<Item, BaitType> baits = Map.of();

    public BaitRegistry() {
        super(BaitType.CODEC, FileToIdConverter.json(DIRECTORY));
    }

    @Override
    protected void apply(Map<Identifier, BaitType> entries, ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<Item, BaitType> byItem = new IdentityHashMap<>();
        // Sorted so that when two files claim the same item, the winner does not depend on hash order.
        new TreeMap<>(entries).forEach((id, bait) -> {
            if (byItem.put(bait.item(), bait) != null) {
                Constants.LOG.warn("Fish trap bait {} overrides an earlier entry for item {}", id, BuiltInRegistries.ITEM.getKey(bait.item()));
            }
        });
        baits = Collections.unmodifiableMap(byItem);
        Constants.LOG.info("Loaded {} fish trap bait types", byItem.size());
    }

    /** Replaces the lookup with the server's copy on the client. */
    public static void acceptSynced(Collection<BaitType> synced) {
        Map<Item, BaitType> byItem = new IdentityHashMap<>();
        synced.forEach(bait -> byItem.put(bait.item(), bait));
        baits = Collections.unmodifiableMap(byItem);
    }

    public static @Nullable BaitType get(ItemStack stack) {
        return stack.isEmpty() ? null : baits.get(stack.getItem());
    }

    public static boolean isBait(ItemStack stack) {
        return get(stack) != null;
    }

    public static Collection<BaitType> all() {
        return baits.values();
    }

    /** Weakest to strongest: by luck, then by speed. */
    public static List<BaitType> sorted() {
        return baits.values().stream()
                .sorted(Comparator.comparingDouble(BaitType::luck).thenComparing(BaitType::minTicks, Comparator.reverseOrder()))
                .toList();
    }
}
