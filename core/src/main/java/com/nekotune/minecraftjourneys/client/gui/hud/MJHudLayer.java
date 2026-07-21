package com.nekotune.minecraftjourneys.client.gui.hud;

import com.nekotune.minecraftjourneys.client.gui.IGuiLayer;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

public abstract class MJHudLayer implements IGuiLayer {
    private final Minecraft minecraft;

    public MJHudLayer() {
        minecraft = Minecraft.getInstance();
    }

    public abstract ResourceLocation getResourceLocation();

    protected abstract void render(final GuiGraphics guiGraphics, final DeltaTracker deltaTracker);
    
    public boolean isVisible() {
        return !this.minecraft.options.hideGui;
    }

    @Override
    public final void register(final RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.HOTBAR, getResourceLocation(), this::maybeRender);
    }

    private void maybeRender(final GuiGraphics guiGraphics, final DeltaTracker deltaTracker) {
        if (this.isVisible()) this.render(guiGraphics, deltaTracker);
    }
}
