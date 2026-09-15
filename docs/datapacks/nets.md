# Net files

Any item can be a net. Each file in `data/<namespace>/net/` defines one:

```json
{
  "item": "minecraft:cobweb",
  "style": "iron",
  "catchTimeMultiplier": 0.85,
  "luck": 1.5,
  "bonusCatchChance": 0.05
}
```

| Field | Type | Meaning |
| --- | --- | --- |
| `item` | item id | The item that goes in the net slot |
| `style` | `plastic`, `copper`, `iron`, `gold`, `diamond` or `netherite` | Which funnel look the trap shows |
| `catchTimeMultiplier` | number from 0.05 to 10, optional (default `1`) | Multiplies the bait's wait. `0.5` catches twice as often. |
| `luck` | number, optional (default `0`) | Added to the bait's luck for the catch roll |
| `bonusCatchChance` | number from 0 to 1, optional (default `0`) | Chance of a second catch roll in the same cycle, without using extra bait |

`style` only chooses one of the built-in looks. New looks need a resource pack that replaces `fishtrap:block/fish_trap_funnel_<style>`.

## Changing built-in nets

Overwrite a file at the same path, for example `data/fishtrap/net/gold_net.json`. If two files use the same item, the one whose file id sorts last wins, and the server log notes it.

## Catches that need a net

The trap passes its net to the catch roll as the loot **tool** (or a fishing rod when no net is fitted). A catch table entry can require a net with vanilla's `minecraft:match_tool` condition:

```json
{
  "type": "minecraft:item",
  "name": "fishtrap:rainbow_fish",
  "weight": 1,
  "quality": 1,
  "conditions": [
    {
      "condition": "minecraft:match_tool",
      "predicate": { "items": "fishtrap:netherite_net" }
    }
  ]
}
```

JEI and this wiki show that requirement, as long as `items` lists item ids rather than a tag.

## Current values

<NetTable />
