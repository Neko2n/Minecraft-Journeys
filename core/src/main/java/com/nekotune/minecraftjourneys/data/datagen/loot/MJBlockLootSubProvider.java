package com.nekotune.minecraftjourneys.data.datagen.loot;

import java.util.Collections;
import com.farcr.nomansland.common.registry.items.NMLItems;
import com.nekotune.minecraftjourneys.shared.definitions.block.pear.HangingPearBlock;
import com.nekotune.minecraftjourneys.shared.definitions.block.pear.PearBlock;
import com.nekotune.minecraftjourneys.shared.registries.content.MJBlocks;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class MJBlockLootSubProvider extends BlockLootSubProvider {

    protected MJBlockLootSubProvider(HolderLookup.Provider registries) {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {

        // Pear fruit block copies the drop table structure of other BF fruit blocks
        final Block PEAR_BLOCK = MJBlocks.PEAR_BLOCK.get();
        final Item PEAR = NMLItems.PEAR.get();
        this.add(PEAR_BLOCK, LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(PEAR_BLOCK)
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(PearBlock.SLICES, 0)))
                        .add(applyExplosionDecay(PEAR_BLOCK, LootItem.lootTableItem(PEAR_BLOCK))))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(4.0F))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(PEAR_BLOCK)
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(PearBlock.SLICES, 1)))
                        .add(applyExplosionDecay(PEAR_BLOCK, LootItem.lootTableItem(PEAR))))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(2.0F))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(PEAR_BLOCK)
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(PearBlock.SLICES, 2)))
                        .add(applyExplosionDecay(PEAR_BLOCK, LootItem.lootTableItem(PEAR))))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(PEAR_BLOCK)
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(PearBlock.SLICES, 3)))
                        .add(applyExplosionDecay(PEAR_BLOCK, LootItem.lootTableItem(PEAR)))));

        // Hanging pear block drops pear fruit when fully ripe
        final Block HANGING_PEAR = MJBlocks.HANGING_PEAR.get();
        this.add(HANGING_PEAR, LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(HANGING_PEAR)
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(HangingPearBlock.AGE, 4)))
                        .add(this.applyExplosionDecay(HANGING_PEAR,
                                LootItem.lootTableItem(NMLItems.PEAR.get())))));

        // Pear leaves act like other fruit tree leaves
        final Block PEAR_LEAVES = MJBlocks.PEAR_LEAVES.get();
        this.add(PEAR_LEAVES, this.createLeavesDrops(PEAR_LEAVES,
                MJBlocks.PEAR_SAPLING.get(),
                0.05f, 0.0625f));

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
