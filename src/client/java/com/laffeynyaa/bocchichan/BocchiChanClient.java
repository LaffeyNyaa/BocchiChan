package com.laffeynyaa.bocchichan;

import com.laffeynyaa.bocchichan.entity.BocchiEntity;
import com.laffeynyaa.bocchichan.model.BocchiEntityModel;
import com.laffeynyaa.bocchichan.network.NetworkingConstants;
import com.laffeynyaa.bocchichan.renderer.BocchiEntityRenderer;
import com.laffeynyaa.bocchichan.screen.BocchiHandledScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
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

        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.BOCCHI_IS_SITTING_PACKET_ID, (client, handler, buf, responseSender) -> {
            BocchiEntity bocchiEntity = (BocchiEntity) client.world.getEntityById(buf.readInt());
            if (bocchiEntity != null) {
                bocchiEntity.setSitting(true);
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.BOCCHI_IS_NOT_SITTING_PACKET_ID, (client, handler, buf, responseSender) -> {
            BocchiEntity bocchiEntity = (BocchiEntity) client.world.getEntityById(buf.readInt());
            if (bocchiEntity != null) {
                bocchiEntity.setSitting(false);
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.BOCCHI_HAS_EFFECT_PACKET_ID, (client, handler, buf, responseSender) -> {
            BocchiEntity bocchiEntity = (BocchiEntity) client.world.getEntityById(buf.readInt());
            if (bocchiEntity != null) {
                bocchiEntity.hasEffect = true;
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.BOCCHI_HAS_NO_EFFECT_PACKET_ID, (client, handler, buf, responseSender) -> {
            BocchiEntity bocchiEntity = (BocchiEntity) client.world.getEntityById(buf.readInt());
            if (bocchiEntity != null) {
                bocchiEntity.hasEffect = false;
            }
        });
    }
}
