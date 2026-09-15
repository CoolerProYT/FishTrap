package com.coolerpromc.fishtrap.screen;

import com.coolerpromc.fishtrap.platform.Services;
import com.coolerpromc.fishtrap.platform.util.RegistryHandler;
import net.minecraft.world.inventory.MenuType;

public class ModMenuTypes {
    public static final RegistryHandler<MenuType<?>, MenuType<FishTrapMenu>> FISH_TRAP = Services.REGISTRY.registerMenuType("fish_trap", FishTrapMenu::new);

    public static void load() {
    }
}
