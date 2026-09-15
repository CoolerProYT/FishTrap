package com.coolerpromc.fishtrap.catches;

import com.coolerpromc.fishtrap.Constants;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Loot tables are server-only data, so the server flattens every {@code fishtrap:fish_trap/*} table into plain
 * item/weight/quality entries and syncs them; recipe viewers read the client copy.
 */
public final class CatchTables {
    public static final String PREFIX = "fish_trap/";

    private static volatile List<CatchTable> synced = List.of();
    private static final List<Runnable> SYNC_LISTENERS = new CopyOnWriteArrayList<>();

    private CatchTables() {
    }

    public static List<CatchTable> collect(MinecraftServer server) {
        HolderLookup.Provider registries = server.reloadableRegistries().lookup();
        RegistryOps<JsonElement> ops = registries.createSerializationContext(JsonOps.INSTANCE);
        List<CatchTable> tables = new ArrayList<>();
        registries.lookupOrThrow(Registries.LOOT_TABLE).listElements()
                .filter(holder -> {
                    Identifier id = holder.key().identifier();
                    return id.getNamespace().equals(Constants.MODID) && id.getPath().startsWith(PREFIX);
                })
                .sorted(Comparator.comparing(holder -> holder.key().identifier()))
                // Encoding back to JSON avoids reaching into the loot table's private pool/entry fields.
                .forEach(holder -> LootTable.DIRECT_CODEC.encodeStart(ops, holder.value())
                        .ifSuccess(json -> tables.add(new CatchTable(holder.key().identifier(), readEntries(json)))));
        return tables;
    }

    /** Plain item entries only; nested tables and alternatives are not flattened. Tool (net) conditions are kept. */
    private static List<CatchEntry> readEntries(JsonElement json) {
        List<CatchEntry> entries = new ArrayList<>();
        if (!json.isJsonObject()) {
            return entries;
        }
        for (JsonElement pool : GsonHelper.getAsJsonArray(json.getAsJsonObject(), "pools", new JsonArray())) {
            if (!pool.isJsonObject()) {
                continue;
            }
            for (JsonElement element : GsonHelper.getAsJsonArray(pool.getAsJsonObject(), "entries", new JsonArray())) {
                if (!element.isJsonObject()) {
                    continue;
                }
                JsonObject entry = element.getAsJsonObject();
                if (!"minecraft:item".equals(GsonHelper.getAsString(entry, "type", ""))) {
                    continue;
                }
                Identifier itemId = Identifier.tryParse(GsonHelper.getAsString(entry, "name", ""));
                if (itemId == null) {
                    continue;
                }
                List<Holder<Item>> requiredTools = readRequiredTools(entry);
                BuiltInRegistries.ITEM.get(itemId).ifPresent(item -> entries.add(
                        new CatchEntry(item, GsonHelper.getAsInt(entry, "weight", 1), GsonHelper.getAsInt(entry, "quality", 0), requiredTools)));
            }
        }
        return entries;
    }

    /** Items listed by {@code minecraft:match_tool} conditions; tags are not expanded. */
    private static List<Holder<Item>> readRequiredTools(JsonObject entry) {
        List<Holder<Item>> tools = new ArrayList<>();
        for (JsonElement element : GsonHelper.getAsJsonArray(entry, "conditions", new JsonArray())) {
            if (!element.isJsonObject() || !"minecraft:match_tool".equals(GsonHelper.getAsString(element.getAsJsonObject(), "condition", ""))) {
                continue;
            }
            JsonElement items = GsonHelper.getAsJsonObject(element.getAsJsonObject(), "predicate", new JsonObject()).get("items");
            List<String> ids = new ArrayList<>();
            if (items != null && items.isJsonPrimitive()) {
                ids.add(items.getAsString());
            } else if (items != null && items.isJsonArray()) {
                items.getAsJsonArray().forEach(item -> ids.add(item.getAsString()));
            }
            for (String id : ids) {
                Identifier toolId = id.startsWith("#") ? null : Identifier.tryParse(id);
                if (toolId != null) {
                    BuiltInRegistries.ITEM.get(toolId).ifPresent(tools::add);
                }
            }
        }
        return tools;
    }

    public static void acceptSynced(List<CatchTable> tables) {
        synced = List.copyOf(tables);
        SYNC_LISTENERS.forEach(Runnable::run);
    }

    public static List<CatchTable> synced() {
        return synced;
    }

    /** Called on the client thread after every sync (join and {@code /reload}). */
    public static void onSync(Runnable listener) {
        SYNC_LISTENERS.add(listener);
    }
}
