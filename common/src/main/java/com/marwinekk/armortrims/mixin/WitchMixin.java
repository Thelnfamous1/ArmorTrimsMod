package com.marwinekk.armortrims.mixin;

import com.marwinekk.armortrims.ArmorTrimsMod;
import com.marwinekk.armortrims.ducks.WitchDuck;
import com.marwinekk.armortrims.entity.DamagelessArrowEntity;
import com.marwinekk.armortrims.entity.ai.CustomFollowOwnerGoal;
import com.marwinekk.armortrims.entity.ai.HealPlayerGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableWitchTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestHealableRaiderTargetGoal;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(Witch.class)
abstract class WitchMixin extends Mob implements WitchDuck {

    @Shadow private NearestAttackableWitchTargetGoal<Player> attackPlayersGoal;
    @Shadow private NearestHealableRaiderTargetGoal<Raider> healRaidersGoal;
    @Nullable
    private UUID ownerUUID;

    protected WitchMixin(EntityType<? extends Mob> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Inject(method = "registerGoals",at = @At("RETURN"))
    private void addCustomGoals(CallbackInfo ci) {
        this.targetSelector.addGoal(2,new HealPlayerGoal<>(this, Player.class,10, true, false, null));
        this.goalSelector.addGoal(3,new CustomFollowOwnerGoal(this, 1, 10, 2, false));
        reassignGoals();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag $$0) {
        super.addAdditionalSaveData($$0);
        if (ownerUUID != null) {
            $$0.putUUID("owner",ownerUUID);
        }
        reassignGoals();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag $$0) {
        super.readAdditionalSaveData($$0);
        if ($$0.hasUUID("owner")) {
            ownerUUID = $$0.getUUID("owner");
        }
        reassignGoals();
    }

    /*@ModifyVariable(method = "performRangedAttack",at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/projectile/ThrownPotion;<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;)V"),ordinal = 0)
    private Potion modifyPotion(Potion original,LivingEntity target,float damage) {
        if (target.getUUID().equals(ownerUUID)) {
            if (target.getHealth() <= 4.0F) {
                original = Potions.HEALING;
            } else {
                original = Potions.REGENERATION;
            }
            setTarget(null);
        }
        return original;
    }*/

    @Inject(method = "performRangedAttack",at = @At("HEAD"),cancellable = true)
    private void hijackAttack(LivingEntity pTarget, float pDistanceFactor, CallbackInfo ci) {
        if(ownerUUID != null){
            ci.cancel();
            DamagelessArrowEntity arrow = new DamagelessArrowEntity(this,this.level());
            double xDist = pTarget.getX() - this.getX();
            double yDist = pTarget.getY(1/3D) - arrow.getY();
            double zDist = pTarget.getZ() - this.getZ();
            double xzDist = Math.sqrt(xDist * xDist + zDist * zDist);

            ItemStack stack = new ItemStack(Items.TIPPED_ARROW);
            Potion potion = Potions.HARMING;
            if (ArmorTrimsMod.isOwnerOrOwnerAlly(this, pTarget)) {
                if (pTarget.getHealth() <= 4.0F) {
                    potion = Potions.HEALING;
                } else {
                    potion = Potions.REGENERATION;
                }

                this.setTarget(null);
            } else if (xzDist >= 8.0 && !pTarget.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
                potion = Potions.SLOWNESS;
            } else if (pTarget.getHealth() >= 8.0F && !pTarget.hasEffect(MobEffects.POISON)) {
                potion = Potions.POISON;
            } else if (xzDist <= 3.0 && !pTarget.hasEffect(MobEffects.WEAKNESS) && this.random.nextFloat() < 0.25F) {
                potion = Potions.WEAKNESS;
            }
            PotionUtils.setPotion(stack, potion);
            arrow.setEffectsFromItem(stack);

            arrow.shoot(xDist, yDist + xzDist * (double)0.2F, zDist, 1.6F, (float)(14 - this.level().getDifficulty().getId() * 4));
//            this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level().addFreshEntity(arrow);
        }
    }

    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/raid/Raider;aiStep()V"))
    private void handleAiStep(CallbackInfo ci){
        if (!this.level().isClientSide && this.isAlive()) {
            LivingEntity owner = this.getOwner();
            if(owner != null && owner.isDeadOrDying()){
                this.kill();
            }
        }
    }

    private void reassignGoals() {
        if (this.ownerUUID != null) {
            this.targetSelector.removeGoal(this.healRaidersGoal);
            this.targetSelector.removeGoal(this.attackPlayersGoal);
            this.attackPlayersGoal = new NearestAttackableWitchTargetGoal<>((Witch)(Object)this, Player.class, 10, true, false, target -> !ArmorTrimsMod.isOwnerOrOwnerAlly(this, target));
            this.targetSelector.addGoal(3, this.attackPlayersGoal);
        }
    }

    @Nullable
    @Override
    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    @Override
    public void setOwnerUUID(UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
        reassignGoals();
    }
}
