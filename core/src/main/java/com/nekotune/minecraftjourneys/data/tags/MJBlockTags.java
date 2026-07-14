package com.nekotune.minecraftjourneys.data.tags;

import com.nekotune.minecraftjourneys.MinecraftJourneys;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class MJBlockTags {
    public static final TagKey<Block> MINEABLE_WITH_MULTITOOL = createTag("mineable_with_mattock");

    public static final TagKey<Block> DROPS_GRASS = createTag("drops_grass");
    public static final TagKey<Block> DROPS_GRASS_EXTRA = createTag("drops_grass_extra");

    private static TagKey<Block> createTag(String modid, String name) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(modid, name));
    }
    private static TagKey<Block> createTag(String name) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(MinecraftJourneys.MOD_ID, name));
    }
}
