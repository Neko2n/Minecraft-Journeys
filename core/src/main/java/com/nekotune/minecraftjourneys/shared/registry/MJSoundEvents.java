package com.nekotune.minecraftjourneys.shared.registry;

import java.util.function.Supplier;

import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.core.RegistryHandler.RegisterDeferred;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MJSoundEvents {
    @RegisterDeferred
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, MinecraftJourneys.MOD_ID);
    
    public static final Supplier<SoundEvent> SPEAR_THROW = register("spear_throw");
    public static final Supplier<SoundEvent> SPEAR_HIT = register("spear_hit");
    public static final Supplier<SoundEvent> SPEAR_HIT_GROUND = register("spear_hit_ground");

    private static Supplier<SoundEvent> register(String name) {
        return SOUND_EVENTS.register(
            name, () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(MinecraftJourneys.MOD_ID, name)));
    }
}
