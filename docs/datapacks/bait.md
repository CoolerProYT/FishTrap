# Bait files

Any item can be bait. Each file in `data/<namespace>/bait/` defines one:

```json
{
  "item": "minecraft:sweet_berries",
  "minTicks": 1400,
  "maxTicks": 2400,
  "luck": 0.5
}
```

| Field | Type | Meaning |
| --- | --- | --- |
| `item` | item id | The item used as bait |
| `minTicks` | positive integer | Shortest wait between catches, in ticks (20 ticks = 1 second) |
| `maxTicks` | positive integer | Longest wait, at least `minTicks` |
| `luck` | number, optional (default `0`) | Passed to the catch roll. Positive values favour treasure, negative values favour junk. |

The wait is picked at random between `minTicks` and `maxTicks`, including both ends. For the maths behind luck, see [how luck works](../guide/catches#how-luck-works).

## Changing built-in bait

Overwrite a file at the same path, for example `data/fishtrap/bait/worm_bait.json`, to change a built-in bait. If two files use the same item, the one whose file id sorts last wins, and the server log notes it.

## Current values

These are the values the mod ships with:

<BaitTable />
