package com.nekotune.minecraftjourneys;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.nekotune.minecraftjourneys.shared.registries.MJRegistries;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(MinecraftJourneys.MOD_ID)
public class MinecraftJourneys {
    public static final String MOD_ID = "modpack";
    public static final Logger LOGGER = LogUtils.getLogger();

    /**
     * The constructor for the mod class is the first code that is run when your mod is loaded.
     * FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
     */
    public MinecraftJourneys(IEventBus modEventBus, ModContainer modContainer) {
        // Register setup events
        modEventBus.addListener(this::commonSetup);

        MJRegistries.registerAll(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (MinecraftJourneysCore) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, MJConfig.SPEC);
    }

    /**
     * First of four common setup events fired on startup.
     * @param event {@link FMLCommonSetupEvent}
     */
    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    /**
     * Fires when the dedicated server begins starting.
     * @param event {@link ServerStartingEvent}
     */
    @SubscribeEvent
    private void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }
}
