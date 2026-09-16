package com.coolerpromc.fishtrap.datagen;

import com.coolerpromc.fishtrap.Constants;
import com.coolerpromc.fishtrap.advancement.FishTrapCatchTrigger;
import com.coolerpromc.fishtrap.item.ModItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.InventoryChangeTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.List;

public class ModAdvancementProvider extends AdvancementSubProvider {
    public ModAdvancementProvider(BootstrapContext<Advancement> output) {
        super(output);
    }

    public static AdvancementProvider create() {
        return new AdvancementProvider(List.of(ModAdvancementProvider::new));
    }

    @Override
    public void generate() {
        HolderGetter<Item> items = this.output.lookup(Registries.ITEM);

        AdvancementHolder root = Advancement.Builder.advancement()
                .rootDisplay(ModItems.FISH_TRAP.asItem(), title("root"), description("root"), Identifier.withDefaultNamespace("block/dark_prismarine"), AdvancementType.TASK, false, false, false)
                .addCriterion("has_fish_trap", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.FISH_TRAP))
                .save(this.output, id("root"));

        AdvancementHolder wormBait = obtain(root, "worm_bait", ModItems.WORM_BAIT, AdvancementType.TASK);
        AdvancementHolder fishChum = obtain(wormBait, "fish_chum", ModItems.FISH_CHUM, AdvancementType.TASK);
        AdvancementHolder glowBait = obtain(fishChum, "glow_bait", ModItems.GLOW_BAIT, AdvancementType.TASK);
        AdvancementHolder prismarineLure = obtain(glowBait, "prismarine_lure", ModItems.PRISMARINE_LURE, AdvancementType.TASK);
        obtain(prismarineLure, "nautilus_lure", ModItems.NAUTILUS_LURE, AdvancementType.GOAL);

        AdvancementHolder copperNet = obtain(root, "copper_net", ModItems.COPPER_NET, AdvancementType.TASK);
        obtain(copperNet, "netherite_net", ModItems.NETHERITE_NET, AdvancementType.GOAL);

        AdvancementHolder firstCatch = Advancement.Builder.advancement()
                .parent(root)
                .display(Items.COD, title("first_catch"), description("first_catch"), AdvancementType.TASK, true, true, false)
                .addCriterion("caught_anything", FishTrapCatchTrigger.TriggerInstance.caughtAnything())
                .save(this.output, id("first_catch"));

        Advancement.Builder.advancement()
                .parent(firstCatch)
                .display(ModItems.MACKEREL.asItem(), title("fresh_catch"), description("fresh_catch"), AdvancementType.GOAL, true, true, false)
                .addCriterion("trout", caught(items, ModItems.TROUT))
                .addCriterion("catfish", caught(items, ModItems.CATFISH))
                .addCriterion("mackerel", caught(items, ModItems.MACKEREL))
                .save(this.output, id("fresh_catch"));

        Advancement.Builder.advancement()
                .parent(firstCatch)
                .display(ModItems.LOBSTER.asItem(), title("shell_shocked"), description("shell_shocked"), AdvancementType.GOAL, true, true, false)
                .addCriterion("crab", caught(items, ModItems.CRAB))
                .addCriterion("lobster", caught(items, ModItems.LOBSTER))
                .addCriterion("crayfish", caught(items, ModItems.CRAYFISH))
                .save(this.output, id("shell_shocked"));

        Advancement.Builder.advancement()
                .parent(firstCatch)
                .display(Items.HEART_OF_THE_SEA, title("sunken_treasure"), description("sunken_treasure"), AdvancementType.CHALLENGE, true, true, false)
                .addCriterion("heart_of_the_sea", caught(items, Items.HEART_OF_THE_SEA))
                .save(this.output, id("sunken_treasure"));

        Advancement.Builder.advancement()
                .parent(firstCatch)
                .display(ModItems.RAINBOW_FISH.asItem(), title("rainbow_fish"), description("rainbow_fish"), AdvancementType.CHALLENGE, true, true, false)
                .addCriterion("rainbow_fish", caught(items, ModItems.RAINBOW_FISH))
                .save(this.output, id("rainbow_fish"));
    }

    private AdvancementHolder obtain(AdvancementHolder parent, String name, ItemLike item, AdvancementType type) {
        return Advancement.Builder.advancement()
                .parent(parent)
                .display(item.asItem(), title(name), description(name), type, true, true, false)
                .addCriterion("has_" + name, InventoryChangeTrigger.TriggerInstance.hasItems(item))
                .save(this.output, id(name));
    }

    private static Criterion<FishTrapCatchTrigger.TriggerInstance> caught(HolderGetter<Item> items, ItemLike item) {
        return FishTrapCatchTrigger.TriggerInstance.caught(ItemPredicate.Builder.item().of(items, item).build());
    }

    private static Component title(String name) {
        return Component.translatable("advancements.fishtrap." + name + ".title");
    }

    private static Component description(String name) {
        return Component.translatable("advancements.fishtrap." + name + ".description");
    }

    private static String id(String name) {
        return Constants.MODID + ":fish_trap/" + name;
    }
}
