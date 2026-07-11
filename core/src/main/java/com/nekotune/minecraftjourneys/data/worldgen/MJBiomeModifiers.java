package com.nekotune.minecraftjourneys.data.worldgen;

import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.data.tags.MJBiomeTags;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries.Keys;

public class MJBiomeModifiers {
    
    public static final ResourceKey<BiomeModifier> ADD_PEAR_TREE = registerKey("add_tree_pear");

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        context.register(ADD_PEAR_TREE, new BiomeModifiers.AddFeaturesBiomeModifier(
            biomes.getOrThrow(MJBiomeTags.HAS_PEAR_TREES),
            HolderSet.direct(placedFeatures.getOrThrow(MJPlacedFeatures.PEAR_PLACED_KEY)),
            GenerationStep.Decoration.VEGETAL_DECORATION));
    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(Keys.BIOME_MODIFIERS,
                ResourceLocation.fromNamespaceAndPath(MinecraftJourneys.MOD_ID, name));
    }
}
