package com.nekotune.minecraftjourneys.shared.registries.content;

import java.util.function.Function;
import java.util.function.Supplier;

import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.data.worldgen.MJTreeGrowers;
import com.nekotune.minecraftjourneys.shared.definitions.block.pear.HangingPearBlock;
import com.nekotune.minecraftjourneys.shared.definitions.block.pear.PearBlock;

import net.hecco.bountifulfares.definition.block.custom.FruitLeavesBlock;
import net.hecco.bountifulfares.definition.block.custom.FruitLogBlock;
import net.hecco.bountifulfares.definition.block.custom.StrippedFruitLogBlock;
import net.hecco.bountifulfares.registry.content.BFBlocks;
import net.hecco.nexuslib.lib.loader_agnostic.toolAction.NLToolActions;
import net.hecco.nexuslib.lib.publicBlocks.PublicSaplingBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = MinecraftJourneys.MOD_ID)
public class MJBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MinecraftJourneys.MOD_ID);

    public static final DeferredBlock<PearBlock> PEAR_BLOCK;
    public static final DeferredBlock<FruitLogBlock> PEAR_LOG;
    public static final DeferredBlock<FruitLogBlock> PEAR_WOOD;
    public static final DeferredBlock<StrippedFruitLogBlock> STRIPPED_PEAR_LOG;
    public static final DeferredBlock<StrippedFruitLogBlock> STRIPPED_PEAR_WOOD;
    public static final DeferredBlock<HangingPearBlock> HANGING_PEAR;
    public static final DeferredBlock<PublicSaplingBlock> PEAR_SAPLING;
    public static final DeferredBlock<FlowerPotBlock> POTTED_PEAR_SAPLING;
    public static final DeferredBlock<FruitLeavesBlock> PEAR_LEAVES;
    // public static final DeferredBlock<FruitLeavesBlock> FLOWERING_PEAR_LEAVES;

    static {
        PEAR_BLOCK = new DeferredBlock.Builder<>(
                "pear_block",
                (properties) -> new PearBlock(
                        Properties.ofFullCopy(BFBlocks.APPLE_BLOCK.get())
                                .mapColor(MapColor.COLOR_LIGHT_GREEN)))
                .register();
        PEAR_LOG = new DeferredBlock.Builder<>(
                "pear_log",
                (properties) -> new FruitLogBlock(
                        Properties.ofFullCopy(BFBlocks.APPLE_LOG.get())
                                .noOcclusion()))
                .register();
        PEAR_WOOD = new DeferredBlock.Builder<>(
                "pear_wood",
                (properties) -> new FruitLogBlock(
                        Properties.ofFullCopy(BFBlocks.APPLE_WOOD.get())
                                .noOcclusion()))
                .register();
        STRIPPED_PEAR_LOG = new DeferredBlock.Builder<>(
                "stripped_pear_log",
                (properties) -> new StrippedFruitLogBlock(
                        Properties.ofFullCopy(BFBlocks.STRIPPED_APPLE_LOG.get())
                                .noOcclusion()))
                .register();
        STRIPPED_PEAR_WOOD = new DeferredBlock.Builder<>(
                "stripped_pear_wood",
                (properties) -> new StrippedFruitLogBlock(
                        Properties.ofFullCopy(BFBlocks.STRIPPED_APPLE_WOOD.get())
                                .noOcclusion()))
                .register();
        HANGING_PEAR = new DeferredBlock.Builder<>(
                "hanging_pear",
                (properties) -> new HangingPearBlock(
                        Properties.ofFullCopy(BFBlocks.HANGING_APPLE.get())
                                .mapColor(MapColor.COLOR_LIGHT_GREEN)))
                .noItem()
                .register();
        PEAR_SAPLING = new DeferredBlock.Builder<>(
                "pear_sapling",
                (properties) -> new PublicSaplingBlock(MJTreeGrowers.PEAR_SAPLING_GENERATOR,
                        Properties.ofFullCopy(BFBlocks.APPLE_SAPLING.get())))
                .register();
        POTTED_PEAR_SAPLING = new DeferredBlock.Builder<>(
                "potted_pear_sapling",
                (properties) -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT,
                        PEAR_SAPLING,
                        Properties.ofFullCopy(BFBlocks.POTTED_APPLE_SAPLING.get())))
                .noItem()
                .register();
        PEAR_LEAVES = new DeferredBlock.Builder<>(
                "pear_leaves",
                (properties) -> new FruitLeavesBlock(HANGING_PEAR.get().defaultBlockState(),
                        Properties.ofFullCopy(BFBlocks.APPLE_LEAVES.get())))
                .register();
        // FLOWERING_PEAR_LEAVES = new DeferredBlock.Builder<>(
        // "flowering_pear_leaves",
        // (properties) -> new FruitLeavesBlock(HANGING_PEAR.get().defaultBlockState(),
        // Properties.ofFullCopy(BFBlocks.FLOWERING_APPLE_LEAVES.get())))
        // .register();
    }

    /**
     * Defines blocks' runtime functionality.
     */
    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {

        // Make logs strippable
        NLToolActions.addStrippable(MJBlocks.PEAR_LOG.get(),
                MJBlocks.STRIPPED_PEAR_LOG.get());
        NLToolActions.addStrippable(MJBlocks.PEAR_WOOD.get(),
                MJBlocks.STRIPPED_PEAR_WOOD.get());

        // Pottable plants
        ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(
                BuiltInRegistries.BLOCK.getKey(MJBlocks.PEAR_SAPLING.get()),
                MJBlocks.POTTED_PEAR_SAPLING);
    }

    /**
     * The interface type for registered blocks.
     */
    public static class DeferredBlock<T extends Block> implements Supplier<T> {
        private final Supplier<T> BLOCK;

        protected DeferredBlock(Builder<T> builder) {
            this.BLOCK = builder.getBlock;
        }

        /**
         * Returns the Block instance of the registered block.
         * 
         * @return The Block instance.
         */
        public T get() {
            return this.BLOCK.get();
        }

        /**
         * Returns the Item instance of the registered block,
         * or AIR if the block has no item.
         * 
         * @return The Item instance.
         */
        public Item asItem() {
            return get().asItem();
        }

        /**
         * Class to build custom blocks.
         */
        public static class Builder<T extends Block> {
            private final String NAME;
            private final Supplier<T> BLOCK;
            private boolean hasItem = true;

            protected Supplier<T> getBlock;

            /**
             * Creates the definition for a new block type to be registered.
             * 
             * @param name     The name ID of the block to register.
             * @param provider The supplier to create an instance of the block class from a
             *                 given set of block properties.
             */
            public Builder(String name, Function<BlockBehaviour.Properties, T> provider) {
                this.NAME = name;
                this.BLOCK = BLOCKS.registerBlock(this.NAME, provider);
                this.getBlock = this.BLOCK;
            }

            /**
             * The block does not have a corresponding block item to be registered.
             * 
             * @return The current block builder instance.
             */
            public Builder<T> noItem() {
                this.hasItem = false;
                return this;
            }

            /**
             * Registers the block with the deferred register.
             * 
             * @return A supplier of the registered block.
             */
            public DeferredBlock<T> register() {
                if (this.hasItem) {
                    MJItems.ITEMS.registerSimpleBlockItem(this.NAME, this.getBlock);
                }
                return new DeferredBlock<>(this);
            }
        }
    }
}
