package com.nekotune.minecraftjourneys.data.datagen;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.core.DataGenerator.DataGenProvider;
import com.nekotune.minecraftjourneys.data.datagen.util.bountifulfares.BFDataGenUtility;
import com.nekotune.minecraftjourneys.shared.registry.content.MJBlocks;

import net.hecco.bountifulfares.definition.block.custom.FruitLeavesBlock;
import net.hecco.bountifulfares.definition.block.custom.FruitLogBlock;
import net.hecco.bountifulfares.definition.block.custom.StrippedFruitLogBlock;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@DataGenProvider(dist = Dist.CLIENT)
public class MJBlockStateProvider extends BlockStateProvider {

    /**
     * Keeps track of which blocks have already been given an explicit
     * block state.
     * Populated automatically by {@link #getVariantBuilder} and
     * {@link #getMultipartBuilder}.
     */
    private final Set<Block> registered = new HashSet<>();

    // Templates for block state datagen
    private static final class ResourceTemplates {
        public static final ResourceLocation LEAVES = ResourceLocation.withDefaultNamespace("block/leaves");
        public static final ResourceLocation FLOWER_POT_CROSS = ResourceLocation.withDefaultNamespace("block/flower_pot_cross");
    }

    public MJBlockStateProvider(GatherDataEvent event) {
        super(event.getGenerator().getPackOutput(),
                MinecraftJourneys.MOD_ID,
                event.getExistingFileHelper());
    }

    @Override
    public VariantBlockStateBuilder getVariantBuilder(Block block) {
        registered.add(block);
        return super.getVariantBuilder(block);
    }

    @Override
    public MultiPartBlockStateBuilder getMultipartBuilder(Block block) {
        registered.add(block);
        return super.getMultipartBuilder(block);
    }

    @Override
    protected void registerStatesAndModels() {

        // Blocks which use static JSON / don't use datagen
        registered.add(MJBlocks.BFPearBlocks.PEAR_BLOCK.get());
        registered.add(MJBlocks.BFPearBlocks.HANGING_PEAR.get());

        // Assign generic block states by block type
        {
            final Collection<Block> blocks = MJBlocks.BLOCKS.getEntries()
                    .stream().map(Holder::value).toList();

            // First, assign dependencies; blocks whose states/models are required by other blocks
            blocks.forEach(block -> {

                // Fruit logs depend on the models of their leaves
                if (block instanceof FruitLeavesBlock) register(block);
            });
            blocks.forEach(block -> {

                // Fruit wood blocks reuse their sibling fruit log's axis-cap models
                if ((block instanceof FruitLogBlock || block instanceof StrippedFruitLogBlock)
                        && !BuiltInRegistries.BLOCK.getKey(block).getPath().endsWith("_wood")) {
                    register(block);
                }
            });

            // Finally, assign dependents and independents.
            blocks.forEach(this::register);
        }
    }

    /**
     * Helper to register the state and model for a single block.
     * @param block The block to register a state and model for.
     */
    private void register(Block block) {
        if (registered.contains(block)) return;
        final String blockName = BuiltInRegistries.BLOCK.getKey(block).getPath();
        final ModelFile model;

        // Bountiful Fares fruit logs
        if (block instanceof FruitLogBlock || block instanceof StrippedFruitLogBlock) {
            model = BFDataGenUtility.BlockStateProviderHelper.fruitLog(this, block);

        // Rotated pillar blocks; logs, columns, etc.
        } else if (block instanceof RotatedPillarBlock pillarBlock) {
            final ResourceLocation base = blockTexture(block);
            final ResourceLocation side = ResourceLocation.fromNamespaceAndPath(base.getNamespace(),
                    base.getPath() + "_side");
            final ResourceLocation end = ResourceLocation.fromNamespaceAndPath(base.getNamespace(),
                    base.getPath() + "_end");
            model = models().cubeColumn(blockName, side, end);
            axisBlock(pillarBlock, model, models().cubeColumnHorizontal(blockName + "_horizontal", side, end));

        // Directional blocks; rods, pistons, etc.
        } else if (block instanceof DirectionalBlock) {
            model = models().cubeAll(blockName, blockTexture(block));
            directionalBlock(block, model);
        
        // Simple blocks
        } else {
            // Bush blocks; flowers, saplings, bushes, etc.
            if (block instanceof BushBlock) {
                model = models().cross(blockName,
                        blockTexture(block)).renderType("cutout");
        
            // Bountiful Fares fruit leaves
            } else if (block instanceof FruitLeavesBlock) {
                model = BFDataGenUtility.BlockStateProviderHelper.fruitLeaves(this, block);

            // Leaves blocks; transparent, color-mapped textures.
            } else if (block instanceof LeavesBlock) {
                model = models().leaves(blockName, ResourceTemplates.LEAVES);

            // Flower pot blocks; potted plants, textured with the plant's own texture.
            } else if (block instanceof FlowerPotBlock potBlock) {
                model = models().withExistingParent(blockName, ResourceTemplates.FLOWER_POT_CROSS)
                        .texture("plant", blockTexture(potBlock.getPotted()))
                        .renderType("cutout");

            // All other blocks; flat, single textures.
            } else {
                model = cubeAll(block);
            }

            simpleBlock(block, model);
        }

        // If the block has an item, generate a model for it.
        final int itemId = Item.getId(block.asItem());
        final boolean hasItem = !(itemId == Item.getId(Blocks.AIR.asItem()));
        if (hasItem) {
            final ModelFile itemModel;
            if (block instanceof BushBlock) {
                itemModel = models().withExistingParent(blockName + "_item",
                        ResourceLocation.withDefaultNamespace("item/generated"))
                        .texture("layer0", blockTexture(block));
            } else {
                itemModel = model;
            }
            simpleBlockItem(block, itemModel);
        }
    }
}
