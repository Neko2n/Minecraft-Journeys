package com.nekotune.minecraftjourneys.client.gui.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.client.gui.IGuiLayer;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(value = Dist.CLIENT)
public class MJExperienceHud implements IGuiLayer {
    public static final ResourceLocation EXPERIENCE_BAR = ResourceLocation.fromNamespaceAndPath(MinecraftJourneys.MOD_ID, "hud/experience/bar");
    public static final ResourceLocation EXPERIENCE_LEVEL = ResourceLocation.fromNamespaceAndPath(MinecraftJourneys.MOD_ID, "hud/experience/number");
    
    private static final class Sprites {
        private static final ResourceLocation BAR_FILLED = ResourceLocation.fromNamespaceAndPath(
                MinecraftJourneys.MOD_ID, "hud/experience/filled");
        private static final ResourceLocation BAR_BACKGROUND = ResourceLocation.fromNamespaceAndPath(
                MinecraftJourneys.MOD_ID, "hud/experience/background");
    }
    private static final int IMG_WIDTH = 61;
    private static final int IMG_HEIGHT = 5;

    private final Minecraft minecraft;
    
    public MJExperienceHud() {
        minecraft = Minecraft.getInstance();
    }

    public boolean isExperienceVisible() {
        return (!this.minecraft.player.hasInfiniteMaterials())
                && (this.minecraft.player.jumpableVehicle() == null);
    }

	@Override
	public void register(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.HOTBAR, EXPERIENCE_BAR, this::maybeRenderBar);
        event.registerAbove(EXPERIENCE_BAR, EXPERIENCE_LEVEL, this::maybeRenderNumber);
	}

    private void maybeRenderBar(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (!this.isExperienceVisible()) return;
        final int x = guiGraphics.guiWidth() / 2;
        this.renderBar(guiGraphics, deltaTracker, x - 91);
    }

    private void maybeRenderNumber(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (!this.isExperienceVisible()) return;
        final int level = minecraft.player.experienceLevel;
        if (level <= 0) return;
        final int x = guiGraphics.guiWidth() / 2;
        this.renderNumber(guiGraphics, deltaTracker, x - 80);
    }

    /**
     * Render the experience level number text
     */
    private void renderNumber(GuiGraphics guiGraphics, DeltaTracker deltaTracker, int x) {
        this.minecraft.getProfiler().push("expLevel");
        final int level = minecraft.player.experienceLevel;
        String s = String.valueOf(level);
        x -= this.minecraft.font.width(s) / 2;
        int k = guiGraphics.guiHeight() - 31 - 4;
        guiGraphics.drawString(this.minecraft.font, s, x + 1, k, 0, false);
        guiGraphics.drawString(this.minecraft.font, s, x - 1, k, 0, false);
        guiGraphics.drawString(this.minecraft.font, s, x, k + 1, 0, false);
        guiGraphics.drawString(this.minecraft.font, s, x, k - 1, 0, false);
        guiGraphics.drawString(this.minecraft.font, s, x, k, 8453920, false);
        this.minecraft.getProfiler().pop();
    }

    /**
     * Render the experience bar
     */
    private void renderBar(GuiGraphics guiGraphics, DeltaTracker deltaTracker, int x) {
        this.minecraft.getProfiler().push("expBar");
        final float progress = minecraft.player.experienceProgress;
        final int k = (int)(progress * ((float) IMG_WIDTH + 1f));
        final int l = guiGraphics.guiHeight() - 32 + 3;
        RenderSystem.enableBlend();
        guiGraphics.blitSprite(Sprites.BAR_BACKGROUND,
                x, l,
                IMG_WIDTH, IMG_HEIGHT);
        if (k > 0) {
            guiGraphics.blitSprite(Sprites.BAR_FILLED,
                    IMG_WIDTH, IMG_HEIGHT,
                    0, 0,
                    x, l,
                    k, IMG_HEIGHT);
        }
        RenderSystem.disableBlend();
        this.minecraft.getProfiler().pop();
    }
}
