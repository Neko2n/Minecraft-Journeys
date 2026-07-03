package com.nekotune.minecraftjourneys.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;

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
     * constructed and
     * added to the data generator, on the given distribution side.
     * The annotated class must declare a constructor that accepts a single
     * {@link GatherDataEvent}
     * parameter; use it to pull whatever the provider needs (pack output, lookup
     * provider,
     * existing file helper, etc.) off the event.
     * 
     * @see DataGeneratorProviderHandler#registerAll
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
     * Scans this mod's jar for {@link DataGenProvider}-annotated classes
     * and constructs/adds them to the data generator.
     */
    @EventBusSubscriber(modid = MinecraftJourneys.MODID)
    protected final class DataGeneratorProviderHandler {

        /**
         * Constructs every {@link DataGenProvider}-annotated {@link DataProvider}
         * class found anywhere in
         * the mod's jar whose {@code dist} matches the current gather-data side, using
         * FML's annotation scan
         * data instead of a hard-coded list of classes.
         * 
         * @param event The gather data event. Used to look up the jar's annotation scan
         *              data,
         *              filter by side, and construct each provider.
         */
        @SubscribeEvent
        public static void onGatherData(GatherDataEvent event) {
            ModFileScanData scanData = event.getModContainer().getModInfo().getOwningFile().getFile().getScanResult();
            scanData.getAnnotatedBy(DataGenProvider.class, ElementType.TYPE).forEach(data -> {

                Dist dist = Dist.valueOf(((ModAnnotation.EnumHolder) data.annotationData().get("dist")).value());

                // Only generate client data on the client, and server data on the server
                if ((dist == Dist.CLIENT && !event.includeClient())
                        || (dist == Dist.DEDICATED_SERVER && !event.includeServer())) {
                    return;
                }

                String className = data.clazz().getClassName();
                try {
                    Class<?> clazz = Class.forName(className, false, DataGenerator.class.getClassLoader());
                    if (!DataProvider.class.isAssignableFrom(clazz)) {
                        throw new IllegalStateException(
                                "@DataGeneratorProvider class " + className + " must implement DataProvider");
                    }
                    
                    Constructor<?> constructor = clazz.getDeclaredConstructor(GatherDataEvent.class);
                    constructor.setAccessible(true);
                    DataProvider dataProvider = (DataProvider) constructor.newInstance(event);
                    event.addProvider(dataProvider);
                } catch (ReflectiveOperationException e) {
                    throw new RuntimeException("Failed to construct @DataGeneratorProvider class " + className, e);
                }
            });
        }
    }
}
