package com.coolerpromc.fishtrap.datagen;

import com.coolerpromc.fishtrap.Constants;
import com.coolerpromc.fishtrap.item.ModItems;
import com.coolerpromc.fishtrap.platform.util.RegistryHandler;
import com.coolerpromc.fishtrap.upgrade.NetRegistry;
import com.coolerpromc.fishtrap.upgrade.NetStyle;
import com.coolerpromc.fishtrap.upgrade.NetType;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.JsonCodecProvider;

import java.util.concurrent.CompletableFuture;

public class ModNetProvider extends JsonCodecProvider<NetType> {
    public ModNetProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, PackOutput.Target.DATA_PACK, NetRegistry.DIRECTORY, NetType.CODEC, lookupProvider, Constants.MODID);
    }

    @Override
    protected void gather() {
        // Each tier waits less, adds luck on top of the bait and may catch twice. Only the netherite net reaches the Rainbow Fish (see loot tables).
        net(ModItems.COPPER_NET, NetStyle.COPPER, 0.9F, 0.5F, 0.0F);
        net(ModItems.IRON_NET, NetStyle.IRON, 0.8F, 1.0F, 0.1F);
        net(ModItems.GOLD_NET, NetStyle.GOLD, 0.8F, 2.0F, 0.15F);
        net(ModItems.DIAMOND_NET, NetStyle.DIAMOND, 0.65F, 2.0F, 0.25F);
        net(ModItems.NETHERITE_NET, NetStyle.NETHERITE, 0.5F, 3.0F, 0.4F);
    }

    private void net(RegistryHandler.Items<Item> item, NetStyle style, float catchTimeMultiplier, float luck, float bonusCatchChance) {
        unconditional(item.id(), new NetType(item.get(), style, catchTimeMultiplier, luck, bonusCatchChance));
    }

    @Override
    public String getName() {
        return "Fish Trap nets";
    }
}
