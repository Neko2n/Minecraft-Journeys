package com.nekotune.minecraftjourneys.data.tags;

import com.nekotune.minecraftjourneys.MinecraftJourneys;

import net.hecco.bountifulfares.BountifulFares;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class MJBlockTags {
    public static final TagKey<Block> PEAR_LEAVES = createTag(BountifulFares.MOD_ID, "pear_leaves");
    public static final TagKey<Block> PEAR_LOGS = createTag(BountifulFares.MOD_ID, "pear_logs");

    public static final TagKey<Block> MINEABLE_WITH_MULTITOOL = createTag(MinecraftJourneys.MOD_ID, "mineable_with_mattock");

    private static TagKey<Block> createTag(String modid, String name) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(modid, name));
    }
}
