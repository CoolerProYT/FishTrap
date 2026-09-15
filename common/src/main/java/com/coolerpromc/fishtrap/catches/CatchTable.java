package com.coolerpromc.fishtrap.catches;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/** A flattened fish trap loot table ({@code <namespace>:fish_trap/<name>}). */
public record CatchTable(Identifier id, List<CatchEntry> entries) {
    public static final StreamCodec<RegistryFriendlyByteBuf, CatchTable> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC, CatchTable::id,
            CatchEntry.STREAM_CODEC.apply(ByteBufCodecs.list()), CatchTable::entries,
            CatchTable::new
    );

    /**
     * Chance of rolling {@code entry} with the given total luck (bait + net) and net in the trap.
     * Entries whose tool condition does not match the net cannot roll and do not count towards the total.
     */
    public double chance(CatchEntry entry, float luck, ItemStack tool) {
        if (!entry.availableWith(tool)) {
            return 0.0;
        }
        int total = this.entries.stream().filter(e -> e.availableWith(tool)).mapToInt(e -> e.effectiveWeight(luck)).sum();
        return total == 0 ? 0.0 : entry.effectiveWeight(luck) / (double) total;
    }

    /** {@code catch_table.<namespace>.<name>}, falling back to a prettified name for tables added by datapacks. */
    public Component displayName() {
        String path = this.id.getPath().startsWith(CatchTables.PREFIX) ? this.id.getPath().substring(CatchTables.PREFIX.length()) : this.id.getPath();
        String fallback = Arrays.stream(path.split("[/_]"))
                .filter(word -> !word.isEmpty())
                .map(word -> word.substring(0, 1).toUpperCase(Locale.ROOT) + word.substring(1))
                .collect(Collectors.joining(" "));
        return Component.translatableWithFallback("catch_table." + this.id.getNamespace() + "." + path.replace('/', '.'), fallback);
    }
}
