package com.nekotune.minecraftjourneys.shared.registries.content;

import java.util.function.Supplier;

import com.mojang.serialization.MapCodec;
import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.shared.definitions.enchantment.CycleStaminaEffect;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MJEnchantmentEffects {
    public static final DeferredRegister<MapCodec<? extends EnchantmentEntityEffect>> ENTITY_ENCHANTMENT_EFFECTS = DeferredRegister.create(BuiltInRegistries.ENCHANTMENT_ENTITY_EFFECT_TYPE, MinecraftJourneys.MOD_ID);

    public static final Supplier<MapCodec<? extends EnchantmentEntityEffect>> CYCLE_STAMINA =
            ENTITY_ENCHANTMENT_EFFECTS.register("cycle_stamina", () -> CycleStaminaEffect.CODEC);
}
