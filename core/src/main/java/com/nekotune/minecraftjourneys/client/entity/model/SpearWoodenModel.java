package com.nekotune.minecraftjourneys.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.nekotune.minecraftjourneys.shared.definition.entity.projectile.ThrownSpear;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(value = Dist.CLIENT)
public class SpearWoodenModel extends EntityModel<ThrownSpear> {
    public static final String NAME = "spear_wooden";

    private final ModelPart spear;

    public SpearWoodenModel(ModelPart root) {
        super(RenderType::entitySolid);
        this.spear = root.getChild(NAME);
    }

    public static LayerDefinition createBodyLayer() {
        // TODO
    }

    @Override
    public void setupAnim(ThrownSpear entity, float limbSwing, float limbSwingAmount, float ageInTicks,
            float netHeadYaw, float headPitch) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setupAnim'");
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay,
            int color) {
        spear.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}
