package com.nekotune.minecraftjourneys.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.FastColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(value = Dist.CLIENT)
public final class GuiUtils {
    public static void setColor(final GuiGraphics guiGraphics, final int packedColor) {
        guiGraphics.setColor(
            FastColor.ARGB32.red(packedColor) / 255f,
            FastColor.ARGB32.green(packedColor) / 255f,
            FastColor.ARGB32.blue(packedColor) / 255f,
            FastColor.ARGB32.alpha(packedColor) / 255f
        );
    }
    public static void clearColor(final GuiGraphics guiGraphics) {
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
