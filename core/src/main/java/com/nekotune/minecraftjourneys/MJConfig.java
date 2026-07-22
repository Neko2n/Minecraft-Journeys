package com.nekotune.minecraftjourneys;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class MJConfig {
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.DoubleValue STAMINA_BASE_DRAIN_MULTIPLIER;
    public static final ModConfigSpec.DoubleValue STAMINA_BASE_REGEN_MULTIPLIER; 
    public static final ModConfigSpec.IntValue STAMINA_REGEN_COOLDOWN_TICKS;
    public static final ModConfigSpec.DoubleValue HEAVY_SWING_STAMINA_MULTIPLIER; 
    public static final ModConfigSpec.ConfigValue<Float> XP_TO_STAMINA; 
    public static final ModConfigSpec.DoubleValue STAMINA_SOUL_SPEED_MULTIPLIER; 
    public static final ModConfigSpec.DoubleValue STAMINA_GLIDER_DRAIN_MULTIPLIER; 
    public static final ModConfigSpec.DoubleValue HEAVY_ITEM_STAMINA_PENALTY; 
    
    static {
        final ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.comment("# Stamina settings");
        STAMINA_BASE_DRAIN_MULTIPLIER = builder
                .comment("A global multiplier applied to base stamina drain.")
                .defineInRange("stamina.base_drain_multiplier", 1d, 0d, (double)Float.MAX_VALUE);
        STAMINA_BASE_REGEN_MULTIPLIER= builder
                .comment("A global multiplier applied to base stamina regeneration.")
                .defineInRange("stamina.base_regen_multiplier", 1d, 0d, (double)Float.MAX_VALUE);
        STAMINA_REGEN_COOLDOWN_TICKS = builder
                .comment("The cooldown for stamina regeneration, in ticks.")
                .defineInRange("stamina.regen_cooldown", 60, 0, Integer.MAX_VALUE);
        HEAVY_SWING_STAMINA_MULTIPLIER = builder
                .comment("A global multiplier applied to the stamina penalty for swinging heavy weapons.")
                .defineInRange("stamina.heavy_swing_penalty_multiplier", 1d, 0d, Float.MAX_VALUE);
        XP_TO_STAMINA = builder
                .comment("The conversion rate multiplied against gained experience to convert into stamina.")
                .define("stamina.xp_to_stamina", 1f/20f);
        STAMINA_SOUL_SPEED_MULTIPLIER = builder
                .comment("A global multiplier applied to the stamina penalty for the soul speed enchantment.")
                .defineInRange("stamina.soul_speed_multiplier", 1d, 0d, Float.MAX_VALUE);
        STAMINA_GLIDER_DRAIN_MULTIPLIER = builder
                .comment("A global multiplier applied to stamina consumption from using Reliable Gliders' gliders.")
                .defineInRange("stamina.glider_drain_multiplier", 1d, 0d, Float.MAX_VALUE);
        HEAVY_ITEM_STAMINA_PENALTY = builder
                .comment("Multiplier applied to your stamina while carrying too many heavy items.")
                .defineInRange("stamina.heavy_item_penalty", 0.5d, 0d, 1d);
        SPEC = builder.build();
    }
}
