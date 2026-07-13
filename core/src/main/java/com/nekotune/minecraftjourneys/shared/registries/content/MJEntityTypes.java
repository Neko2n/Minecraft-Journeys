package com.nekotune.minecraftjourneys.shared.registries.content;

import java.util.function.Supplier;

import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.shared.definitions.entity.projectile.ThrownSpear;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MJEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, MinecraftJourneys.MOD_ID);

    public static final Supplier<EntityType<ThrownSpear>> THROWN_SPEAR;

    static {
        THROWN_SPEAR = ENTITY_TYPES.register("thrown_spear", () -> EntityType.Builder.<ThrownSpear>of(ThrownSpear::new, MobCategory.MISC)
                .sized(1f, 1f).build("thrown_spear"));
    }
}
