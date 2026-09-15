package com.coolerpromc.fishtrap.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.ContextAwarePredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

/** {@code fishtrap:fish_trap_catch}: a player takes an item out of a fish trap's catch slots. */
public class FishTrapCatchTrigger extends SimpleCriterionTrigger<FishTrapCatchTrigger.TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, ItemStack caught) {
        this.trigger(player, instance -> instance.matches(caught));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ItemPredicate> item) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                ItemPredicate.CODEC.optionalFieldOf("item").forGetter(TriggerInstance::item)
        ).apply(instance, TriggerInstance::new));

        public static Criterion<TriggerInstance> caughtAnything() {
            return ModCriteriaTriggers.FISH_TRAP_CATCH.get().createCriterion(new TriggerInstance(Optional.empty(), Optional.empty()));
        }

        public static Criterion<TriggerInstance> caught(ItemPredicate item) {
            return ModCriteriaTriggers.FISH_TRAP_CATCH.get().createCriterion(new TriggerInstance(Optional.empty(), Optional.of(item)));
        }

        public boolean matches(ItemStack caught) {
            return this.item.isEmpty() || this.item.get().test(caught);
        }
    }
}
