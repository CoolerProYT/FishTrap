package com.coolerpromc.fishtrap.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Optional;

public class FishTrapCatchTrigger extends SimpleCriterionTrigger<FishTrapCatchTrigger.TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, ItemStack caught) {
        this.trigger(player, instance -> instance.matches(caught));
    }

    public record TriggerInstance(Optional<Holder<LootItemCondition>> player, Optional<ItemPredicate> item) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                LootItemCondition.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
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
