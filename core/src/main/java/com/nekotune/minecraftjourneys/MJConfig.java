package com.nekotune.minecraftjourneys;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * An example config class. This is not required, but it's a good idea to have one to keep your config organized.
 * Demonstrates how to use Neo's config APIs
 */
public class MJConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.ConfigValue<Float> STAMINA_DRAIN_MULTIPLIER = BUILDER
            .comment("A global multiplier applied to stamina drain")
            .define("staminaDrainMultiplier", 1.0f);

    public static final ModConfigSpec.ConfigValue<Float> STAMINA_REGEN_MULTIPLIER = BUILDER
            .comment("A global multiplier applied to stamina regeneration")
            .define("staminaRegenMultiplier", 1.0f);

    public static final ModConfigSpec SPEC = BUILDER.build();
}
