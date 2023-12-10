package com.marwinekk.armortrims.entity;

import com.marwinekk.armortrims.ArmorTrimsModEntities;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class DamagelessArrowEntity extends AbstractArrow {
    public DamagelessArrowEntity(EntityType<? extends DamagelessArrowEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    private Potion potion = Potions.EMPTY;

    protected DamagelessArrowEntity(EntityType<? extends AbstractArrow> $$0, double $$1, double $$2, double $$3, Level $$4) {
        super($$0, $$1, $$2, $$3, $$4);
    }

    public DamagelessArrowEntity(LivingEntity $$1, Level $$2) {
        super(ArmorTrimsModEntities.DAMAGELESS_ARROW, $$1, $$2);
        this.setBaseDamage(0);
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        Entity hitEntity = hitResult.getEntity();
        Entity owner = this.getOwner();
        DamageSource source;
        if (owner == null) {
            source = this.damageSources().arrow(this, this);
        } else {
            source = this.damageSources().arrow(this, owner);
            if (owner instanceof LivingEntity ownerLiving) {
                ownerLiving.setLastHurtMob(hitEntity);
            }
        }

        boolean hitEnderman = hitEntity.getType() == EntityType.ENDERMAN;
        if (this.isOnFire() && !hitEnderman) {
            hitEntity.setSecondsOnFire(5);
        }

        hitEntity.hurt(source, 0);
        if (hitEnderman) {
            return;
        }

        if (hitEntity instanceof LivingEntity hitLiving) {
            if (!this.level().isClientSide && owner instanceof LivingEntity) {
                EnchantmentHelper.doPostHurtEffects(hitLiving, owner);
                EnchantmentHelper.doPostDamageEffects((LivingEntity)owner, hitLiving);
            }

            this.doPostHurtEffects(hitLiving);
            if (owner != null && hitLiving != owner && hitLiving instanceof Player && owner instanceof ServerPlayer && !this.isSilent()) {
                ((ServerPlayer)owner).connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));
            }
        }

        this.playSound(this.getHitGroundSoundEvent(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        if (!this.level().isClientSide) {
            this.discard();
        }
    }

    protected void doPostHurtEffects(LivingEntity pLiving) {
        super.doPostHurtEffects(pLiving);
        Entity source = this.getEffectSource();

        for(MobEffectInstance effectInstance : this.potion.getEffects()) {
            pLiving.addEffect(new MobEffectInstance(effectInstance.getEffect(), Math.max(effectInstance.mapDuration((i) -> i / 8), 1), effectInstance.getAmplifier(), effectInstance.isAmbient(), effectInstance.isVisible()), source);
        }
    }

    public void setEffectsFromItem(ItemStack pStack) {
        if (pStack.is(Items.TIPPED_ARROW)) {
            this.potion = PotionUtils.getPotion(pStack);
       //     Collection<MobEffectInstance> collection = PotionUtils.getCustomEffects(pStack);
     //       if (!collection.isEmpty()) {
    //            for(MobEffectInstance mobeffectinstance : collection) {
     //               this.effects.add(new MobEffectInstance(mobeffectinstance));
     //           }
     //       }

        //    int i = getCustomColor(pStack);
      //      if (i == -1) {
        //        this.updateColor();
      //      } else {
       //         this.setFixedColor(i);
     //       }
        } else if (pStack.is(Items.ARROW)) {
            this.potion = Potions.EMPTY;
     //       this.effects.clear();
        //    this.entityData.set(ID_EFFECT_COLOR, -1);
        }

    }

    @Override
    protected void onHitBlock(BlockHitResult $$0) {
        super.onHitBlock($$0);
        if(!this.level().isClientSide){
            this.discard();
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }
}
