package com.nekotune.minecraftjourneys.shared.registry.content.capabilities;

import com.mojang.serialization.MapCodec;
import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.core.RegistryHandler.RegisterDeferred;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MJEnchantmentEffects {
    @RegisterDeferred
    public static final DeferredRegister<MapCodec<? extends EnchantmentEntityEffect>> ENTITY_ENCHANTMENT_EFFECTS = DeferredRegister.create(BuiltInRegistries.ENCHANTMENT_ENTITY_EFFECT_TYPE, MinecraftJourneys.MOD_ID);

    // public static final Supplier<MapCodec<? extends EnchantmentEntityEffect>> PIERCING =
    //         ENTITY_ENCHANTMENT_EFFECTS.register("piercing", () -> PiercingEnchantmentEffect.CODEC);
}
