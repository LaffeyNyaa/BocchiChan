package com.laffeynyaa.bocchichan.model;

import com.laffeynyaa.bocchichan.BocchiChanClient;
import com.laffeynyaa.bocchichan.entity.BocchiEntity;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class BocchiEntityModel extends EntityModel<BocchiEntity> {
    private final ModelPart hairpin2;

    public BocchiEntityModel(ModelPart root) {
        hairpin2 = root.getChild("hairpin2");
    }

    @SuppressWarnings("unused")
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData hairpin2 = modelPartData.addChild("hairpin2",
                ModelPartBuilder.create().uv(0, 0)
                        .cuboid(-23.0F, -25.0F, -22.0F, 45.0F, 45.0F, 45.0F, new Dilation(-20.0F))
                        .uv(0, 90).cuboid(-1.0F, -8.0F, -1.0F, 7.0F, 7.0F, 7.0F, new Dilation(-3.0F))
                        .uv(28, 90).cuboid(-1.0F, -7.0F, -1.0F, 7.0F, 7.0F, 7.0F, new Dilation(-3.0F)),
                ModelTransform.of(0.0F, 24.0F, 0.0F, 0.0F, 3.1416F, 0.0F));
        return TexturedModelData.of(modelData, 256, 256);
    }

    @Override
    public void setAngles(BocchiEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks,
            float netHeadYaw, float headPitch) {
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red,
            float green, float blue, float alpha) {
        hairpin2.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    public static void register() {
        EntityModelLayerRegistry.registerModelLayer(BocchiChanClient.MODEL_BOCCHI_LAYER,
                BocchiEntityModel::getTexturedModelData);
    }
}
