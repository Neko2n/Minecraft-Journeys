package com.nekotune.minecraftjourneys.shared.registry.tags;

import com.nekotune.minecraftjourneys.MinecraftJourneys;

import net.hecco.bountifulfares.BountifulFares;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class MJItemTags {
    public static final TagKey<Item> PEAR_LEAVES = createTag(BountifulFares.MOD_ID, "pear_leaves");
    public static final TagKey<Item> PEAR_LOGS = createTag(BountifulFares.MOD_ID, "pear_logs");
    
    public static final TagKey<Item> SPEAR_ENCHANTABLE = createTag("spear_enchantable");
    public static final TagKey<Item> KNIFE_ENCHANTABLE = createTag("knife_enchantable");
    public static final TagKey<Item> MATTOCK_ENCHANTABLE = createTag("mattock_enchantable");

    public static final class Equipment {

        public static final TagKey<Item> SPEARS = createCoreTag("spears");
        public static final TagKey<Item> KNIVES = createCoreTag("knives");
        public static final TagKey<Item> MATTOCKS = createCoreTag("mattocks");
    }

    private static TagKey<Item> createTag(String modid, String name) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(modid, name));
    }
    private static TagKey<Item> createTag(String name) {
        return createTag(MinecraftJourneys.MOD_ID, name);
    }
    private static TagKey<Item> createCoreTag(String name) {
        return createTag("c", name);
    }
}
