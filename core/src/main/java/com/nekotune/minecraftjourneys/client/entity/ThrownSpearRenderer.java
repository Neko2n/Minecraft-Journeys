package com.nekotune.minecraftjourneys.client.entity;

import java.util.IdentityHashMap;
import java.util.Map;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.client.entity.model.SpearGenericModel;
import com.nekotune.minecraftjourneys.client.entity.model.SpearWoodenModel;
import com.nekotune.minecraftjourneys.shared.definition.entity.projectile.ThrownSpear;
import com.nekotune.minecraftjourneys.shared.registry.content.MJItems;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ThrownSpearRenderer extends EntityRenderer<ThrownSpear> {

    private final Map<Item, EntityModel<ThrownSpear>> modelsByItem = new IdentityHashMap<>();
    private final ModelPart genericModelPart;
    private final ModelPart woodenModelPart;

    public ThrownSpearRenderer(Context context) {
        super(context);

        // Wooden spears have a different model
        final ModelLayerLocation genericLocation = new ModelLayerLocation(
                ResourceLocation.fromNamespaceAndPath(
                        MinecraftJourneys.MOD_ID, SpearGenericModel.NAME),
                "main");
        final ModelLayerLocation woodenLocation = new ModelLayerLocation(
                ResourceLocation.fromNamespaceAndPath(
                        MinecraftJourneys.MOD_ID, SpearWoodenModel.NAME),
                "main");
        genericModelPart = context.bakeLayer(genericLocation);
        woodenModelPart = context.bakeLayer(woodenLocation);
    }

    public void render(ThrownSpear entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        EntityModel<ThrownSpear> model = modelsByItem.computeIfAbsent(entity.getItem().getItem(), item ->
                item == MJItems.Equipment.WOODEN_SPEAR.get()
                ? new SpearWoodenModel(woodenModelPart)
                : new SpearGenericModel(genericModelPart));
        
        // Render code from ThrownTridentRenderer
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot()) + 90.0F));
        VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(
            buffer, model.renderType(this.getTextureLocation(entity)), false, entity.isFoil()
        );
        model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(ThrownSpear entity) {

        // Each spear has its own texture
        Item spearItem = entity.getItem().getItem();
        ResourceLocation location = BuiltInRegistries.ITEM.getKey(spearItem);
        return ResourceLocation.fromNamespaceAndPath(location.getNamespace(),
            "textures/entity/spear/" + location.getPath() + ".png");
    }
}
