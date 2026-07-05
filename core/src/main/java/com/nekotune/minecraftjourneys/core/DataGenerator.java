package com.nekotune.minecraftjourneys.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.nekotune.minecraftjourneys.MinecraftJourneys;

import net.minecraft.data.DataProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.modscan.ModAnnotation;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforgespi.language.ModFileScanData;

public class DataGenerator {

    /**
     * Marks a class implementing {@link DataProvider} to be automatically
     * constructed and added to the data generator, on the given distribution side.
     * The annotated class must declare a constructor whose first parameter is a
     * {@link GatherDataEvent}; use it to pull whatever the provider needs (pack
     * output, lookup provider, existing file helper, etc.) off the event.
     * @see DataGeneratorProviderHandler#onGatherData
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface DataGenProvider {
        /**
         * The distribution side that the data should generate on.
         */
        Dist dist();
    }

    /**
     * Scans for {@link DataGenProvider}-annotated classes
     * and constructs/adds them to the data generator.
     */
    @EventBusSubscriber(modid = MinecraftJourneys.MODID)
    protected final class DataGeneratorProviderHandler {

        /**
         * Constructs every {@link DataGenProvider}-annotated {@link DataProvider}
         * class whose {@code dist} matches the current gather-data side.
         * @param event The gather data event. Used to look up the jar's annotation scan
         *              data, filter by side, and construct each provider.
         */
        @SubscribeEvent
        public static void onGatherData(GatherDataEvent event) {
            ModFileScanData scanData = event.getModContainer().getModInfo().getOwningFile().getFile().getScanResult();
            List<ModFileScanData.AnnotationData> pending = new ArrayList<>();
            scanData.getAnnotatedBy(DataGenProvider.class, ElementType.TYPE).forEach(data -> {
                Dist dist = Dist.valueOf(((ModAnnotation.EnumHolder) data.annotationData().get("dist")).value());

                // Only generate client data on the client, and server data on the server
                if ((dist == Dist.CLIENT && !event.includeClient())
                        || (dist == Dist.DEDICATED_SERVER && !event.includeServer())) {
                    return;
                }

                pending.add(data);
            });
            Map<Class<?>, DataProvider> constructed = new HashMap<>();
            while (!pending.isEmpty()) {
                boolean progressed = false;

                for (Iterator<ModFileScanData.AnnotationData> it = pending.iterator(); it.hasNext();) {
                    ModFileScanData.AnnotationData data = it.next();
                    String className = data.clazz().getClassName();
                    
                    // Derive and verify the class
                    Class<?> clazz;
                    try {
                        clazz = Class.forName(className, false, DataGenerator.class.getClassLoader());
                    } catch (ReflectiveOperationException e) {
                        throw new RuntimeException("Failed to load @DataGenProvider class " + className, e);
                    }
                    if (!DataProvider.class.isAssignableFrom(clazz)) {
                        throw new IllegalStateException(
                                "@DataGenProvider class " + className + " must implement DataProvider");
                    }

                    // Derive and verify the constructor
                    Constructor<?> constructor = null;
                    Object[] args = null;
                    for (Constructor<?> candidate : clazz.getDeclaredConstructors()) {
                        Class<?>[] paramTypes = candidate.getParameterTypes();
                        if (paramTypes.length == 0 || paramTypes[0] != GatherDataEvent.class) {
                            continue;
                        }

                        Object[] candidateArgs = new Object[paramTypes.length];
                        candidateArgs[0] = event;
                        boolean satisfied = true;
                        for (int i = 1; i < paramTypes.length; i++) {
                            DataProvider dependency = constructed.get(paramTypes[i]);
                            if (dependency == null) {
                                satisfied = false;
                                break;
                            }
                            candidateArgs[i] = dependency;
                        }

                        if (satisfied) {
                            constructor = candidate;
                            args = candidateArgs;
                            break;
                        }
                    }

                    // Dependencies not constructed yet, defer
                    if (constructor == null) {
                        continue;
                    }

                    // Construct and add the provider
                    try {
                        constructor.setAccessible(true);
                        DataProvider dataProvider = (DataProvider) constructor.newInstance(args);
                        constructed.put(clazz, dataProvider);
                        event.addProvider(dataProvider);
                    } catch (ReflectiveOperationException e) {
                        throw new RuntimeException("Failed to construct @DataGenProvider class " + className, e);
                    }

                    it.remove();
                    progressed = true;
                }

                if (!progressed) {
                    List<String> unresolved = pending.stream().map(data -> data.clazz().getClassName()).toList();
                    throw new IllegalStateException(
                            "Could not resolve @DataGenProvider dependencies (missing constructor or dependency cycle) for: "
                                    + unresolved);
                }
            }
        }
    }
}
