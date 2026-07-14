package com.nekotune.minecraftjourneys.data.tags;

import com.nekotune.minecraftjourneys.MinecraftJourneys;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class MJItemTags {
    public static final TagKey<Item> SPEAR_ENCHANTABLE = createTag("spear_enchantable");
    public static final TagKey<Item> KNIFE_ENCHANTABLE = createTag("knife_enchantable");
    public static final TagKey<Item> MATTOCK_ENCHANTABLE = createTag("mattock_enchantable");
    public static final TagKey<Item> LOYALTY_ENCHANTABLE = createTag("loyalty_enchantable");
    public static final TagKey<Item> PIERCING_ENCHANTABLE = createTag("piercing_enchantable");

    public static final class Tools {

        public static final TagKey<Item> SPEARS = createCoreTag("tools/spears");
        public static final TagKey<Item> KNIVES = createCoreTag("tools/knives");
        public static final TagKey<Item> MATTOCKS = createCoreTag("tools/mattocks");
    }

    public static final class Armors {

        public static final TagKey<Item> CLOTH = createCoreTag("armors/cloth");
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
