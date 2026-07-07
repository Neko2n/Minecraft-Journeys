package com.nekotune.minecraftjourneys.shared.registry.util;

import com.nekotune.minecraftjourneys.MinecraftJourneys;

import net.minecraft.resources.ResourceLocation;

public final class MJResourceLocations {
    public static final class Paths {
        public static final String ARMOR = "armor/";
        public static final String TOOL = "tool/";
    }

    public static final ResourceLocation ARMOR_CLOTH = at(Paths.ARMOR + "cloth");
    public static final ResourceLocation TOOL_MATTOCK = at(Paths.TOOL + "mattock");

    private static ResourceLocation at(String name) {
        return ResourceLocation.fromNamespaceAndPath(MinecraftJourneys.MOD_ID, name);
    }
}
