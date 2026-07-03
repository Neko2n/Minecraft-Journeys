package com.nekotune.minecraftjourneys.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforgespi.language.ModFileScanData;

public class RegistryHandler {

    /**
     * Marks a static {@link DeferredRegister}
     * field to be automatically registered to the mod event bus.
     * 
     * @see RegistryHandler#registerAll
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Register {}

    /**
     * Registers every {@link Register}-annotated {@link DeferredRegister} field
     * found anywhere in the mod's jar,
     * using FML's annotation scan data instead of a hard-coded list of classes.
     * 
     * @param modEventBus  The mod event bus to register to.
     * @param modContainer This mod's container, used to look up the jar's
     *                     annotation scan data.
     * @param classLoader  The program's class loader, for searching for annotated classes.
     */
    public static void registerAll(IEventBus modEventBus, ModContainer modContainer, ClassLoader classLoader) {
        ModFileScanData scanData = modContainer.getModInfo().getOwningFile().getFile().getScanResult();
        scanData.getAnnotatedBy(Register.class, ElementType.FIELD).forEach(data -> {
            String className = data.clazz().getClassName();
            String fieldName = data.memberName();
            try {
                Class<?> clazz = Class.forName(className, false, classLoader);
                Field field = clazz.getDeclaredField(fieldName);
                if (!Modifier.isStatic(field.getModifiers())
                        || !DeferredRegister.class.isAssignableFrom(field.getType())) {
                    throw new IllegalStateException(
                            "@Registry field " + className + "#" + fieldName + " must be a static DeferredRegister");
                }
                field.setAccessible(true);
                ((DeferredRegister<?>) field.get(null)).register(modEventBus);

                // Nested classes (used to group related entries, e.g. MJBlocks.BFPearBlocks) are
                // only initialized by the JVM on first active use. Force-load them now so any
                // registration calls in their field initializers run while the DeferredRegister
                // is still open, instead of lazily (e.g. from datagen) after RegisterEvent fires.
                forceInitNestedClasses(clazz, classLoader);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(
                        "Failed to access @Registry field " + className + "#" + fieldName, e);
            }
        });
    }

    private static void forceInitNestedClasses(Class<?> clazz, ClassLoader classLoader)
            throws ReflectiveOperationException {
        for (Class<?> nested : clazz.getDeclaredClasses()) {
            Class.forName(nested.getName(), true, classLoader);
            forceInitNestedClasses(nested, classLoader);
        }
    }
}
