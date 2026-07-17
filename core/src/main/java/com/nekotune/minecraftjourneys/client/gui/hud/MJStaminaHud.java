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
            final int fill = (int)(proportion * ((float) IMG_WIDTH + 1f));
            final int l = guiGraphics.guiHeight() - 32 + 3;
            final ResourceLocation background = SpriteCache
                    .getSprite(false, maxStamina);
            final ResourceLocation filled = SpriteCache
                    .getSprite(true, maxStamina);
            RenderSystem.enableBlend();
            guiGraphics.blitSprite(background,
                    x, l,
                    IMG_WIDTH, IMG_HEIGHT);
            if (fill > 0) {
                guiGraphics.blitSprite(filled,
                        IMG_WIDTH, IMG_HEIGHT,
                        0, 0,
                        x, l,
                        fill, IMG_HEIGHT);
            }
            RenderSystem.disableBlend();
            this.minecraft.getProfiler().pop();
        }
    }

    private static final class SpriteCache {

        private static ResourceLocation backgroundSprite = null;
        private static ResourceLocation filledSprite = null;
        private static int i = -1;

        public static ResourceLocation getSprite(boolean filled, int maxStamina) {
            if (i != maxStamina) {
                i = maxStamina;
                final String s = String.valueOf(maxStamina);
                filledSprite = ResourceLocation.fromNamespaceAndPath(
                        MinecraftJourneys.MOD_ID, "hud/stamina/filled/" + s);
                backgroundSprite = ResourceLocation.fromNamespaceAndPath(
                        MinecraftJourneys.MOD_ID, "hud/stamina/background/" + s);
            }
            return filled ? filledSprite : backgroundSprite;
        }
    }
}
