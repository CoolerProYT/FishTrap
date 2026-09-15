# Advancement trigger

`fishtrap:fish_trap_catch` fires when a player takes an item out of a fish trap's catch slots, by clicking or shift-clicking.

| Field | Type | Meaning |
| --- | --- | --- |
| `player` | entity predicate, optional | Conditions on the player |
| `item` | item predicate, optional | Conditions on the item taken out. Leave it out to match any catch. |

## Example

Reward a player for pulling three lobsters out of traps at once:

```json
{
  "parent": "fishtrap:fish_trap/first_catch",
  "display": {
    "icon": { "id": "fishtrap:lobster" },
    "title": "Lobster Night",
    "description": "Take three lobsters out of a fish trap at once",
    "frame": "goal"
  },
  "criteria": {
    "lobsters": {
      "trigger": "fishtrap:fish_trap_catch",
      "conditions": {
        "item": { "items": "fishtrap:lobster", "count": { "min": 3 } }
      }
    }
  }
}
```
