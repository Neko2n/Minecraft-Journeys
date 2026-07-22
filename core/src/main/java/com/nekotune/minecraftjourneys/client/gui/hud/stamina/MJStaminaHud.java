package com.nekotune.minecraftjourneys.client.gui.hud.stamina;

import java.util.Optional;

import com.mojang.blaze3d.systems.RenderSystem;
import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.client.gui.hud.MJHudLayer;
import com.nekotune.minecraftjourneys.shared.logic.stamina.PlayerStamina;
import com.nekotune.minecraftjourneys.client.gui.GuiUtils;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(value = Dist.CLIENT)
public class MJStaminaHud extends MJHudLayer {
    private static final ResourceLocation STAMINA_HUD = ResourceLocation.fromNamespaceAndPath(MinecraftJourneys.MOD_ID, "hud/stamina");
    
    private static final int BASE_IMG_WIDTH = 61;
    private static final int WIDTH_PER_STAMINA = 4;
    private static final int IMG_HEIGHT = 5;
    private static final class Colors {
        public static final int FULL = FastColor.ARGB32.opaque(0xFF40F5A0);
        public static final int DELTA_POSITIVE = Colors.FULL;
        public static final int DELTA_NEGATIVE = FastColor.ARGB32.opaque(0xFFFFA0A0);
    }

    public static Optional<MJStaminaHud> instance = Optional.empty(); 

    protected final Minecraft minecraft;
    protected final DeltaAnimation deltaAnimation = new DeltaAnimation();

    public MJStaminaHud() {
        minecraft = Minecraft.getInstance();
        MJStaminaHud.instance = Optional.of(this);
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return STAMINA_HUD;
    }

    @Override
    public boolean isVisible() {
        final PlayerStamina playerStamina = PlayerStamina.get(minecraft.player);
        final int maxStamina = playerStamina.getMaxValue();
        return super.isVisible()
                && maxStamina > 0
                && this.minecraft.player.jumpableVehicle() == null
                && this.minecraft.gameMode.hasExperience();
    }

    @Override
    protected final void render(final GuiGraphics guiGraphics, final DeltaTracker deltaTracker) {
        this.minecraft.getProfiler().push("staminaHud");
        final var playerStamina = PlayerStamina.get(this.minecraft.player);

        // Render the bar and fill
        final BarRenderResult bar = renderBar(guiGraphics, deltaTracker, playerStamina);

        // Render the delta overlay
        if (this.deltaAnimation.shouldStart) {
            this.deltaAnimation.shouldStart = false;
            this.deltaAnimation.elapsedTicks = 0f;
        }
        if (this.deltaAnimation.elapsedTicks < this.deltaAnimation.tickLength) {
            this.deltaAnimation.elapsedTicks += deltaTracker.getGameTimeDeltaTicks();
            renderDeltaAnimation(guiGraphics, deltaTracker, playerStamina, bar);
        }
        
        // TODO: Render the cycle overlay

        // TODO: Render item weight penalties

        RenderSystem.disableBlend();
        this.minecraft.getProfiler().pop();
    }

    /**
     * Render the bar (background and fill)
     */
    protected BarRenderResult renderBar(final GuiGraphics guiGraphics,
            final DeltaTracker deltaTracker, final PlayerStamina stamina) {
        GuiUtils.setColor(guiGraphics, Colors.FULL);

        // Calculate positions and sizes
        final int maxStamina = stamina.getMaxValue();
        final int imgWidth = BASE_IMG_WIDTH + WIDTH_PER_STAMINA * (maxStamina - 1);
        final int barX = (guiGraphics.guiWidth() - imgWidth) / 2;
        final int barY = guiGraphics.guiHeight() - 32 + IMG_HEIGHT;
        final float fillProportion = stamina.getValue() / ((float) maxStamina);
        final int fillWidth = (int)(fillProportion * ((float) imgWidth + 1f));

        // Fetch sprites
        final ResourceLocation barBackground = SpriteHelper
                .fromCache(SpriteHelper.SpriteType.BAR_BACKGROUND, maxStamina);
        final ResourceLocation barFill = SpriteHelper
                .fromCache(SpriteHelper.SpriteType.BAR_FILL, maxStamina);

        // Render
        guiGraphics.blitSprite(barBackground,
                barX, barY,
                imgWidth, IMG_HEIGHT);
        if (fillWidth > 0) {
            guiGraphics.blitSprite(barFill,
                    imgWidth, IMG_HEIGHT,
                    0, 0,
                    barX, barY,
                    fillWidth, IMG_HEIGHT);
        }
        GuiUtils.clearColor(guiGraphics);
        return new BarRenderResult(barX, barY, imgWidth, IMG_HEIGHT, fillWidth,
                barBackground, barFill);
    }

    /**
     * Render the delta animation
     */
    protected void renderDeltaAnimation(final GuiGraphics guiGraphics,
            final DeltaTracker deltaTracker, final PlayerStamina stamina,
            final BarRenderResult bar) {
        
        // Calculate width
        final int maxStamina = stamina.getMaxValue();
        final int imgWidth = BASE_IMG_WIDTH + WIDTH_PER_STAMINA * (maxStamina - 1);
        final float deltaProportion = Math.abs(this.deltaAnimation.deltaStamina) / ((float) maxStamina);
        final float alpha = (float)Math.pow(
                this.deltaAnimation.elapsedTicks / this.deltaAnimation.tickLength,
                DeltaAnimation.CURVE);
        int width = (int)((1f - alpha) * (deltaProportion * ((float) (imgWidth - 2) + 1f)));

        // Calculate position
        final boolean positive = this.deltaAnimation.deltaStamina >= 0;
        int x = bar.posX + bar.fillWidth - (positive ? width : 0);

        // Adjust position and width for borders
        if (x == bar.posX) {
            x++;
            width--;
        }
        if (x + width == bar.posX + imgWidth) {
            width--;
        }

        // Render
        final int color = positive ? Colors.DELTA_POSITIVE : Colors.DELTA_NEGATIVE;
        GuiUtils.setColor(guiGraphics, color);
        guiGraphics.blitSprite(SpriteHelper.DELTA_SPRITE,
                x, bar.posY,
                width, IMG_HEIGHT);
        GuiUtils.clearColor(guiGraphics);
    }

    protected static final record BarRenderResult (
        int posX,
        int posY,
        int barWidth,
        int barHeight,
        int fillWidth,
        ResourceLocation backgroundSprite,
        ResourceLocation fillSprite
    ) {}

    public static final class DeltaAnimation {
        private DeltaAnimation() {}
        public static final int CURVE = 6;
        public float tickLength = 0f;
        public float deltaStamina = 0f;
        public boolean shouldStart = false;
        public float elapsedTicks = 0f;
    }
}
