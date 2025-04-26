package com.laffeynyaa.bocchichan.entity;

import java.io.IOException;
import java.util.UUID;

import com.laffeynyaa.bocchichan.BocchiChan;
import com.laffeynyaa.bocchichan.handler.BocchiScreenHandler;
import com.laffeynyaa.bocchichan.network.NetworkingConstants;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.EntityView;
import net.minecraft.world.World;

public class BocchiEntity extends TameableEntity {
    public Goal escapeGoal = new FollowOwnerGoal(this, 0.5, 0, 100, true);
    public Goal wanderAroundGoal = new WanderAroundGoal(this, 0.2);
    public boolean hasEffect;
    public UUID tempUuid;

    public BocchiEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);

        if (!world.isClient) {
            goalSelector.add(10, new LookAroundGoal(this));
            goalSelector.add(10, wanderAroundGoal);
            goalSelector.add(10, new FollowOwnerGoal(this, 0.3, 0, 100, true));
            tempUuid = uuid;
        }

    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (!getWorld().isClient) {
            NamedScreenHandlerFactory namedScreenHandlerFactory = new SimpleNamedScreenHandlerFactory(
                    (syncId, playerInventory, playerx) -> {
                        return new BocchiScreenHandler(syncId, playerInventory);
                    }, Text.literal(""));

            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeInt(getId());

            if (isSitting()) {
                ServerPlayNetworking.send((ServerPlayerEntity) player, NetworkingConstants.BOCCHI_IS_SITTING_PACKET_ID,
                        buf);
            } else {
                ServerPlayNetworking.send((ServerPlayerEntity) player,
                        NetworkingConstants.BOCCHI_IS_NOT_SITTING_PACKET_ID, buf);
            }

            buf.writeInt(getId());

            if (hasEffect) {
                ServerPlayNetworking.send((ServerPlayerEntity) player, NetworkingConstants.BOCCHI_HAS_EFFECT_PACKET_ID,
                        buf);
            } else {
                ServerPlayNetworking.send((ServerPlayerEntity) player,
                        NetworkingConstants.BOCCHI_HAS_NO_EFFECT_PACKET_ID,
                        buf);
            }

            player.openHandledScreen(namedScreenHandlerFactory);
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public EntityView method_48926() {
        return getWorld();
    }

    @Override
    public PassiveEntity createChild(ServerWorld arg0, PassiveEntity arg1) {
        return null;
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.getWorld().isClient) {
            LivingEntity closestPlayer = getWorld().getClosestPlayer(this, 5);
            LivingEntity owner = getOwner();

            if (getAttacker() != null) {
                goalSelector.add(1, escapeGoal);
            } else {
                goalSelector.remove(escapeGoal);
            }

            if (hasEffect) {

                if (closestPlayer != null) {
                    closestPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.LUCK, 500));
                }

                if (owner != null && distanceTo(owner) <= 5) {
                    owner.addStatusEffect(new StatusEffectInstance(StatusEffects.LUCK, 500));
                    owner.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 500));
                }
            }

            if (getUuid() != tempUuid) {
                ServerWorld serverWorld = (ServerWorld) getWorld();

                try {
                    NbtCompound nbt = serverWorld.getPersistentStateManager().readNbt(BocchiChan.MOD_ID, 3465)
                            .getCompound("data");
                    if (nbt.contains(getUuidAsString())) {
                        hasEffect = nbt.getBoolean(getUuidAsString());
                    }
                } catch (IOException e) {

                }

                tempUuid = getUuid();
            }
        }

    }

    public static EntityType<BocchiEntity> register() {
        Identifier identifier = Identifier.of(BocchiChan.MOD_ID, "bocchi");
        EntityType<BocchiEntity> entityType = EntityType.Builder
                .create(BocchiEntity::new, SpawnGroup.CREATURE)
                .setDimensions(0.3f, 0.3f)
                .build("bocchi");

        return Registry.register(Registries.ENTITY_TYPE, identifier, entityType);
    }

    public GoalSelector getGoalSelector() {
        return goalSelector;
    }
}
