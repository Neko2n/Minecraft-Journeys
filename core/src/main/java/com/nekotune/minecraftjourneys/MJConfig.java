package com.nekotune.minecraftjourneys;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class MJConfig {
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.DoubleValue STAMINA_DRAIN_MULTIPLIER;
    public static final ModConfigSpec.DoubleValue STAMINA_REGEN_MULTIPLIER; 
    public static final ModConfigSpec.IntValue STAMINA_REGEN_COOLDOWN_TICKS;
    public static final ModConfigSpec.DoubleValue HEAVY_SWING_STAMINA_MULTIPLIER; 
    public static final ModConfigSpec.ConfigValue<Float> XP_TO_STAMINA; 
    
    static {
        final ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.comment("# Stamina settings");
        STAMINA_DRAIN_MULTIPLIER = builder
                .comment("A global multiplier applied to stamina drain.")
                .defineInRange("stamina.drain_multiplier", 1d, 0d, (double)Float.MAX_VALUE);
        STAMINA_REGEN_MULTIPLIER= builder
                .comment("A global multiplier applied to stamina regeneration.")
                .defineInRange("stamina.regen_multiplier", 1d, 0d, (double)Float.MAX_VALUE);
        STAMINA_REGEN_COOLDOWN_TICKS = builder
                .comment("The cooldown for stamina regeneration, in ticks.")
                .defineInRange("stamina.regen_cooldown", 50, 0, Integer.MAX_VALUE);
        HEAVY_SWING_STAMINA_MULTIPLIER = builder
                .comment("A global multiplier applied to the stamina penalty for swinging heavy weapons.")
                .defineInRange("stamina.heavy_swing_penalty_multiplier", 1d, 0d, Float.MAX_VALUE);
        XP_TO_STAMINA = builder
                .comment("The conversion rate multiplied against gained experience to convert into stamina.")
                .define("stamina.xp_to_stamina", 1f/20f);
        SPEC = builder.build();
    }
}
