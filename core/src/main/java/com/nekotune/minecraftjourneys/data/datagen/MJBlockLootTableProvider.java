package com.nekotune.minecraftjourneys.data.datagen;

import java.util.Collections;
import java.util.List;

import com.farcr.nomansland.common.registry.items.NMLItems;
import com.nekotune.minecraftjourneys.core.DataGenerator.DataGenProvider;
import com.nekotune.minecraftjourneys.shared.definition.block.pear.HangingPearBlock;
import com.nekotune.minecraftjourneys.shared.definition.block.pear.PearBlock;
import com.nekotune.minecraftjourneys.shared.registry.content.MJBlocks;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.data.event.GatherDataEvent;

/**
 * Provides block loot table datagen definitions to generate.
 */
@DataGenProvider(dist = Dist.DEDICATED_SERVER)
public class MJBlockLootTableProvider extends LootTableProvider {

    public MJBlockLootTableProvider(GatherDataEvent event) {
        super(event.getGenerator().getPackOutput(), Collections.emptySet(),
                List.of(new SubProviderEntry(SubProvider::new,
                        LootContextParamSets.BLOCK)),
                event.getLookupProvider());
    }

    public static class SubProvider extends BlockLootSubProvider {

        protected SubProvider(HolderLookup.Provider registries) {
            super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), registries);
        }

        @Override
        protected void generate() {

            // Pear fruit block copies the drop table structure of other BF fruit blocks
            final Block PEAR_BLOCK = MJBlocks.BFPearBlocks.PEAR_BLOCK.get();
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
            final Block HANGING_PEAR = MJBlocks.BFPearBlocks.HANGING_PEAR.get();
            this.add(HANGING_PEAR, LootTable.lootTable()
                    .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                            .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(HANGING_PEAR)
                                    .setProperties(StatePropertiesPredicate.Builder.properties()
                                            .hasProperty(HangingPearBlock.AGE, 4)))
                            .add(this.applyExplosionDecay(HANGING_PEAR,
                                    LootItem.lootTableItem(NMLItems.PEAR.get())))));
            
            // Pear leaves act like other fruit tree leaves
            final Block PEAR_LEAVES = MJBlocks.BFPearBlocks.PEAR_LEAVES.get();
            this.add(PEAR_LEAVES, this.createLeavesDrops(PEAR_LEAVES,
                    MJBlocks.BFPearBlocks.PEAR_SAPLING.get(),
                    0.05f, 0.0625f));

            // Any block that didn't get an explicit loot table
            // is given a generic loot table.
            // For most blocks, this means dropping itself.
            this.getKnownBlocks().forEach(block -> {
                ResourceKey<LootTable> lootTable = block.getLootTable();
                if (this.map.containsKey(lootTable)) return;

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
}
