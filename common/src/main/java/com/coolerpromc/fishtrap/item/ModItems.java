package com.coolerpromc.fishtrap.item;

import com.coolerpromc.fishtrap.block.ModBlocks;
import com.coolerpromc.fishtrap.platform.Services;
import com.coolerpromc.fishtrap.platform.util.RegistryHandler;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;

import java.util.List;

public class ModItems {
    public static final RegistryHandler.Items<BlockItem> FISH_TRAP = Services.REGISTRY.registerItem("fish_trap", properties -> new BlockItem(ModBlocks.FISH_TRAP.get(), properties.useBlockDescriptionPrefix()));

    public static final RegistryHandler.Items<Item> PLANT_BAIT = bait("plant_bait");
    public static final RegistryHandler.Items<Item> WORM_BAIT = bait("worm_bait");
    public static final RegistryHandler.Items<Item> FISH_CHUM = bait("fish_chum");
    public static final RegistryHandler.Items<Item> GLOW_BAIT = bait("glow_bait");
    public static final RegistryHandler.Items<Item> PRISMARINE_LURE = bait("prismarine_lure");
    public static final RegistryHandler.Items<Item> NAUTILUS_LURE = bait("nautilus_lure");

    public static final List<RegistryHandler.Items<Item>> BAITS = List.of(PLANT_BAIT, WORM_BAIT, FISH_CHUM, GLOW_BAIT, PRISMARINE_LURE, NAUTILUS_LURE);

    public static final RegistryHandler.Items<Item> COPPER_NET = net("copper_net");
    public static final RegistryHandler.Items<Item> IRON_NET = net("iron_net");
    public static final RegistryHandler.Items<Item> GOLD_NET = net("gold_net");
    public static final RegistryHandler.Items<Item> DIAMOND_NET = net("diamond_net");
    public static final RegistryHandler.Items<Item> NETHERITE_NET = Services.REGISTRY.registerItem("netherite_net", Item::new, new Item.Properties().stacksTo(1).fireResistant());

    public static final List<RegistryHandler.Items<Item>> NETS = List.of(COPPER_NET, IRON_NET, GOLD_NET, DIAMOND_NET, NETHERITE_NET);

    public static final RegistryHandler.Items<Item> TROUT = food("trout", 2, 0.1F);
    public static final RegistryHandler.Items<Item> COOKED_TROUT = food("cooked_trout", 6, 0.8F);
    public static final RegistryHandler.Items<Item> CATFISH = food("catfish", 3, 0.1F);
    public static final RegistryHandler.Items<Item> COOKED_CATFISH = food("cooked_catfish", 7, 0.7F);
    public static final RegistryHandler.Items<Item> MACKEREL = food("mackerel", 2, 0.1F);
    public static final RegistryHandler.Items<Item> COOKED_MACKEREL = food("cooked_mackerel", 5, 0.6F);
    public static final RegistryHandler.Items<Item> CRAB = food("crab", 2, 0.1F);
    public static final RegistryHandler.Items<Item> COOKED_CRAB = food("cooked_crab", 6, 0.7F);
    public static final RegistryHandler.Items<Item> LOBSTER = food("lobster", 3, 0.2F);
    public static final RegistryHandler.Items<Item> COOKED_LOBSTER = food("cooked_lobster", 8, 0.9F);
    public static final RegistryHandler.Items<Item> CRAYFISH = food("crayfish", 1, 0.1F);
    public static final RegistryHandler.Items<Item> COOKED_CRAYFISH = food("cooked_crayfish", 4, 0.5F);

    public static final List<RegistryHandler.Items<Item>> RAW_CATCHES = List.of(TROUT, CATFISH, MACKEREL, CRAB, LOBSTER, CRAYFISH);
    public static final List<RegistryHandler.Items<Item>> COOKED_CATCHES = List.of(COOKED_TROUT, COOKED_CATFISH, COOKED_MACKEREL, COOKED_CRAB, COOKED_LOBSTER, COOKED_CRAYFISH);

    public static final RegistryHandler.Items<Item> RAINBOW_FISH = Services.REGISTRY.registerItem("rainbow_fish", Item::new, new Item.Properties()
            .rarity(Rarity.EPIC)
            .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
            .food(new FoodProperties.Builder().nutrition(6).saturationModifier(1.2F).alwaysEdible().build(),
                    Consumables.defaultFood().onConsume(new ApplyStatusEffectsConsumeEffect(List.of(
                            new MobEffectInstance(MobEffects.ABSORPTION, 2400, 4),
                            new MobEffectInstance(MobEffects.RESISTANCE, 1200, 1)
                    ))).build()));

    private static RegistryHandler.Items<Item> bait(String name) {
        return Services.REGISTRY.registerItem(name, Item::new);
    }

    private static RegistryHandler.Items<Item> net(String name) {
        return Services.REGISTRY.registerItem(name, Item::new, new Item.Properties().stacksTo(1));
    }

    private static RegistryHandler.Items<Item> food(String name, int nutrition, float saturation) {
        return Services.REGISTRY.registerItem(name, Item::new, new Item.Properties().food(new FoodProperties.Builder().nutrition(nutrition).saturationModifier(saturation).build()));
    }

    public static void load() {
    }
}
