# Catch tables

Catch tables are normal loot tables of type `minecraft:fishing`, stored in `data/fishtrap/loot_table/fish_trap/`.

```json
{
  "type": "minecraft:fishing",
  "pools": [
    {
      "rolls": 1,
      "entries": [
        { "type": "minecraft:item", "name": "minecraft:cod", "weight": 50, "quality": -1 },
        { "type": "minecraft:item", "name": "minecraft:stick", "weight": 6, "quality": -2 },
        { "type": "minecraft:item", "name": "minecraft:name_tag", "weight": 1, "quality": 2 }
      ]
    }
  ]
}
```

Use `weight` and `quality` for rarity. The bait's luck is passed to the roll, so you do not need any custom functions. Everything vanilla loot tables support, such as functions and conditions, works too.

## Table lookup

For a trap in biome `<namespace>:<biome>`, the first table that exists is used:

1. `fishtrap:fish_trap/<biome>` for vanilla biomes, or `fishtrap:fish_trap/<namespace>/<biome>` for modded ones
2. the first matching [biome category](#biome-categories)
3. `fishtrap:fish_trap/default`

So a datapack can add a table for a single biome, for example `fish_trap/mangrove_swamp.json`, and it takes over from the swamp category there.

## Biome categories

Categories are checked in this order:

| Table | Biome tag |
| --- | --- |
| `warm_ocean` | `#fishtrap:fish_trap/warm_ocean` |
| `cold_ocean` | `#fishtrap:fish_trap/cold_ocean` |
| `swamp` | `#fishtrap:fish_trap/swamp` |
| `ocean` | `#minecraft:is_ocean` |
| `river` | `#minecraft:is_river` |

To send a modded biome to a category, add it to the tag:

```json
// data/fishtrap/tags/worldgen/biome/fish_trap/swamp.json
{
  "values": ["examplemod:bayou"]
}
```

## Catches that need a net

The trap's net is passed to the roll as the loot tool, so an entry can require one with `minecraft:match_tool`. See [Net files](./nets#catches-that-need-a-net) for an example.

## Built-in tables

<CatchExplorer />
