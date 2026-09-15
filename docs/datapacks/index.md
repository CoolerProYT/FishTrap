# Datapacks

Almost everything about how traps behave is data, so a datapack can change it without touching code.

| What | Where | Details |
| --- | --- | --- |
| Which items are bait, and how good they are | `data/<namespace>/bait/*.json` | [Bait files](./bait) |
| Which items are nets, and what they do | `data/<namespace>/net/*.json` | [Net files](./nets) |
| What each biome catches | `data/fishtrap/loot_table/fish_trap/*.json` | [Catch tables](./catch-tables) |
| Which biomes count as warm ocean, cold ocean or swamp | `data/fishtrap/tags/worldgen/biome/fish_trap/*.json` | [Catch tables](./catch-tables#biome-categories) |
| Advancements for trap catches | `data/<namespace>/advancement/*.json` | [Advancement trigger](./advancement-trigger) |

All of it reloads with `/reload`. Players' tooltips and JEI update straight away.
