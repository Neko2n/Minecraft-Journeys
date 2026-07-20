package com.nekotune.minecraftjourneys.client.gui.hud.stamina;

import java.util.Optional;

import com.mojang.blaze3d.systems.RenderSystem;
import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.client.gui.IGuiLayer;
import com.nekotune.minecraftjourneys.shared.systems.stamina.PlayerStamina;
import com.nekotune.minecraftjourneys.client.gui.GuiUtils;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(value = Dist.CLIENT)
public class MJStaminaHud implements IGuiLayer {
    public static final ResourceLocation STAMINA_HUD = ResourceLocation.fromNamespaceAndPath(MinecraftJourneys.MOD_ID, "hud/stamina");
    
    private static final int IMG_WIDTH = 61;
    private static final int IMG_HEIGHT = 5;
    private static final class Colors {
        public static final int FULL = FastColor.ARGB32.opaque(0xFFFFE864);
        public static final int DELTA_POSITIVE = Colors.FULL;
        public static final int DELTA_NEGATIVE = FastColor.ARGB32.opaque(0xFFFFA0A0);
    }

    public static Optional<MJStaminaHud> instance = Optional.empty(); 

    private final Minecraft minecraft;
    protected final DeltaAnimation deltaAnimation = new DeltaAnimation();

    public MJStaminaHud() {
        minecraft = Minecraft.getInstance();
        MJStaminaHud.instance = Optional.of(this);
    }

    public boolean isStaminaVisible() {
        return !minecraft.player.hasInfiniteMaterials();
    }

    @Override
    public void register(final RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.HOTBAR, STAMINA_HUD, this::maybeRender);
    }

    private void maybeRender(final GuiGraphics guiGraphics, final DeltaTracker deltaTracker) {
        if (this.minecraft.player.jumpableVehicle() == null && this.isStaminaVisible()) {
            final PlayerStamina playerStamina = PlayerStamina.get(minecraft.player);
            final int maxStamina = playerStamina.getMaxValue();
            if (maxStamina > 0) {
                final int x = guiGraphics.guiWidth() / 2 + 30;
                this.render(guiGraphics, deltaTracker, x, playerStamina.getValue(), maxStamina);
            }
        }
    }

    private void render(final GuiGraphics guiGraphics, final DeltaTracker deltaTracker,
            final int barX, final float stamina, final int maxStamina) {
        this.minecraft.getProfiler().push("staminaHud");
        final float proportion = stamina / ((float) maxStamina);
        final int fillWidth = (int)(proportion * ((float) IMG_WIDTH + 1f));
        final int barY = guiGraphics.guiHeight() - 32 + 3;
        final ResourceLocation barBackground = SpriteHelper
                .fromCache(SpriteHelper.SpriteType.BAR_BACKGROUND, maxStamina);
        final ResourceLocation barFill = SpriteHelper
                .fromCache(SpriteHelper.SpriteType.BAR_FILL, maxStamina);
        RenderSystem.enableBlend();

        // Render the bar (background and fill)
        GuiUtils.setColor(guiGraphics, Colors.FULL);
        guiGraphics.blitSprite(barBackground,
                barX, barY,
                IMG_WIDTH, IMG_HEIGHT);
        if (fillWidth > 0) {
            guiGraphics.blitSprite(barFill,
                    IMG_WIDTH, IMG_HEIGHT,
                    0, 0,
                    barX, barY,
                    fillWidth, IMG_HEIGHT);
        }
        GuiUtils.clearColor(guiGraphics);

        // Render the delta overlay
        if (this.deltaAnimation.shouldStart) {
            this.deltaAnimation.shouldStart = false;
            this.deltaAnimation.elapsedTicks = 0f;
            MinecraftJourneys.LOGGER.debug("[Stamina] Started deltaAnimation");
        }
        if (this.deltaAnimation.elapsedTicks < this.deltaAnimation.tickLength) {
            this.deltaAnimation.elapsedTicks += deltaTracker.getGameTimeDeltaTicks();

            // Calculate width
            final float deltaProportion = Math.abs(this.deltaAnimation.deltaStamina) / ((float) maxStamina);
            final float alpha = (float)Math.pow(
                    this.deltaAnimation.elapsedTicks / this.deltaAnimation.tickLength,
                    DeltaAnimation.CURVE);
            int width = (int)((1f - alpha) * (deltaProportion * ((float) (IMG_WIDTH - 2) + 1f)));

            // Calculate position
            final boolean positive = this.deltaAnimation.deltaStamina >= 0;
            int x = barX + fillWidth - (positive ? width : 0);

            // Adjust position and width for borders
            if (x == barX) {
                x++;
                width--;
            }
            if (x + width == barX + IMG_WIDTH) {
                width--;
            }

            // Render
            final int color = positive ? Colors.DELTA_POSITIVE : Colors.DELTA_NEGATIVE;
            GuiUtils.setColor(guiGraphics, color);
            MinecraftJourneys.LOGGER.debug(
                    "[Stamina] maxStamina={} deltaStamina={} deltaProportion={} width={} x={}",
                    maxStamina, this.deltaAnimation.deltaStamina, deltaProportion, width, x);
            guiGraphics.blitSprite(SpriteHelper.DELTA_SPRITE,
                    IMG_WIDTH, IMG_HEIGHT,
                    0, 0,
                    x, barY,
                    width, IMG_HEIGHT);
            GuiUtils.clearColor(guiGraphics);
        }

        RenderSystem.disableBlend();
        this.minecraft.getProfiler().pop();
    }

    static final class DeltaAnimation {
        public static final int CURVE = 6;
        public float tickLength = 0f;
        public float deltaStamina = 0f;
        public boolean shouldStart = false;
        public float elapsedTicks = 0f;
    }
}
