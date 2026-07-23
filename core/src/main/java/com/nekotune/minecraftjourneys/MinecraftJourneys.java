package com.nekotune.minecraftjourneys;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.EnumMap;
import java.util.Map;

import javax.annotation.Nullable;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.nekotune.minecraftjourneys.shared.registries.MJRegistries;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(MinecraftJourneys.MOD_ID)
public class MinecraftJourneys {
    public static final String MOD_ID = "modpack";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static enum Dependency {
        INCREMENTAL_MINING,
        BETTER_COMBAT,
        RELIABLE_GLIDERS,
        ALL_WITH_YOU
    }
    public static final EnumMap<Dependency, ModDependency> DEPENDENCIES = new EnumMap<>(Map.of(
        Dependency.INCREMENTAL_MINING, new ModDependency("incrementalmining"),
        Dependency.BETTER_COMBAT, new ModDependency("bettercombat"),
        Dependency.RELIABLE_GLIDERS, new ModDependency("reliable_gliders"),
        Dependency.ALL_WITH_YOU, new ModDependency("all_with_you")
    ));
    
    
    /**
     * Annotate a class which will be subscribed to an Event Bus at mod construction time
     * only if the given dependency is loaded.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public static @interface DependentEventBusSubscriber {

        /**
         * The mod dependency that should be checked.
         */
        public Dependency dependency();
        
        @Nullable
        public Dist[] value() default {Dist.CLIENT, Dist.DEDICATED_SERVER};
    }

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

        // Register optional dependency event subscriber classes
        final var annotationType = DependentEventBusSubscriber.class;
        ModList.get().getModFileById(MOD_ID).getFile().getScanResult()
                .getAnnotatedBy(annotationType, ElementType.TYPE)
                .forEach(annotationData -> {
                    final Class<?> clazz;
                    try {
                        clazz = Class.forName(annotationData.clazz().getClassName());
                    } catch (final ClassNotFoundException e) {
                        LOGGER.error(e.getLocalizedMessage());
                        return;
                    }
                    final var annotation = clazz.getAnnotation(annotationType);
                    for (Dist dist : annotation.value()) {
                        if (FMLEnvironment.dist == dist) {
                            if (DEPENDENCIES.get(annotation.dependency()).isLoaded()) {
                                NeoForge.EVENT_BUS.register(clazz);
                            }
                            return;
                        }
                    }
                });

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
    
    /**
     * Data type holding information about a mod dependency.
     */
    public static final class ModDependency {

        /**
         * The dependency's string mod id.
         */
        public final String MOD_ID;

        private Boolean isLoaded = null;

        private ModDependency(final String mod_id) {
            MOD_ID = mod_id;
        }

        /**
         * @return True if the dependency is loaded.
         * @see ModList#isLoaded()
         */
        public boolean isLoaded() {
            if (isLoaded == null) {
                isLoaded = ModList.get().isLoaded(this.MOD_ID);
            }
            return isLoaded;
        }
    }
}
