package com.coolerpromc.fishtrap.datagen;

import com.coolerpromc.fishtrap.Constants;
import com.coolerpromc.fishtrap.advancement.FishTrapCatchTrigger;
import com.coolerpromc.fishtrap.item.ModItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModAdvancementProvider implements AdvancementSubProvider {
    public static AdvancementProvider create(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        return new AdvancementProvider(output, lookupProvider, List.of(new ModAdvancementProvider()));
    }

    @Override
    public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> output) {
        HolderGetter<Item> items = registries.lookupOrThrow(Registries.ITEM);

        AdvancementHolder root = Advancement.Builder.advancement()
                .display(ModItems.FISH_TRAP, title("root"), description("root"), Identifier.withDefaultNamespace("block/dark_prismarine"), AdvancementType.TASK, false, false, false)
                .addCriterion("has_fish_trap", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.FISH_TRAP))
                .save(output, id("root"));

        AdvancementHolder wormBait = obtain(output, root, "worm_bait", ModItems.WORM_BAIT, AdvancementType.TASK);
        AdvancementHolder fishChum = obtain(output, wormBait, "fish_chum", ModItems.FISH_CHUM, AdvancementType.TASK);
        AdvancementHolder glowBait = obtain(output, fishChum, "glow_bait", ModItems.GLOW_BAIT, AdvancementType.TASK);
        AdvancementHolder prismarineLure = obtain(output, glowBait, "prismarine_lure", ModItems.PRISMARINE_LURE, AdvancementType.TASK);
        obtain(output, prismarineLure, "nautilus_lure", ModItems.NAUTILUS_LURE, AdvancementType.GOAL);

        AdvancementHolder copperNet = obtain(output, root, "copper_net", ModItems.COPPER_NET, AdvancementType.TASK);
        obtain(output, copperNet, "netherite_net", ModItems.NETHERITE_NET, AdvancementType.GOAL);

        AdvancementHolder firstCatch = Advancement.Builder.advancement()
                .parent(root)
                .display(Items.COD, title("first_catch"), description("first_catch"), null, AdvancementType.TASK, true, true, false)
                .addCriterion("caught_anything", FishTrapCatchTrigger.TriggerInstance.caughtAnything())
                .save(output, id("first_catch"));

        Advancement.Builder.advancement()
                .parent(firstCatch)
                .display(ModItems.MACKEREL, title("fresh_catch"), description("fresh_catch"), null, AdvancementType.GOAL, true, true, false)
                .addCriterion("trout", caught(items, ModItems.TROUT))
                .addCriterion("catfish", caught(items, ModItems.CATFISH))
                .addCriterion("mackerel", caught(items, ModItems.MACKEREL))
                .save(output, id("fresh_catch"));

        Advancement.Builder.advancement()
                .parent(firstCatch)
                .display(ModItems.LOBSTER, title("shell_shocked"), description("shell_shocked"), null, AdvancementType.GOAL, true, true, false)
                .addCriterion("crab", caught(items, ModItems.CRAB))
                .addCriterion("lobster", caught(items, ModItems.LOBSTER))
                .addCriterion("crayfish", caught(items, ModItems.CRAYFISH))
                .save(output, id("shell_shocked"));

        Advancement.Builder.advancement()
                .parent(firstCatch)
                .display(Items.HEART_OF_THE_SEA, title("sunken_treasure"), description("sunken_treasure"), null, AdvancementType.CHALLENGE, true, true, false)
                .addCriterion("heart_of_the_sea", caught(items, Items.HEART_OF_THE_SEA))
                .save(output, id("sunken_treasure"));

        Advancement.Builder.advancement()
                .parent(firstCatch)
                .display(ModItems.RAINBOW_FISH, title("rainbow_fish"), description("rainbow_fish"), null, AdvancementType.CHALLENGE, true, true, false)
                .addCriterion("rainbow_fish", caught(items, ModItems.RAINBOW_FISH))
                .save(output, id("rainbow_fish"));
    }

    private static AdvancementHolder obtain(Consumer<AdvancementHolder> output, AdvancementHolder parent, String name, ItemLike item, AdvancementType type) {
        return Advancement.Builder.advancement()
                .parent(parent)
                .display(item, title(name), description(name), null, type, true, true, false)
                .addCriterion("has_" + name, InventoryChangeTrigger.TriggerInstance.hasItems(item))
                .save(output, id(name));
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
