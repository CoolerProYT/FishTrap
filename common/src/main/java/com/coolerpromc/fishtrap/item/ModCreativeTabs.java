package com.coolerpromc.fishtrap.item;

import com.coolerpromc.fishtrap.platform.Services;
import com.coolerpromc.fishtrap.platform.util.RegistryHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public class ModCreativeTabs {
    public static final RegistryHandler<CreativeModeTab, CreativeModeTab> FISH_TRAP_TAB = Services.REGISTRY.registerCreativeTab("fish_trap", ModItems.FISH_TRAP::toStack, Component.translatable("creativetab.fishtrap"),
            (output, parameters) -> {
                output.accept(ModItems.FISH_TRAP);
                for (RegistryHandler.Items<Item> net : ModItems.NETS) {
                    output.accept(net);
                }
                for (RegistryHandler.Items<Item> bait : ModItems.BAITS) {
                    output.accept(bait);
                }
                for (int i = 0; i < ModItems.RAW_CATCHES.size(); i++) {
                    output.accept(ModItems.RAW_CATCHES.get(i));
                    output.accept(ModItems.COOKED_CATCHES.get(i));
                }
                output.accept(ModItems.RAINBOW_FISH);
            });

    public static void load() {
    }
}
