package com.marwinekk.armortrims.entity;

import com.marwinekk.armortrims.ArmorTrimsModEntities;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import java.util.Collection;

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
    }

    @Override
    protected void onHitEntity(EntityHitResult $$0) {
        super.onHitEntity($$0);
    }

    protected void doPostHurtEffects(LivingEntity pLiving) {
        super.doPostHurtEffects(pLiving);
        Entity entity = this.getEffectSource();

        for(MobEffectInstance mobeffectinstance : this.potion.getEffects()) {
            pLiving.addEffect(new MobEffectInstance(mobeffectinstance.getEffect(), Math.max(mobeffectinstance.mapDuration((p_268168_) -> {
                return p_268168_ / 8;
            }), 1), mobeffectinstance.getAmplifier(), mobeffectinstance.isAmbient(), mobeffectinstance.isVisible()), entity);
        }
        pLiving.setArrowCount(pLiving.getArrowCount() - 1);
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
        discard();
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }
}
