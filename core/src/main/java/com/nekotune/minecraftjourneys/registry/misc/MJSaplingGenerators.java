package com.nekotune.minecraftjourneys.registry.misc;

import java.util.Optional;

import net.minecraft.world.level.block.grower.TreeGrower;

public class MJSaplingGenerators {

    public static final TreeGrower PEAR_SAPLING_GENERATOR;

    static {
        PEAR_SAPLING_GENERATOR = new TreeGrower("pear", 0.0F, Optional.empty(), Optional.empty(),
                Optional.of(MJConfiguredFeatures.PEAR_KEY), Optional.empty(), Optional.empty(), Optional.empty());
    }
}
