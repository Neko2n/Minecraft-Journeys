package com.nekotune.minecraftjourneys.shared.registries.content;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.shared.definitions.item.armor.MJArmorMaterials;
import com.nekotune.minecraftjourneys.shared.definitions.item.tool.IncendiarySpearItem;
import com.nekotune.minecraftjourneys.shared.definitions.item.tool.KnifeItem;
import com.nekotune.minecraftjourneys.shared.definitions.item.tool.MultitoolItem;
import com.nekotune.minecraftjourneys.shared.definitions.item.tool.SpearItem;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class MJItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MinecraftJourneys.MOD_ID);
    
    public static final DeferredItem<Item> GRASS;
    public static final DeferredItem<ArmorItem> CLOTH_HELMET;
    public static final DeferredItem<ArmorItem> CLOTH_CHESTPLATE;
    public static final DeferredItem<ArmorItem> CLOTH_LEGGINGS;
    public static final DeferredItem<ArmorItem> CLOTH_BOOTS;
    public static final DeferredItem<MultitoolItem> STONE_MATTOCK;
    public static final DeferredItem<MultitoolItem> FLINT_MATTOCK;
    public static final DeferredItem<MultitoolItem> BONE_MATTOCK;
    public static final DeferredItem<MultitoolItem> OBSIDIAN_MATTOCK;
    public static final DeferredItem<SpearItem> WOODEN_SPEAR;
    public static final DeferredItem<IncendiarySpearItem> INCENDIARY_SPEAR;
    public static final DeferredItem<SpearItem> STONE_SPEAR;
    public static final DeferredItem<SpearItem> FLINT_SPEAR;
    public static final DeferredItem<SpearItem> BONE_SPEAR;
    public static final DeferredItem<SpearItem> OBSIDIAN_SPEAR;
    public static final DeferredItem<KnifeItem> STONE_KNIFE;
    public static final DeferredItem<KnifeItem> FLINT_KNIFE;
    public static final DeferredItem<KnifeItem> BONE_KNIFE;
    public static final DeferredItem<KnifeItem> OBSIDIAN_KNIFE;
    
    static {
        GRASS = new DeferredItem.Builder<>("grass", properties ->
                new Item(properties)).register();
        CLOTH_HELMET = new DeferredItem.Builder<ArmorItem>(
                "cloth_helmet",
                properties -> new ArmorItem(MJArmorMaterials.CLOTH, ArmorItem.Type.HELMET, properties
                        .durability(ArmorItem.Type.HELMET.getDurability(3))
                        .rarity(Rarity.COMMON)
                )).register();
        CLOTH_CHESTPLATE = new DeferredItem.Builder<ArmorItem>(
                "cloth_chestplate",
                properties -> new ArmorItem(MJArmorMaterials.CLOTH, ArmorItem.Type.CHESTPLATE, properties
                        .durability(ArmorItem.Type.CHESTPLATE.getDurability(3))
                        .rarity(Rarity.COMMON)
                )).register();
        CLOTH_LEGGINGS = new DeferredItem.Builder<ArmorItem>(
                "cloth_leggings",
                properties -> new ArmorItem(MJArmorMaterials.CLOTH, ArmorItem.Type.LEGGINGS, properties
                        .durability(ArmorItem.Type.LEGGINGS.getDurability(3))
                        .rarity(Rarity.COMMON)
                )).register();
        CLOTH_BOOTS = new DeferredItem.Builder<ArmorItem>(
                "cloth_boots",
                properties -> new ArmorItem(MJArmorMaterials.CLOTH, ArmorItem.Type.BOOTS, properties
                        .durability(ArmorItem.Type.BOOTS.getDurability(3))
                        .rarity(Rarity.COMMON)
                )).register();
        STONE_MATTOCK = basicMattock("stone_mattock");
        FLINT_MATTOCK = basicMattock("flint_mattock");
        BONE_MATTOCK = basicMattock("bone_mattock");
        OBSIDIAN_MATTOCK = new DeferredItem.Builder<MultitoolItem>(
                "obsidian_mattock",
                properties -> new MultitoolItem(Tiers.IRON, 3, -2.8F, properties
                        .durability(2000)
                        .rarity(Rarity.UNCOMMON)
                        .fireResistant()))
                .register();
        WOODEN_SPEAR = new DeferredItem.Builder<SpearItem>(
                "wooden_spear",
                properties -> new SpearItem(Tiers.WOOD, 3, -3.2F, properties
                        .durability(32)
                        .rarity(Rarity.COMMON))
                ).register();
        INCENDIARY_SPEAR = new DeferredItem.Builder<IncendiarySpearItem>(
                "incendiary_spear",
                properties -> new IncendiarySpearItem(Tiers.WOOD, 3, -3.2F, properties
                        .durability(32)
                        .rarity(Rarity.COMMON))
                ).register();
        STONE_SPEAR = stoneSpear("stone_spear");
        FLINT_SPEAR = stoneSpear("flint_spear");
        BONE_SPEAR = stoneSpear("bone_spear");
        OBSIDIAN_SPEAR = new DeferredItem.Builder<SpearItem>(
                    "obsidian_spear",
                    properties -> new SpearItem(Tiers.IRON, 6, -3.2F, properties
                            .durability(2000)
                            .rarity(Rarity.UNCOMMON)
                            .fireResistant())
                ).register();
        STONE_KNIFE = basicKnife("stone_knife", Tiers.STONE);
        FLINT_KNIFE = basicKnife("flint_knife", Tiers.STONE);
        BONE_KNIFE = basicKnife("bone_knife", Tiers.STONE);
        OBSIDIAN_KNIFE = new DeferredItem.Builder<KnifeItem>(
                "obsidian_knife",
                properties -> new KnifeItem(Tiers.IRON, 3, -2.2F, properties
                        .durability(2000)
                        .rarity(Rarity.UNCOMMON)
                        .fireResistant())
                ).register();
    }
    
    private static DeferredItem<MultitoolItem> basicMattock(String name) {
        return new DeferredItem.Builder<MultitoolItem>(name, properties ->
                new MultitoolItem(Tiers.STONE, 2, -2.8F, properties
                        .durability(128)
                        .rarity(Rarity.COMMON))
                ).register();
    }
    
    private static DeferredItem<SpearItem> stoneSpear(String name) {
        return new DeferredItem.Builder<SpearItem>(name, properties ->
                new SpearItem(Tiers.STONE, 5, -3.2F, properties
                        .durability(128)
                        .rarity(Rarity.COMMON))
                ).register();
    }

    private static DeferredItem<KnifeItem> basicKnife(String name, Tier tier) {
        return new DeferredItem.Builder<KnifeItem>(name, properties ->
                new KnifeItem(tier, 2, -2.2F, properties
                        .durability(128)
                        .rarity(Rarity.COMMON))
            ).register();
    }

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
        public static class Builder<T extends Item> extends Item.Properties {
            private final String NAME;
            private final Supplier<T> ITEM;
            private final Collection<Consumer<T>> mutators = new ArrayList<>();
            protected final Supplier<T> getItem;

            /**
             * Creates the definition for a new item type to be registered.
             * @param name     The name ID of the item to register.
             * @param provider The supplier to create an instance of the item class from a
             *                 given set of item properties.
             */
            public Builder(String name, Function<Item.Properties, T> provider) {
                this.NAME = name;
                this.ITEM = ITEMS.registerItem(this.NAME, provider);
                this.getItem = () -> {
                    T item = ITEM.get();
                    mutators.forEach(m -> m.accept(item));
                    return item;
                };
            }

            /**
             * Registers the item with the deferred register.
             * @return A supplier of the registered item.
             */
            public DeferredItem<T> register() {
                return new DeferredItem<>(this);
            }
        }
    }
}
