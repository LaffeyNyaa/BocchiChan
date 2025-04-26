package com.laffeynyaa.bocchichan;

import com.laffeynyaa.bocchichan.entity.BocchiEntity;
import com.laffeynyaa.bocchichan.handler.BocchiScreenHandler;
import com.laffeynyaa.bocchichan.network.NetworkingConstants;
import com.laffeynyaa.bocchichan.state.StateSaverAndLoader;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class BocchiChan implements ModInitializer {
    public static final String MOD_ID = "bocchichan";
    public static final EntityType<BocchiEntity> BOCCHI = BocchiEntity.register();
    public static final Item BOCCHI_SPAWN_EGG = new SpawnEggItem(BOCCHI, 0xc4c4c4, 0xadadad, new FabricItemSettings());
    public static final ScreenHandlerType<BocchiScreenHandler> BOCCHI_SCREEN_HANDLER = BocchiScreenHandler.register();

    @Override
    public void onInitialize() {
        FabricDefaultAttributeRegistry.register(BOCCHI, BocchiEntity.createMobAttributes());
        Registry.register(Registries.ITEM, new Identifier("bocchichan", "bocchi_spawn_egg"), BOCCHI_SPAWN_EGG);

        ServerPlayNetworking.registerGlobalReceiver(NetworkingConstants.BOCCHI_FOLLOW_PACKET_ID,
                (server, player, handler, buf, responseSender) -> {
                    BocchiEntity bocchiEntity = (BocchiEntity) server
                            .getWorld(World.OVERWORLD)
                            .getEntityById(buf.readInt());
                    if (bocchiEntity.getOwner() == null) {
                        bocchiEntity.setOwner(player);
                    }
                });

        ServerPlayNetworking.registerGlobalReceiver(NetworkingConstants.BOCCHI_SIT_PACKET_ID,
                (server, player, handler, buf, responseSender) -> {
                    BocchiEntity bocchiEntity = (BocchiEntity) server
                            .getWorld(World.OVERWORLD)
                            .getEntityById(buf.readInt());

                    if (bocchiEntity.isSitting()) {
                        bocchiEntity.setSitting(false);
                        bocchiEntity.getGoalSelector().add(10, bocchiEntity.wanderAroundGoal);
                    } else {
                        bocchiEntity.setSitting(true);
                        bocchiEntity.getGoalSelector().remove(bocchiEntity.wanderAroundGoal);
                    }
                });

        ServerPlayNetworking.registerGlobalReceiver(NetworkingConstants.BOCCHI_EFFECT_PACKET_ID,
                (server, player, handler, buf, responseSender) -> {
                    BocchiEntity bocchiEntity = (BocchiEntity) server
                            .getWorld(World.OVERWORLD)
                            .getEntityById(buf.readInt());

                    if (bocchiEntity.hasEffect) {
                        bocchiEntity.hasEffect = false;
                    } else {
                        bocchiEntity.hasEffect = true;
                    }

                    StateSaverAndLoader serverState = StateSaverAndLoader.getServerState(server);
                    serverState.hashMap.put(bocchiEntity.getUuid().toString(), bocchiEntity.hasEffect);
                });
    }
}
