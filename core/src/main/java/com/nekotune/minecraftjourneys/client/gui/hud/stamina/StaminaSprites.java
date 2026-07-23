package com.nekotune.minecraftjourneys.client.gui.hud.stamina;

import com.nekotune.minecraftjourneys.MinecraftJourneys;

import net.minecraft.resources.ResourceLocation;

public final class StaminaSprites {

    public static final String PATH_PREFIX = "hud/stamina/";
    public static final ResourceLocation DELTA_SPRITE = ResourceLocation.fromNamespaceAndPath(
            MinecraftJourneys.MOD_ID, PATH_PREFIX + "delta");
    public static final ResourceLocation ARROW_SPRITE_1 = ResourceLocation.fromNamespaceAndPath(
            MinecraftJourneys.MOD_ID, PATH_PREFIX + "arrow/1");
    public static final ResourceLocation ARROW_SPRITE_2 = ResourceLocation.fromNamespaceAndPath(
            MinecraftJourneys.MOD_ID, PATH_PREFIX + "arrow/2");

    public static enum SpriteType {
        BAR_FILL,
        BAR_BACKGROUND
    }

    private static ResourceLocation backgroundSprite = null;
    private static ResourceLocation filledSprite = null;
    private static int i = -1;

    public static ResourceLocation fromCache(final SpriteType type, final int maxStamina) {
        if (i != maxStamina) {
            i = maxStamina;
            final String s = String.valueOf(maxStamina);
            filledSprite = ResourceLocation.fromNamespaceAndPath(
                    MinecraftJourneys.MOD_ID, PATH_PREFIX + "filled/" + s);
            backgroundSprite = ResourceLocation.fromNamespaceAndPath(
                    MinecraftJourneys.MOD_ID, PATH_PREFIX + "background/" + s);
        }
        switch(type) {
            case SpriteType.BAR_FILL: {
                return filledSprite;
            }
            case SpriteType.BAR_BACKGROUND: {
                return backgroundSprite;
            }
        }
        return null; // Cannot be reached
    }
}