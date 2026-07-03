package com.nekotune.minecraftjourneys.registry.misc;

import com.nekotune.minecraftjourneys.MinecraftJourneys;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class MJConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> PEAR_KEY = registerKey("pear");

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE,
                ResourceLocation.fromNamespaceAndPath(MinecraftJourneys.MODID, name));
    }
}
