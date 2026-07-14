package com.nekotune.minecraftjourneys.data.datagen.loot;

import java.util.stream.Stream;

import com.nekotune.minecraftjourneys.shared.registries.content.MJEntityTypes;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.storage.loot.LootTable;

public class MJEntityLootSubProvider extends EntityLootSubProvider {

    protected MJEntityLootSubProvider(HolderLookup.Provider registries) {
        super(FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    public void generate() {

        // By default, entities drop nothing
        getKnownEntityTypes().forEach(entityType -> {
            if (!canHaveLootTable(entityType)) return;
            add(entityType, LootTable.lootTable());
        });
    }

    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        return MJEntityTypes.ENTITY_TYPES.getEntries().stream().map(Holder::value);
    }
}
