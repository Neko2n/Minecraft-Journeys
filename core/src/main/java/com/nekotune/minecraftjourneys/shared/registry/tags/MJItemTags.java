package com.nekotune.minecraftjourneys.shared.registry.tags;

import net.hecco.bountifulfares.BountifulFares;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class MJItemTags {
    public static final TagKey<Item> PEAR_LEAVES = createTag(BountifulFares.MOD_ID, "pear_leaves");
    public static final TagKey<Item> PEAR_LOGS = createTag(BountifulFares.MOD_ID, "pear_logs");

    private static TagKey<Item> createTag(String modid, String name) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(modid, name));
    }
}
