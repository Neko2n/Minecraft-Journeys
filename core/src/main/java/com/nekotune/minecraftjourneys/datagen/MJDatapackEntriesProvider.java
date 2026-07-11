package com.nekotune.minecraftjourneys.datagen;

import java.util.Set;

import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.core.DataGenerator.DataGenProvider;
import com.nekotune.minecraftjourneys.shared.registry.content.MJEnchantments;
import com.nekotune.minecraftjourneys.shared.registry.worldgen.MJBiomeModifiers;
import com.nekotune.minecraftjourneys.shared.registry.worldgen.MJConfiguredFeatures;
import com.nekotune.minecraftjourneys.shared.registry.worldgen.MJPlacedFeatures;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries.Keys;

@DataGenProvider(dist = Dist.DEDICATED_SERVER)
public class MJDatapackEntriesProvider extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.ENCHANTMENT, MJEnchantments::bootstrap)
            .add(Registries.CONFIGURED_FEATURE, MJConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, MJPlacedFeatures::bootstrap)
            .add(Keys.BIOME_MODIFIERS, MJBiomeModifiers::bootstrap);

    public MJDatapackEntriesProvider(GatherDataEvent event) {
        super(event.getGenerator().getPackOutput(),
                event.getLookupProvider(),
                BUILDER,
                Set.of(MinecraftJourneys.MOD_ID));
    }
    
}
