package com.coolerpromc.fishtrap.datagen;

import com.coolerpromc.fishtrap.Constants;
import com.coolerpromc.fishtrap.block.ModBlocks;
import com.coolerpromc.fishtrap.item.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(PackOutput output) {
        super(output, Constants.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(ModBlocks.FISH_TRAP.get(), "Fish Trap");
        add(ModItems.PLANT_BAIT.get(), "Plant Bait");
        add(ModItems.WORM_BAIT.get(), "Worm Bait");
        add(ModItems.FISH_CHUM.get(), "Fish Chum");
        add(ModItems.GLOW_BAIT.get(), "Glow Bait");
        add(ModItems.PRISMARINE_LURE.get(), "Prismarine Lure");
        add(ModItems.NAUTILUS_LURE.get(), "Nautilus Lure");

        add(ModItems.COPPER_NET.get(), "Copper Net");
        add(ModItems.IRON_NET.get(), "Iron Net");
        add(ModItems.GOLD_NET.get(), "Gold Net");
        add(ModItems.DIAMOND_NET.get(), "Diamond Net");
        add(ModItems.NETHERITE_NET.get(), "Netherite Net");

        add(ModItems.TROUT.get(), "Raw Trout");
        add(ModItems.COOKED_TROUT.get(), "Cooked Trout");
        add(ModItems.CATFISH.get(), "Raw Catfish");
        add(ModItems.COOKED_CATFISH.get(), "Cooked Catfish");
        add(ModItems.MACKEREL.get(), "Raw Mackerel");
        add(ModItems.COOKED_MACKEREL.get(), "Cooked Mackerel");
        add(ModItems.CRAB.get(), "Raw Crab");
        add(ModItems.COOKED_CRAB.get(), "Cooked Crab");
        add(ModItems.LOBSTER.get(), "Raw Lobster");
        add(ModItems.COOKED_LOBSTER.get(), "Cooked Lobster");
        add(ModItems.CRAYFISH.get(), "Raw Crayfish");
        add(ModItems.COOKED_CRAYFISH.get(), "Cooked Crayfish");
        add(ModItems.RAINBOW_FISH.get(), "Rainbow Fish");

        add("container.fishtrap.fish_trap", "Fish Trap");
        add("creativetab.fishtrap", "Fish Trap");
        add("gui.fishtrap.fish_trap.not_submerged", "Not underwater");
        add("gui.fishtrap.fish_trap.no_bait", "Add bait");

        add("tooltip.fishtrap.fish_trap", "Place fully underwater, add bait and optionally a net");
        add("tooltip.fishtrap.bait", "Fish Trap Bait");
        add("tooltip.fishtrap.bait.time", "Catch time: %s–%s s");
        add("tooltip.fishtrap.bait.luck", "Luck: %s");
        add("tooltip.fishtrap.net", "Fish Trap Net");
        add("tooltip.fishtrap.net.faster", "Catches %s faster");
        add("tooltip.fishtrap.net.slower", "Catches %s slower");
        add("tooltip.fishtrap.net.speed_normal", "Normal catch speed");
        add("tooltip.fishtrap.net.bonus", "Bonus catch chance: %s");
        add("tooltip.fishtrap.rainbow_fish", "Absorption V and Resistance II when eaten");

        add("catch_table.fishtrap.default", "Anywhere Else");
        add("catch_table.fishtrap.ocean", "Ocean");
        add("catch_table.fishtrap.warm_ocean", "Warm Ocean");
        add("catch_table.fishtrap.cold_ocean", "Cold Ocean");
        add("catch_table.fishtrap.river", "River");
        add("catch_table.fishtrap.swamp", "Swamp");

        add("jei.fishtrap.catches", "Fish Trap Catches");
        add("jei.fishtrap.bait", "Fish Trap Bait");
        add("jei.fishtrap.net", "Fish Trap Nets");
        add("jei.fishtrap.weight", "Weight %s, quality %s");
        add("jei.fishtrap.requires", "Needs %s in the trap");
        add("jei.fishtrap.chance", "Chance: %s");
        add("jei.fishtrap.chance_by_bait", "Chance with each bait:");
        add("jei.fishtrap.chance_by_bait_with_net", "Chance with each bait and that net:");
        add("jei.fishtrap.bait_chance", "%s: %s");

        add("jade.fishtrap.bait", "Bait: %s × %s");
        add("jade.fishtrap.net", "Net: %s");
        add("jade.fishtrap.progress", "Next catch: %s%%");
        add("config.jade.plugin_fishtrap.fish_trap", "Fish Trap");

        advancement("root", "Fish Trap", "Craft a fish trap. It catches fish for you while it sits underwater");
        advancement("first_catch", "Something's Biting", "Take a catch out of a fish trap");
        advancement("worm_bait", "Can of Worms", "Make worm bait");
        advancement("fish_chum", "Chumming the Waters", "Turn some of your catch into fish chum");
        advancement("glow_bait", "Bright Idea", "Make glow bait");
        advancement("prismarine_lure", "Monumental Lure", "Craft a prismarine lure");
        advancement("nautilus_lure", "Lure of the Deep", "Craft a nautilus lure, the best bait there is");
        advancement("copper_net", "Net Gains", "Craft a copper net to speed up your traps");
        advancement("netherite_net", "Net Worth", "Upgrade a diamond net to netherite");
        advancement("fresh_catch", "Fresh Catch", "Catch a trout, a catfish and a mackerel with fish traps");
        advancement("shell_shocked", "Shell Shocked", "Catch a crab, a lobster and a crayfish with fish traps");
        advancement("sunken_treasure", "Sunken Treasure", "Pull a Heart of the Sea out of a fish trap");
        advancement("rainbow_fish", "End of the Rainbow", "Catch a Rainbow Fish with a netherite net");
    }

    private void advancement(String name, String title, String description) {
        add("advancements.fishtrap." + name + ".title", title);
        add("advancements.fishtrap." + name + ".description", description);
    }
}
