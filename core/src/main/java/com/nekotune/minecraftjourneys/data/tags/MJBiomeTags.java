package com.nekotune.minecraftjourneys.data.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class MJBiomeTags {
    private static TagKey<Biome> createTag(String modid, String name) {
        return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(modid, name));
    }
}
