package com.nekotune.minecraftjourneys.data.datagen;

import com.nekotune.minecraftjourneys.data.datagen.assets.MJBlockStateProvider;
import com.nekotune.minecraftjourneys.data.datagen.assets.MJItemModelProvider;
import com.nekotune.minecraftjourneys.data.datagen.loot.MJLootTableProvider;
import com.nekotune.minecraftjourneys.data.datagen.loot.modifier.MJGlobalLootModifierProvider;
import com.nekotune.minecraftjourneys.data.datagen.maps.MJDataMapProvider;
import com.nekotune.minecraftjourneys.data.datagen.recipes.MJRecipeProvider;
import com.nekotune.minecraftjourneys.data.datagen.tags.MJBiomeTagsProvider;
import com.nekotune.minecraftjourneys.data.datagen.tags.MJBlockTagsProvider;
import com.nekotune.minecraftjourneys.data.datagen.tags.MJEnchantmentTagsProvider;
import com.nekotune.minecraftjourneys.data.datagen.tags.MJEntityTypeTagsProvider;
import com.nekotune.minecraftjourneys.data.datagen.tags.MJItemTagsProvider;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber
public class MJDataGenerator {
    
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        if (!event.includeServer()) return;

        final var datapackEntries = event.addProvider(new MJDatapackEntriesProvider(event));

        // TODO: Group tag providers into sub-providers under one main provider
        final var blockTags = event.addProvider(new MJBlockTagsProvider(event));
        final var itemTags = event.addProvider(new MJItemTagsProvider(event, blockTags));
        final var entityTypeTags = event.addProvider(new MJEntityTypeTagsProvider(event));
        final var enchantmentTags = event.addProvider(new MJEnchantmentTagsProvider(event, datapackEntries));
        final var biomeTags = event.addProvider(new MJBiomeTagsProvider(event));

        // TODO: Split recipes into sub-providers under a main provider
        final var recipes = event.addProvider(new MJRecipeProvider(event));

        final var lootTables = event.addProvider(new MJLootTableProvider(event));
        final var lootModifiers = event.addProvider(new MJGlobalLootModifierProvider(event, datapackEntries));
        
        final var dataMaps = event.addProvider(new MJDataMapProvider(event));
    }

    @SubscribeEvent
    public static void onGatherDataClient(GatherDataEvent event) {
        if (!event.includeClient()) return;
        
        final var blockStates = event.addProvider(new MJBlockStateProvider(event));
        final var itemModels = event.addProvider(new MJItemModelProvider(event));
    }
}
