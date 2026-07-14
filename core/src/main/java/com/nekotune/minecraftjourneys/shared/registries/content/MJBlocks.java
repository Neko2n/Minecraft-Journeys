package com.nekotune.minecraftjourneys.shared.registries.content;

import java.util.function.Function;
import java.util.function.Supplier;

import com.nekotune.minecraftjourneys.MinecraftJourneys;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = MinecraftJourneys.MOD_ID)
public class MJBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MinecraftJourneys.MOD_ID);

    /**
     * Defines blocks' runtime functionality.
     */
    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {}

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
