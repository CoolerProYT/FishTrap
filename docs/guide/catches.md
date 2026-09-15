# Catches

Every catch is one roll on a catch table. Pick a table, a bait and a net to see the real odds:

<CatchExplorer />

## Which table a trap uses

A trap checks these in order and uses the first one that exists:

1. **A table for its exact biome**, for example `fish_trap/warm_ocean`. Modded biomes use `fish_trap/<namespace>/<biome>`.
2. **A biome category**, matched by biome tag, most specific first:

   | Category | Biomes |
   | --- | --- |
   | Warm Ocean | warm, lukewarm and deep lukewarm ocean |
   | Cold Ocean | cold, deep cold, frozen and deep frozen ocean |
   | Swamp | swamp, mangrove swamp |
   | Ocean | any other ocean (`#minecraft:is_ocean`) |
   | River | rivers (`#minecraft:is_river`) |

3. **The default table** for everywhere else, such as lakes and beaches.

## How luck works

Catch tables use Minecraft's own loot weights. Each entry has a **weight** and a **quality**, and the trap's luck changes the weight:

```
luck = bait luck + net luck
effective weight = max(floor(weight + quality × luck), 0)
chance = effective weight ÷ sum of all effective weights
```

- **Junk** has negative quality, so luck shrinks it and eventually removes it.
- **Treasure** has positive quality, so luck makes it more common.
- **Ordinary fish** have a quality of 0 or −1, so they shift only a little.

For example, in the ocean a Nautilus Lure (luck +5) removes kelp, seagrass and old boots completely, and raises the chance of a Heart of the Sea from 0.7% with Worm Bait to 4.1%.

## Catches that need a net

Some entries only roll when a particular net is in the trap. The **Rainbow Fish** needs a Netherite Net. Without it the entry is skipped entirely and doesn't count towards the total. In the explorer above, these entries show which net they need.
