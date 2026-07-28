package com.nekotune.minecraftjourneys;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.slf4j.Logger;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;

public enum MJDependency {
    INCREMENTAL_MINING("incrementalmining"),
    BETTER_COMBAT("bettercombat"),
    RELIABLE_GLIDERS("reliable_gliders"),
    ALL_WITH_YOU("all_with_you");

    /**
     * Annotate a class which will be subscribed to an Event Bus at mod construction
     * time only if the given dependency is loaded.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public static @interface DependentEventBusSubscriber {

        /**
         * The mod dependency that should be checked.
         */
        public MJDependency dependency();

        public Dist[] value() default { Dist.CLIENT, Dist.DEDICATED_SERVER };
    }

    /**
     * Registers all classes annotated with DependentEventBusSubscriber to the NeoForge event bus.
     * @param modId The mod ID
     * @param log The logger to log errors to
     */
    public static void registerDependentEventBusSubscribers(final String modId, final Logger log) {
        final var annotationType = DependentEventBusSubscriber.class;
        ModList.get().getModFileById(modId).getFile().getScanResult()
                .getAnnotatedBy(annotationType, ElementType.TYPE)
                .forEach(annotationData -> {
                    final Class<?> clazz;
                    try {
                        clazz = Class.forName(annotationData.clazz().getClassName());
                    } catch (final ClassNotFoundException e) {
                        log.error(e.getLocalizedMessage());
                        return;
                    }
                    final var annotation = clazz.getAnnotation(annotationType);
                    for (Dist dist : annotation.value()) {
                        if (FMLEnvironment.dist == dist) {
                            if (annotation.dependency().isLoaded()) {
                                NeoForge.EVENT_BUS.register(clazz);
                            }
                            return;
                        }
                    }
                });
    }

    /**
     * The dependency's string mod id.
     */
    public final String MOD_ID;

    private Boolean isLoaded = null;

    private MJDependency(final String modId) {
        MOD_ID = modId;
    }

    /**
     * @return True if the dependency is loaded.
     * @see ModList#isLoaded()
     */
    public final boolean isLoaded() {
        if (isLoaded == null) {
            isLoaded = ModList.get().isLoaded(this.MOD_ID);
        }
        return isLoaded;
    }
}