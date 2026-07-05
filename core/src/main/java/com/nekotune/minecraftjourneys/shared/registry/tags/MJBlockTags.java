package com.nekotune.minecraftjourneys.shared.registry.tags;

import net.hecco.bountifulfares.BountifulFares;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class MJBlockTags {
    public static final TagKey<Block> PEAR_LEAVES = createTag(BountifulFares.MOD_ID, "pear_leaves");
    public static final TagKey<Block> PEAR_LOGS = createTag(BountifulFares.MOD_ID, "pear_logs");

    private static TagKey<Block> createTag(String modid, String name) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(modid, name));
    }
}
