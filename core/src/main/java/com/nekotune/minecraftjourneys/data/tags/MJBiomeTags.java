package com.nekotune.minecraftjourneys.data.tags;

import net.hecco.bountifulfares.BountifulFares;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class MJBiomeTags {
    public static final TagKey<Biome> HAS_PEAR_TREES = createTag(BountifulFares.MOD_ID, "has_pear_trees");
    
    private static TagKey<Biome> createTag(String modid, String name) {
        return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(modid, name));
    }
}
