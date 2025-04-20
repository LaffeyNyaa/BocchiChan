package com.laffeynyaa.bocchichan.renderer;

import com.laffeynyaa.bocchichan.BocchiChan;
import com.laffeynyaa.bocchichan.BocchiChanClient;
import com.laffeynyaa.bocchichan.entity.BocchiEntity;
import com.laffeynyaa.bocchichan.model.BocchiEntityModel;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class BocchiEntityRenderer extends MobEntityRenderer<BocchiEntity, BocchiEntityModel> {
    public BocchiEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new BocchiEntityModel(context.getPart(BocchiChanClient.MODEL_BOCCHI_LAYER)), 0.5f);
    }

    @Override
    public Identifier getTexture(BocchiEntity entity) {
        return Identifier.of("bocchichan", "textures/entity/bocchi/bocchi.png");
    }

    public static void register() {
        EntityRendererRegistry.register(BocchiChan.BOCCHI, (context) -> {
            return new BocchiEntityRenderer(context);
        });
    }
}
