package com.nekotune.minecraftjourneys.data.datagen.maps;

import com.nekotune.minecraftjourneys.data.tags.MJItemTags;
import com.nekotune.minecraftjourneys.shared.registries.content.MJItems;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

public final class MJFurnaceFuelsSubProvider extends MJDataMapProvider.SubProvider<Item, FurnaceFuel> {

	@Override
	protected void define(MJDataMapProvider.DataMap<Item, FurnaceFuel> map) {
        map.add(MJItems.GRASS.get(), new FurnaceFuel(50), false);
        map.add(MJItemTags.Armors.CLOTH, new FurnaceFuel(200), false);
        map.add(MJItems.WOODEN_SPEAR.get(), new FurnaceFuel(100), false);
        map.add(MJItems.INCENDIARY_SPEAR.get(), new FurnaceFuel(100), false);
	}

	@Override
	protected DataMapType<Item, FurnaceFuel> mapType() {
		return NeoForgeDataMaps.FURNACE_FUELS;
	}
}
