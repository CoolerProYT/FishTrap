package com.coolerpromc.fishtrap.advancement;

import com.coolerpromc.fishtrap.platform.Services;
import com.coolerpromc.fishtrap.platform.util.RegistryHandler;
import net.minecraft.advancements.triggers.CriterionTrigger;

public class ModCriteriaTriggers {
    public static final RegistryHandler<CriterionTrigger<?>, FishTrapCatchTrigger> FISH_TRAP_CATCH = Services.REGISTRY.registerCriterionTrigger("fish_trap_catch", FishTrapCatchTrigger::new);

    public static void load() {
    }
}
