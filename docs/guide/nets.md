# Nets

Under the bait slot, every trap has a slot for a **net**. Without a net the trap uses its basic blue plastic funnel. A net upgrades the trap in three ways:

- **Faster catches.** The bait's wait time is multiplied, so the trap catches more often.
- **More luck.** The net's luck is added to the bait's, pushing catches further towards treasure.
- **Bonus catches.** Some nets have a chance to roll a second catch in the same cycle. That second catch does not use up any extra bait.

You can see which net a trap has from outside: the funnel and bait tube change to the net's material.

<NetTable />

::: tip
Hover over a net in your inventory to see its stats.
:::

## Crafting progression

Start with copper. Each later net is woven around the one before it, and the Netherite Net is a smithing upgrade like netherite tools.

### Copper Net

<RecipeCard id="copper_net" />

### Iron Net

<RecipeCard id="iron_net" />

### Gold Net

Gold is lucky: the Gold Net adds as much luck as a Diamond Net, but is slower.

<RecipeCard id="gold_net" />

### Diamond Net

<RecipeCard id="diamond_net" />

### Netherite Net

The best net, and the only one that can catch the <ItemSlot id="fishtrap:rainbow_fish" label />. See [New fish](./fish#rainbow-fish).

<RecipeCard id="netherite_net_smithing" />

## Bait and nets together

The two combine:

- **Catch time** = the bait's catch time × the net's multiplier
- **Luck** = the bait's luck + the net's luck

For example, a Nautilus Lure (20–30 s, luck +5) in a trap with a Netherite Net (×0.5, luck +3) catches every **10–15 seconds** with **luck +8**, and 40% of cycles bring in two catches.

Try combinations in the [catch explorer](./catches).

## Automation

Hoppers and pipes cannot insert or remove nets, so an automated trap keeps its net. Change nets by hand in the trap's menu.
