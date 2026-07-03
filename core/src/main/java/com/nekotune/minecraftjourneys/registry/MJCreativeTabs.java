package com.nekotune.minecraftjourneys.registry;

import com.nekotune.minecraftjourneys.registry.content.MJBlocks;

import net.hecco.bountifulfares.registry.misc.BFItemGroups;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@EventBusSubscriber
public class MJCreativeTabs {
    
    @SubscribeEvent
    public static void onBuildCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        CreativeModeTab tab = event.getTab();
        if (tab == BFItemGroups.COMPATIBILITY.get()) {
            event.accept(MJBlocks.BFPearBlocks.PEAR_BLOCK.get().asItem());
            event.accept(MJBlocks.BFPearBlocks.PEAR_LOG.get().asItem());
            event.accept(MJBlocks.BFPearBlocks.PEAR_WOOD.get().asItem());
            event.accept(MJBlocks.BFPearBlocks.STRIPPED_PEAR_LOG.get().asItem());
            event.accept(MJBlocks.BFPearBlocks.STRIPPED_PEAR_WOOD.get().asItem());
            event.accept(MJBlocks.BFPearBlocks.PEAR_SAPLING.get().asItem());
            event.accept(MJBlocks.BFPearBlocks.PEAR_LEAVES.get().asItem());
            event.accept(MJBlocks.BFPearBlocks.FLOWERING_PEAR_LEAVES.get().asItem());
        }
    }
}
