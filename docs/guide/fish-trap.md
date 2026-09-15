# The fish trap

<RecipeCard id="fish_trap" />

The trap is a mesh cage with a funnel entrance and a bait tube hanging from the lid. You can see the loaded bait spinning inside the tube, and up to six caught items lying on the floor of the cage. The funnel and tube change material to match the fitted [net](./nets).

## The menu

![Fish trap interface](/gui/fish_trap.png){.pixelated width=352}

- **Top left: bait.** Required. See [Bait](./bait).
- **Bottom left: net.** Optional. See [Nets](./nets).
- **Arrow.** Fills while the trap waits for its next catch.
- **Grid.** The nine catch slots.

If something is wrong, a red message appears under the slots: **Not underwater** or **Add bait**.

## Placement

A trap only works when it is **fully submerged**:

- the trap's block is waterlogged, **and**
- the block directly above it is water.

Out of water the trap pauses. Its timer is kept, so it carries on where it left off when you flood it again.

## How a catch cycle works

1. When bait is in the slot, the trap picks a random wait time within that bait's catch time range, then applies the net's multiplier.
2. When the wait runs out, **one bait is used** and the trap rolls its catch table with the bait's luck plus the net's.
3. Some nets have a chance to roll a **second catch** in the same cycle.
4. The catch goes into the nine output slots. If they are full, the extra items pop out above the trap.
5. If bait is left, a new wait time is picked. If the bait runs out, the trap goes idle until you add more.

Bait is used on every cycle, whether the catch is a fish or an old boot. There is no "missed" catch.

Each catch makes a small burst of bubbles and a splash, so you can hear a busy trap.

## Which table is used

The trap looks at the biome it is in and uses the most specific catch table available: a table for that exact biome, then a biome category (warm ocean, cold ocean, swamp, ocean, river), then the default table. See [Catches](./catches) for every table.

## Automation

Traps work with hoppers, pipes and other item transport on both loaders.

| Side | What it exposes |
| --- | --- |
| Bottom | The nine catch slots, extract only |
| Top and sides | The bait slot, insert only accepts bait |

A hopper under a trap will empty it, and a hopper above it can keep it stocked with bait. Nets can only be changed by hand.

## Breaking the trap

Breaking a trap drops the trap, its bait, its net and everything it has caught.
