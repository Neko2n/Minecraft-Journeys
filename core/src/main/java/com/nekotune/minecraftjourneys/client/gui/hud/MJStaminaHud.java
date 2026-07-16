package com.nekotune.minecraftjourneys.client.gui.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.client.gui.IGuiLayer;
import com.nekotune.minecraftjourneys.shared.systems.PlayerStamina;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

public class MJStaminaHud implements IGuiLayer {
    public static final ResourceLocation STAMINA_HUD = ResourceLocation.fromNamespaceAndPath(MinecraftJourneys.MOD_ID, "hud/stamina");
    
    private static final int IMG_WIDTH = 61;
    private static final int IMG_HEIGHT = 5;

    private final Minecraft minecraft;

    public MJStaminaHud() {
        minecraft = Minecraft.getInstance();
    }

    public boolean isStaminaVisible() {
        return !minecraft.player.hasInfiniteMaterials();
    }

    @Override
    public void register(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.HOTBAR, STAMINA_HUD, this::maybeRender);
    }

    private void maybeRender(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        final int x = guiGraphics.guiWidth() / 2 + 30;
        if (this.minecraft.player.jumpableVehicle() == null && this.isStaminaVisible()) {
            this.render(guiGraphics, deltaTracker, x);
        }
    }

    private void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker, int x) {
        final PlayerStamina playerStamina = PlayerStamina.get(minecraft.player);
        final int maxStamina = playerStamina.getMax();
        if (maxStamina > 0) {
            this.minecraft.getProfiler().push("staminaHud");
            final float stamina = playerStamina.get();
            final float proportion = stamina / ((float) maxStamina);
            final int k = (int)(proportion * ((float) IMG_WIDTH + 1f));
            final int l = guiGraphics.guiHeight() - 32 + 3;
            final ResourceLocation background = SpriteManager
                    .getSprite(false, maxStamina);
            final ResourceLocation filled = SpriteManager
                    .getSprite(true, maxStamina);
            RenderSystem.enableBlend();
            guiGraphics.blitSprite(background,
                    x, l,
                    IMG_WIDTH, IMG_HEIGHT);
            if (k > 0) {
                guiGraphics.blitSprite(filled,
                        IMG_WIDTH, IMG_HEIGHT,
                        0, 0,
                        x, l,
                        k, IMG_HEIGHT);
            }
            RenderSystem.disableBlend();
            this.minecraft.getProfiler().pop();
        }
    }

    private static final class SpriteManager {

        private static ResourceLocation[] cacheBackground = new ResourceLocation[10];
        private static ResourceLocation[] cacheFilled = new ResourceLocation[10];

        public static ResourceLocation getSprite(boolean filled, Integer maxStamina) {
            final String path = filled ? "filled/" : "background/";
            final ResourceLocation[] cache = filled ? cacheFilled : cacheBackground;
            if (cache.length < maxStamina) {
                cacheFilled = new ResourceLocation[maxStamina];
                cacheBackground = new ResourceLocation[maxStamina];
            }
            ResourceLocation resource = cache[maxStamina - 1];
            if (resource == null) {
                resource = ResourceLocation.fromNamespaceAndPath(
                        MinecraftJourneys.MOD_ID, "hud/stamina/" + path + String.valueOf(maxStamina));
                cache[maxStamina - 1] = resource;
            }
            return resource;
        }
    }
}
