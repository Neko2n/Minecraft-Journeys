package com.nekotune.minecraftjourneys.data.worldgen;

import com.nekotune.minecraftjourneys.MinecraftJourneys;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.NeoForgeRegistries.Keys;

public class MJBiomeModifiers {
    
    public static void bootstrap(BootstrapContext<BiomeModifier> context) {}

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(Keys.BIOME_MODIFIERS,
                ResourceLocation.fromNamespaceAndPath(MinecraftJourneys.MOD_ID, name));
    }
}
