package com.nekotune.minecraftjourneys;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class MJConfig {
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.DoubleValue STAMINA_DRAIN_MULTIPLIER;
    public static final ModConfigSpec.DoubleValue STAMINA_REGEN_MULTIPLIER; 
    public static final ModConfigSpec.IntValue STAMINA_REGEN_COOLDOWN_TICKS;
    public static final ModConfigSpec.DoubleValue HEAVY_SWING_STAMINA_MULTIPLIER; 
    
    static {
        final ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.comment("# Stamina settings");
        STAMINA_DRAIN_MULTIPLIER = builder
                .comment("A global multiplier applied to stamina drain.")
                .defineInRange("staminaDrainMultiplier", 1.0f, 0.0f, Float.MAX_VALUE);
        STAMINA_REGEN_MULTIPLIER= builder
                .comment("A global multiplier applied to stamina regeneration.")
                .defineInRange("staminaRegenMultiplier", 1.0f, 0.0f, Float.MAX_VALUE);
        STAMINA_REGEN_COOLDOWN_TICKS = builder
                .comment("The cooldown for stamina regeneration, in ticks.")
                .defineInRange("staminaRegenCooldownTicks", () -> 40, 0, Integer.MAX_VALUE);
        HEAVY_SWING_STAMINA_MULTIPLIER = builder
                .comment("A global multiplier applied to the stamina penalty for swinging heavy weapons.")
                .defineInRange("heavySwingStaminaMultiplier", 1.0f, 0.0f, Float.MAX_VALUE);
        SPEC = builder.build();
    }
}
