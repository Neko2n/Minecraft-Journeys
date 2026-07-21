package com.nekotune.minecraftjourneys.shared.definitions.enchantment;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nekotune.minecraftjourneys.MJConfig;
import com.nekotune.minecraftjourneys.shared.logic.stamina.PlayerStamina;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

public record CycleStaminaEffect(LevelBasedValue amount) implements EnchantmentEntityEffect {
    public static final MapCodec<CycleStaminaEffect> CODEC = RecordCodecBuilder.mapCodec(
        instance -> instance.group(
                LevelBasedValue.CODEC.fieldOf("amount")
                        .forGetter(effect -> effect.amount))
                .apply(instance, CycleStaminaEffect::new)
    );
    private static final String CYCLE_KEY = "CycleStaminaEnchantmentEffect";

    @Override
    public void apply(ServerLevel level, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 origin) {
        if (!(entity instanceof final Player player)) return;
        final var stamina = PlayerStamina.get(player);
        final float drain = enchantmentLevel / 20f / 4f * MJConfig.STAMINA_SOUL_SPEED_MULTIPLIER.get().floatValue();
        stamina.mapCycleToKey(CYCLE_KEY, stamina.addCycle(drain));
    }

    @Override
    public void onDeactivated(EnchantedItemInUse item, Entity entity, Vec3 pos, int enchantmentLevel) {
        if (!(entity instanceof final Player player)) return;
        final var stamina = PlayerStamina.get(player);
        final var cycle = stamina.getCycleFromKey(CYCLE_KEY);
        if (cycle.isEmpty()) return;
        cycle.get().remove();
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
