# Bait

Bait decides two things:

- **Catch time.** How long the trap waits between catches, picked at random in the bait's range.
- **Luck.** How much the odds shift towards treasure and away from junk. See [how luck works](./catches#how-luck-works).

A [net](./nets) in the trap shortens the catch time and adds its own luck on top of the bait's.

<BaitTable />

::: tip
Hover over bait in your inventory to see its catch time and luck.
:::

## Crafting progression

Each tier is made from the one below it, plus something from a new part of the world. Early traps help you make the next tier: Fish Chum takes any fish, including your own catches.

### Plant Bait

Farm scraps. Slow, and with negative luck it catches more junk than any other bait.

<RecipeCard id="plant_bait" />

### Worm Bait

A night out fighting zombies turns seeds into worms.

<RecipeCard id="worm_bait" />

### Fish Chum

Use part of what your traps catch to catch more. Any item in `#minecraft:fishes` works, including the [new fish](./fish).

<RecipeCard id="fish_chum" />

### Glow Bait

Needs a trip to the lush caves for glow berries, and a glow squid.

<RecipeCard id="glow_bait" />

### Prismarine Lure

Ocean monument materials. Traps in oceans occasionally catch one, too.

<RecipeCard id="prismarine_lure" />

### Nautilus Lure

The best bait. Nautilus shells come from fishing, wandering traders, and lucky trap catches.

<RecipeCard id="nautilus_lure" />

## Catching bait

Traps sometimes catch bait back. This keeps a working trap going a little longer, but not forever:

- Worm Bait and Plant Bait in rivers and swamps
- Fish Chum in oceans
- Glow Bait in warm oceans
- Prismarine Lures in oceans (rare)

Any item can be made into bait with a datapack. See [Bait files](../datapacks/bait).
