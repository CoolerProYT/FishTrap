package com.coolerpromc.fishtrap.client;

import com.coolerpromc.fishtrap.bait.BaitRegistry;
import com.coolerpromc.fishtrap.bait.BaitType;
import com.coolerpromc.fishtrap.item.ModItems;
import com.coolerpromc.fishtrap.upgrade.NetRegistry;
import com.coolerpromc.fishtrap.upgrade.NetType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Locale;

public final class FishTrapTooltips {
    private FishTrapTooltips() {
    }

    public static void append(ItemStack stack, List<Component> lines) {
        if (stack.is(ModItems.FISH_TRAP.get())) {
            lines.add(Component.translatable("tooltip.fishtrap.fish_trap").withStyle(ChatFormatting.GRAY));
            return;
        }
        if (stack.is(ModItems.RAINBOW_FISH.get())) {
            lines.add(Component.translatable("tooltip.fishtrap.rainbow_fish").withStyle(ChatFormatting.LIGHT_PURPLE));
        }

        BaitType bait = BaitRegistry.get(stack);
        if (bait != null) {
            lines.add(Component.translatable("tooltip.fishtrap.bait").withStyle(ChatFormatting.DARK_AQUA));
            lines.add(catchTime(bait).withStyle(ChatFormatting.GRAY));
            lines.add(luck(bait.luck()).withStyle(luckColor(bait.luck())));
        }

        NetType net = NetRegistry.get(stack);
        if (net != null) {
            lines.add(Component.translatable("tooltip.fishtrap.net").withStyle(ChatFormatting.DARK_AQUA));
            lines.add(netSpeed(net).withStyle(net.catchTimeMultiplier() < 1.0F ? ChatFormatting.GREEN : ChatFormatting.GRAY));
            lines.add(luck(net.luck()).withStyle(luckColor(net.luck())));
            if (net.bonusCatchChance() > 0.0F) {
                lines.add(netBonus(net).withStyle(ChatFormatting.GREEN));
            }
        }
    }

    public static MutableComponent catchTime(BaitType bait) {
        return Component.translatable("tooltip.fishtrap.bait.time", number(bait.minTicks() / 20.0F), number(bait.maxTicks() / 20.0F));
    }

    public static MutableComponent luck(BaitType bait) {
        return luck(bait.luck());
    }

    public static MutableComponent luck(float luck) {
        String value = number(luck);
        return Component.translatable("tooltip.fishtrap.bait.luck", luck > 0 ? "+" + value : value);
    }

    public static MutableComponent netSpeed(NetType net) {
        float multiplier = net.catchTimeMultiplier();
        if (multiplier == 1.0F) {
            return Component.translatable("tooltip.fishtrap.net.speed_normal");
        }
        return multiplier < 1.0F ? Component.translatable("tooltip.fishtrap.net.faster", percent(1.0F - multiplier)) : Component.translatable("tooltip.fishtrap.net.slower", percent(multiplier - 1.0F));
    }

    public static MutableComponent netBonus(NetType net) {
        return Component.translatable("tooltip.fishtrap.net.bonus", percent(net.bonusCatchChance()));
    }

    public static String percent(double fraction) {
        double value = Math.round(fraction * 1000.0) / 10.0;
        return value == Math.floor(value) ? String.format(Locale.ROOT, "%.0f%%", value) : String.format(Locale.ROOT, "%.1f%%", value);
    }

    private static ChatFormatting luckColor(float luck) {
        return luck > 0 ? ChatFormatting.GREEN : luck < 0 ? ChatFormatting.RED : ChatFormatting.GRAY;
    }

    private static String number(float value) {
        return value == Math.floor(value) ? Integer.toString((int) value) : String.format(Locale.ROOT, "%.1f", value);
    }
}
