package com.nekotune.minecraftjourneys.shared.registries;

import java.util.function.Supplier;

import com.nekotune.minecraftjourneys.MinecraftJourneys;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class MJSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, MinecraftJourneys.MOD_ID);
    
    public static final Supplier<SoundEvent> SPEAR_THROW;
    public static final Supplier<SoundEvent> SPEAR_HIT;
    public static final Supplier<SoundEvent> SPEAR_HIT_GROUND;

    static {
        SPEAR_THROW = register("spear_throw");
        SPEAR_HIT = register("spear_hit");
        SPEAR_HIT_GROUND = register("spear_hit_ground");
    }

    private static Supplier<SoundEvent> register(String name) {
        return SOUND_EVENTS.register(
            name, () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(MinecraftJourneys.MOD_ID, name)));
    }
}
