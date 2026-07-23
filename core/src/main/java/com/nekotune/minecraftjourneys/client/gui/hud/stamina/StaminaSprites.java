package com.nekotune.minecraftjourneys.client.gui.hud.stamina;

import java.util.Map;
import java.util.WeakHashMap;

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

    public static final class BarSprites {

        public static enum SpriteType {
            FILL,
            BACKGROUND
        }

        private static Map<SpriteType, ResourceLocation> spriteCache = new WeakHashMap<>();
        private static int i = -1;
        
        public static ResourceLocation fromCache(final SpriteType type, final int maxStamina) {
            if (i != maxStamina) {
                i = maxStamina;
                final String s = String.valueOf(maxStamina);
                spriteCache.put(type, ResourceLocation.fromNamespaceAndPath(
                        MinecraftJourneys.MOD_ID, StaminaSprites.PATH_PREFIX + "filled/" + s));
                spriteCache.put(type, ResourceLocation.fromNamespaceAndPath(
                        MinecraftJourneys.MOD_ID, StaminaSprites.PATH_PREFIX + "background/" + s));
            }
            return spriteCache.get(type);
        }
    }
}