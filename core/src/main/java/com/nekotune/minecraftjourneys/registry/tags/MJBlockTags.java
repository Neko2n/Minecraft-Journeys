package com.nekotune.minecraftjourneys.registry.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class MJBlockTags {
    public static final TagKey<Block> PEAR_LEAVES = createBlockTag("pear_leaves");
    public static final TagKey<Block> PEAR_LOGS = createBlockTag("pear_logs");

    private static TagKey<Block> createBlockTag(String name) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("bountifulfares", name));
    }
}
