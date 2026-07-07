package com.nekotune.minecraftjourneys.shared.registry;

import com.nekotune.minecraftjourneys.shared.registry.content.MJBlocks;
import com.nekotune.minecraftjourneys.shared.registry.content.MJItems;

import net.hecco.bountifulfares.registry.misc.BFItemGroups;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.CreativeModeTabRegistry;
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
            // event.accept(MJBlocks.BFPearBlocks.FLOWERING_PEAR_LEAVES.get().asItem());
        }
        else if (tab == getTab(CreativeModeTabs.TOOLS_AND_UTILITIES)) {
            event.accept(MJItems.Equipment.STONE_MATTOCK.get());
            event.accept(MJItems.Equipment.BONE_MATTOCK.get());
            event.accept(MJItems.Equipment.FLINT_MATTOCK.get());
            event.accept(MJItems.Equipment.OBSIDIAN_MATTOCK.get());
        }
        else if (tab == getTab(CreativeModeTabs.COMBAT)) {
            event.accept(MJItems.Equipment.CLOTH_HELMET.get());
            event.accept(MJItems.Equipment.CLOTH_CHESTPLATE.get());
            event.accept(MJItems.Equipment.CLOTH_LEGGINGS.get());
            event.accept(MJItems.Equipment.CLOTH_BOOTS.get());
        }
    }

    private static CreativeModeTab getTab(ResourceKey<CreativeModeTab> key) {
        return CreativeModeTabRegistry.getTab(key.location());
    }
}
