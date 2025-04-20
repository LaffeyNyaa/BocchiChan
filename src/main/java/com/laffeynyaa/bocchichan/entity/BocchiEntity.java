package com.laffeynyaa.bocchichan.entity;

import com.laffeynyaa.bocchichan.BocchiChan;
import com.laffeynyaa.bocchichan.handler.BocchiScreenHandler;

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
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
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

    public BocchiEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
        this.goalSelector.add(10, new LookAroundGoal(this));
        this.goalSelector.add(10, wanderAroundGoal);
        this.goalSelector.add(10, new FollowOwnerGoal(this, 0.3, 0, 100, true));
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        NamedScreenHandlerFactory namedScreenHandlerFactory = new SimpleNamedScreenHandlerFactory(
                (syncId, playerInventory, playerx) -> {
                    return new BocchiScreenHandler(syncId, playerInventory);
                }, Text.literal(""));

        player.openHandledScreen(namedScreenHandlerFactory);

        return ActionResult.SUCCESS;
    }

    @Override
    public EntityView method_48926() {
        return this.getWorld();
    }

    @Override
    public PassiveEntity createChild(ServerWorld arg0, PassiveEntity arg1) {
        return null;
    }

    @Override
    public void tick() {
        LivingEntity closestPlayer = this.getWorld().getClosestPlayer(this, 5);
        LivingEntity owner = this.getOwner();

        super.tick();

        if (this.getAttacker() != null) {
            this.goalSelector.add(1, escapeGoal);
        } else {
            this.goalSelector.remove(escapeGoal);
        }

        if (closestPlayer != null) {
            closestPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.LUCK, 500));
        }

        if (owner != null && this.distanceTo(owner) <= 5) {
            owner.addStatusEffect(new StatusEffectInstance(StatusEffects.LUCK, 500));
            owner.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 500));
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
        return this.goalSelector;
    }
}
