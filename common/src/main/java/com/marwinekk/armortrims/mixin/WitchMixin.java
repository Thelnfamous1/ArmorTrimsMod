package com.marwinekk.armortrims.mixin;

import com.marwinekk.armortrims.ducks.WitchDuck;
import com.marwinekk.armortrims.entity.DamagelessArrowEntity;
import com.marwinekk.armortrims.entity.ai.CustomFollowOwnerGoal;
import com.marwinekk.armortrims.entity.ai.HealPlayerGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableWitchTargetGoal;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
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
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(Witch.class)
abstract class WitchMixin extends Mob implements WitchDuck {

    @Shadow private NearestAttackableWitchTargetGoal<Player> attackPlayersGoal;
    @Nullable
    private UUID ownerUUID;

    protected WitchMixin(EntityType<? extends Mob> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Inject(method = "registerGoals",at = @At("RETURN"))
    private void addCustomGoals(CallbackInfo ci) {
        this.targetSelector.addGoal(2,new HealPlayerGoal<>(this, Player.class,10,true,false,null));
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
        if (pTarget.getUUID().equals(ownerUUID)) {
            ItemStack itemstack = new ItemStack(Items.TIPPED_ARROW);
            PotionUtils.setPotion(itemstack,Potions.STRONG_HEALING);
            DamagelessArrowEntity abstractarrow = new DamagelessArrowEntity(this,this.level());
            abstractarrow.setEffectsFromItem(itemstack);
            double d0 = pTarget.getX() - this.getX();
            double d1 = pTarget.getY(1/3D) - abstractarrow.getY();
            double d2 = pTarget.getZ() - this.getZ();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            abstractarrow.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.6F, (float)(14 - this.level().getDifficulty().getId() * 4));
//            this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level().addFreshEntity(abstractarrow);
            ci.cancel();
        }
    }

    private void reassignGoals() {
        if (ownerUUID != null) {
            goalSelector.removeGoal(attackPlayersGoal);
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
