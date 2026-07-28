package com.nekotune.minecraftjourneys;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.nekotune.minecraftjourneys.shared.registries.MJRegistries;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;

@Mod(MinecraftJourneys.MOD_ID)
public class MinecraftJourneys {

    public static final String MOD_ID = "modpack";
    public static final Logger LOG = LogUtils.getLogger();

    public MinecraftJourneys(final IEventBus modEventBus, final ModContainer modContainer) {

        // Register deferred registry content
        MJRegistries.registerAll(modEventBus);

        // Register config spec to NeoForge
        modContainer.registerConfig(ModConfig.Type.COMMON, MJConfig.SPEC);

        // Register event subscriber classes
        MJDependency.registerDependentEventBusSubscribers(MOD_ID, LOG);
    }
}
