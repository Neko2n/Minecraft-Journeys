package com.nekotune.minecraftjourneys.data.datagen.loot;

import java.util.Collections;
import com.nekotune.minecraftjourneys.shared.registries.content.MJBlocks;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.storage.loot.LootTable;

public class MJBlockLootSubProvider extends BlockLootSubProvider {

    protected MJBlockLootSubProvider(HolderLookup.Provider registries) {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {

        // Any block that didn't get an explicit loot table
        // is given a generic loot table.
        // For most blocks, this means dropping itself.
        this.getKnownBlocks().forEach(block -> {
            ResourceKey<LootTable> lootTable = block.getLootTable();
            if (this.map.containsKey(lootTable))
                return;

            // Default case: drops self
            this.dropSelf(block);

            // Flower pots also drop their potted contents
            if (block instanceof FlowerPotBlock) {
                this.dropPottedContents(block);
            }
        });
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return MJBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
