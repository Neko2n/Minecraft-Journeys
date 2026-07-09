package com.nekotune.minecraftjourneys.shared.registry.content;

import java.util.function.Function;
import java.util.function.Supplier;

import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.core.RegistryHandler.Register;
import com.nekotune.minecraftjourneys.shared.definition.item.gear.KnifeItem;
import com.nekotune.minecraftjourneys.shared.definition.item.gear.MultitoolItem;
import com.nekotune.minecraftjourneys.shared.definition.item.gear.SpearItem;
import com.nekotune.minecraftjourneys.shared.registry.misc.MJArmorMaterials;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = MinecraftJourneys.MOD_ID)
public class MJItems {
    /**
     * Deferred register for all items.
     */
    @Register
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MinecraftJourneys.MOD_ID);
    
    /**
     * Gear items (armor, tools, etc.)
     */
    public static final class Equipment {

        /**
         * Cloth armor
         */
        public static final DeferredItem<ArmorItem> CLOTH_HELMET = new DeferredItem.Builder<ArmorItem>(
                "cloth_helmet",
                properties -> new ArmorItem(MJArmorMaterials.CLOTH, ArmorItem.Type.HELMET, properties
                        .durability(ArmorItem.Type.HELMET.getDurability(3))
                        .rarity(Rarity.COMMON)
                )).register();
        public static final DeferredItem<ArmorItem> CLOTH_CHESTPLATE = new DeferredItem.Builder<ArmorItem>(
                "cloth_chestplate",
                properties -> new ArmorItem(MJArmorMaterials.CLOTH, ArmorItem.Type.CHESTPLATE, properties
                        .durability(ArmorItem.Type.CHESTPLATE.getDurability(3))
                        .rarity(Rarity.COMMON)
                )).register();
        public static final DeferredItem<ArmorItem> CLOTH_LEGGINGS = new DeferredItem.Builder<ArmorItem>(
                "cloth_leggings",
                properties -> new ArmorItem(MJArmorMaterials.CLOTH, ArmorItem.Type.LEGGINGS, properties
                        .durability(ArmorItem.Type.LEGGINGS.getDurability(3))
                        .rarity(Rarity.COMMON)
                )).register();
        public static final DeferredItem<ArmorItem> CLOTH_BOOTS = new DeferredItem.Builder<ArmorItem>(
                "cloth_boots",
                properties -> new ArmorItem(MJArmorMaterials.CLOTH, ArmorItem.Type.BOOTS, properties
                        .durability(ArmorItem.Type.BOOTS.getDurability(3))
                        .rarity(Rarity.COMMON)
                )).register();
        
        /**
         * Mattocks (early-game multi-tool)
         */
        private static DeferredItem<MultitoolItem> basicMattock(String name) {
            return new DeferredItem.Builder<MultitoolItem>(name, properties ->
                    new MultitoolItem(Tiers.STONE, 2, -2.8F, properties
                            .durability(128)
                            .rarity(Rarity.COMMON))
                    ).register();
        }
        public static final DeferredItem<MultitoolItem> STONE_MATTOCK = basicMattock("stone_mattock");
        public static final DeferredItem<MultitoolItem> FLINT_MATTOCK = basicMattock("flint_mattock");
        public static final DeferredItem<MultitoolItem> BONE_MATTOCK = basicMattock("bone_mattock");
        public static final DeferredItem<MultitoolItem> OBSIDIAN_MATTOCK = new DeferredItem.Builder<MultitoolItem>(
                "obsidian_mattock",
                properties -> new MultitoolItem(Tiers.IRON, 3, -2.8F, properties
                        .durability(2000)
                        .rarity(Rarity.UNCOMMON)))
                .register();
        
        /**
         * Spears (early-game throwing weapon)
         */
        private static DeferredItem<SpearItem> basicSpear(String name, Tier tier) {
            return new DeferredItem.Builder<SpearItem>(name, properties ->
                    new SpearItem(tier, 4, -3.2F, properties
                            .durability(128)
                            .rarity(Rarity.COMMON))
                    ).register();
        }
        public static final DeferredItem<SpearItem> STONE_SPEAR = basicSpear("stone_spear", Tiers.STONE);
        public static final DeferredItem<SpearItem> FLINT_SPEAR = basicSpear("flint_spear", Tiers.STONE);
        public static final DeferredItem<SpearItem> BONE_SPEAR = basicSpear("bone_spear", Tiers.STONE);
        public static final DeferredItem<SpearItem> WOODEN_SPEAR = new DeferredItem.Builder<SpearItem>(
                    "wooden_spear",
                    properties -> new SpearItem(Tiers.WOOD, 3, -3.2F, properties
                            .durability(32)
                            .rarity(Rarity.COMMON))
                ).register();
        public static final DeferredItem<SpearItem> OBSIDIAN_SPEAR = new DeferredItem.Builder<SpearItem>(
                    "obsidian_spear",
                    properties -> new SpearItem(Tiers.IRON, 5, -3.2F, properties
                            .durability(2000)
                            .rarity(Rarity.UNCOMMON))
                ).register();
        
        /**
         * Knives (early-game tool & weapon)
         */
        private static DeferredItem<KnifeItem> basicKnife(String name, Tier tier) {
            return new DeferredItem.Builder<KnifeItem>(name, properties ->
                    new KnifeItem(tier, 2, -2.2F, properties
                            .durability(128)
                            .rarity(Rarity.COMMON))
                ).register();
        }
        public static final DeferredItem<KnifeItem> STONE_KNIFE = basicKnife("stone_knife", Tiers.STONE);
        public static final DeferredItem<KnifeItem> FLINT_KNIFE = basicKnife("flint_knife", Tiers.STONE);
        public static final DeferredItem<KnifeItem> BONE_KNIFE = basicKnife("bone_knife", Tiers.STONE);
        public static final DeferredItem<KnifeItem> OBSIDIAN_KNIFE = new DeferredItem.Builder<KnifeItem>(
                "obsidian_knife",
                properties -> new KnifeItem(Tiers.IRON, 3, -2.2F, properties
                        .durability(2000)
                        .rarity(Rarity.UNCOMMON))
                ).register();
    }

    /**
     * Defines items' runtime functionality.
     */
    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {}

    /**
     * The interface type for registered items.
     */
    public static class DeferredItem<T extends Item> implements Supplier<T> {
        private final Supplier<T> ITEM;

        protected DeferredItem(Builder<T> builder) {
            this.ITEM = builder.getItem;
        }

        /**
         * Returns the Item instance of the registered item.
         * @return The Item instance.
         */
        public T get() {
            return this.ITEM.get();
        }

        /**
         * Class to build custom items.
         */
        public static class Builder<T extends Item> {
            private final String NAME;
            private final Supplier<T> ITEM;

            protected Supplier<T> getItem;

            /**
             * Creates the definition for a new item type to be registered.
             * 
             * @param name     The name ID of the item to register.
             * @param provider The supplier to create an instance of the item class from a
             *                 given set of item properties.
             */
            public Builder(String name, Function<Item.Properties, T> provider) {
                this.NAME = name;
                this.ITEM = ITEMS.registerItem(this.NAME, provider);
                this.getItem = this.ITEM;
            }

            /**
             * Registers the item with the deferred register.
             * 
             * @return A supplier of the registered item.
             */
            public DeferredItem<T> register() {
                return new DeferredItem<>(this);
            }
        }
    }
}
