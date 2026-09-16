package com.coolerpromc.fishtrap.loot;

import com.coolerpromc.fishtrap.Constants;
import com.coolerpromc.fishtrap.catches.CatchTables;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.ReloadableServerRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class FishTrapLootTables {
    public static final ResourceKey<LootTable> DEFAULT = key("default");
    public static final ResourceKey<LootTable> OCEAN = key("ocean");
    public static final ResourceKey<LootTable> WARM_OCEAN = key("warm_ocean");
    public static final ResourceKey<LootTable> COLD_OCEAN = key("cold_ocean");
    public static final ResourceKey<LootTable> RIVER = key("river");
    public static final ResourceKey<LootTable> SWAMP = key("swamp");

    public static final TagKey<Biome> WARM_OCEAN_BIOMES = biomeTag("warm_ocean");
    public static final TagKey<Biome> COLD_OCEAN_BIOMES = biomeTag("cold_ocean");
    public static final TagKey<Biome> SWAMP_BIOMES = biomeTag("swamp");

    private static final List<Map.Entry<TagKey<Biome>, ResourceKey<LootTable>>> CATEGORIES = List.of(
            Map.entry(WARM_OCEAN_BIOMES, WARM_OCEAN),
            Map.entry(COLD_OCEAN_BIOMES, COLD_OCEAN),
            Map.entry(SWAMP_BIOMES, SWAMP),
            Map.entry(BiomeTags.IS_OCEAN, OCEAN),
            Map.entry(BiomeTags.IS_RIVER, RIVER)
    );

    private FishTrapLootTables() {
    }

    public static LootTable resolve(ServerLevel level, BlockPos pos) {
        ReloadableServerRegistries.Holder registries = level.getServer().reloadableRegistries();
        Holder<Biome> biome = level.getBiome(pos);

        Optional<ResourceKey<Biome>> biomeKey = biome.unwrapKey();
        if (biomeKey.isPresent()) {
            LootTable table = registries.getLootTable(forBiome(biomeKey.get()));
            if (table != LootTable.EMPTY) {
                return table;
            }
        }

        for (Map.Entry<TagKey<Biome>, ResourceKey<LootTable>> category : CATEGORIES) {
            if (biome.is(category.getKey())) {
                LootTable table = registries.getLootTable(category.getValue());
                if (table != LootTable.EMPTY) {
                    return table;
                }
            }
        }

        return registries.getLootTable(DEFAULT);
    }

    public static ResourceKey<LootTable> forBiome(ResourceKey<Biome> biome) {
        Identifier id = biome.identifier();
        return key(Identifier.DEFAULT_NAMESPACE.equals(id.getNamespace()) ? id.getPath() : id.getNamespace() + "/" + id.getPath());
    }

    private static ResourceKey<LootTable> key(String path) {
        return ResourceKey.create(Registries.LOOT_TABLE, Constants.id(CatchTables.PREFIX + path));
    }

    private static TagKey<Biome> biomeTag(String name) {
        return TagKey.create(Registries.BIOME, Constants.id(CatchTables.PREFIX + name));
    }
}
