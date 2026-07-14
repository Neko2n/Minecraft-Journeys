package com.nekotune.minecraftjourneys.shared.registries.content;

import java.util.function.Supplier;

import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.shared.definitions.entity.projectile.ThrownSpear;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MJEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, MinecraftJourneys.MOD_ID);

    public static final Supplier<EntityType<ThrownSpear>> THROWN_SPEAR;

    static {
        THROWN_SPEAR = register("thrown_spear", EntityType.Builder.<ThrownSpear>of(ThrownSpear::new, MobCategory.MISC)
                .sized(0.5F, 0.5F)
                .eyeHeight(0.13F)
                .clientTrackingRange(4)
                .updateInterval(20));
    }

    private static <T extends Entity> Supplier<EntityType<T>> register(String name, EntityType.Builder<T> builder) {
        return ENTITY_TYPES.register(name, () -> builder.build(name));
    }
}
