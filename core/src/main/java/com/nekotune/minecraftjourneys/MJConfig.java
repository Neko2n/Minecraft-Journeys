package com.nekotune.minecraftjourneys;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * An example config class. This is not required, but it's a good idea to have one to keep your config organized.
 * Demonstrates how to use Neo's config APIs
 */
public class MJConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.DoubleValue STAMINA_DRAIN_MULTIPLIER;
    public static final ModConfigSpec.DoubleValue STAMINA_REGEN_MULTIPLIER; 
    public static final ModConfigSpec.IntValue STAMINA_REGEN_COOLDOWN_TICKS;
    
    static {
        BUILDER.comment("# Stamina settings");
        STAMINA_DRAIN_MULTIPLIER = BUILDER
                .comment("A global multiplier applied to stamina drain.")
                .defineInRange("staminaDrainMultiplier", 1.0f, 0.0f, Float.MAX_VALUE);
        STAMINA_REGEN_MULTIPLIER= BUILDER
                .comment("A global multiplier applied to stamina regeneration.")
                .defineInRange("staminaRegenMultiplier", 1.0f, 0.0f, Float.MAX_VALUE);
        STAMINA_REGEN_COOLDOWN_TICKS = BUILDER
                .comment("The cooldown for stamina regeneration, in ticks.")
                .defineInRange("staminaRegenCooldownTicks", () -> 40, 0, Integer.MAX_VALUE);
    }

    public static final ModConfigSpec SPEC = BUILDER.build();
}
