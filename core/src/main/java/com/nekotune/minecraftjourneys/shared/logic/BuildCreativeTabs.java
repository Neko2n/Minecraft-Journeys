package com.nekotune.minecraftjourneys.shared.logic;

import com.nekotune.minecraftjourneys.shared.registries.content.MJItems;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.CreativeModeTabRegistry;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@EventBusSubscriber
public final class BuildCreativeTabs {
    
    @SubscribeEvent
    public static void onBuildCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        CreativeModeTab tab = event.getTab();
        if (tab == getTab(CreativeModeTabs.TOOLS_AND_UTILITIES)) {
            event.accept(MJItems.STONE_MATTOCK.get());
            event.accept(MJItems.BONE_MATTOCK.get());
            event.accept(MJItems.FLINT_MATTOCK.get());
            event.accept(MJItems.OBSIDIAN_MATTOCK.get());
        }
        else if (tab == getTab(CreativeModeTabs.COMBAT)) {
            event.accept(MJItems.CLOTH_HELMET.get());
            event.accept(MJItems.CLOTH_CHESTPLATE.get());
            event.accept(MJItems.CLOTH_LEGGINGS.get());
            event.accept(MJItems.CLOTH_BOOTS.get());
            event.accept(MJItems.STONE_KNIFE.get());
            event.accept(MJItems.FLINT_KNIFE.get());
            event.accept(MJItems.BONE_KNIFE.get());
            event.accept(MJItems.OBSIDIAN_KNIFE.get());
            event.accept(MJItems.WOODEN_SPEAR.get());
            event.accept(MJItems.INCENDIARY_SPEAR.get());
            event.accept(MJItems.STONE_SPEAR.get());
            event.accept(MJItems.FLINT_SPEAR.get());
            event.accept(MJItems.BONE_SPEAR.get());
            event.accept(MJItems.OBSIDIAN_SPEAR.get());
        }
        else if (tab == getTab(CreativeModeTabs.INGREDIENTS)) {
            event.accept(MJItems.GRASS.get());
        }
    }

    private static CreativeModeTab getTab(ResourceKey<CreativeModeTab> key) {
        return CreativeModeTabRegistry.getTab(key.location());
    }
}
