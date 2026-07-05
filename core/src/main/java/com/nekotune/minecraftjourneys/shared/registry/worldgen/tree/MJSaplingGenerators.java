package com.nekotune.minecraftjourneys.shared.registry.worldgen.tree;

import java.util.Optional;

import com.nekotune.minecraftjourneys.shared.registry.worldgen.MJConfiguredFeatures;

import net.minecraft.world.level.block.grower.TreeGrower;

public class MJSaplingGenerators {

    public static final TreeGrower PEAR_SAPLING_GENERATOR = new TreeGrower(
            "pear", 0.0F,
            Optional.empty(),
            Optional.empty(),
            Optional.of(MJConfiguredFeatures.PEAR_KEY),
            Optional.empty(),
            Optional.empty(),
            Optional.empty());
}
