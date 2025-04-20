package com.laffeynyaa.bocchichan;

import com.laffeynyaa.bocchichan.model.BocchiEntityModel;
import com.laffeynyaa.bocchichan.renderer.BocchiEntityRenderer;
import com.laffeynyaa.bocchichan.screen.BocchiHandledScreen;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class BocchiChanClient implements ClientModInitializer {
    public static final EntityModelLayer MODEL_BOCCHI_LAYER = new EntityModelLayer(
                Identifier.of("bocchichan", "bocchi"),
                "main");

    @Override
    public void onInitializeClient() {
        BocchiEntityRenderer.register();
        BocchiEntityModel.register();
        BocchiHandledScreen.register();
    }
}
