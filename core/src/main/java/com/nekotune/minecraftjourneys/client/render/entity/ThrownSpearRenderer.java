package com.nekotune.minecraftjourneys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.nekotune.minecraftjourneys.shared.definitions.entity.projectile.ThrownSpear;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ThrownSpearRenderer extends EntityRenderer<ThrownSpear> {

    private final ItemRenderer itemRenderer;

    public ThrownSpearRenderer(Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    public void render(ThrownSpear entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        // Orientation code from ThrownTridentRenderer - no camera billboarding, the spear
        // keeps facing the direction it's flying in, like an in-world item.
        poseStack.pushPose();
        poseStack.scale(2.0f, 2.0f, 2.0f);
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) + 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(-1.0f * Mth.lerp(partialTicks, entity.xRotO, entity.getXRot()) - 45.0f - 90.0f));
        poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
        poseStack.translate(-0.15f, -0.25f, 0.0f);
        
        itemRenderer.renderStatic(entity.getItem(), ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY,
                poseStack, buffer, entity.level(), entity.getId());
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(ThrownSpear entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
