package com.nekotune.minecraftjourneys.data.datagen;

import java.util.Set;

import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.data.content.MJEnchantments;
import com.nekotune.minecraftjourneys.data.worldgen.MJBiomeModifiers;
import com.nekotune.minecraftjourneys.data.worldgen.MJConfiguredFeatures;
import com.nekotune.minecraftjourneys.data.worldgen.MJPlacedFeatures;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries.Keys;

@OnlyIn(value = Dist.DEDICATED_SERVER)
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
