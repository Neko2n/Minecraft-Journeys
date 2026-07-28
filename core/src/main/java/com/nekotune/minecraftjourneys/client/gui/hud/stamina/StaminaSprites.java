package com.nekotune.minecraftjourneys.client.gui.hud.stamina;

import java.util.Optional;
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

    public static enum BarSprite {
        FILL {
            public String getPath() {
                return "filled/";
            }
        },
        BACKGROUND {
            public String getPath() {
                return "background/";
            }
        };

        private Optional<ResourceLocation> cached = Optional.empty();
        private int cached_i = -1;

        public abstract String getPath();

        public ResourceLocation cacheRead(final int maxStamina) {
            if (maxStamina != cached_i)
                return writeCache(maxStamina);
            return cached.orElseGet(() -> writeCache(maxStamina));
        }

        private ResourceLocation writeCache(final int maxStamina) {
            final String path = StaminaSprites.PATH_PREFIX
                    + getPath()
                    + String.valueOf(maxStamina);
            final ResourceLocation sprite = ResourceLocation.fromNamespaceAndPath(
                MinecraftJourneys.MOD_ID,
                path);
            cached_i = maxStamina;
            return sprite;
        }
    }
}