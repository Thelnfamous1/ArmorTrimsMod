package com.marwinekk.armortrims.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class HealPlayerGoal<M extends Mob & OwnableEntity, T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

    public HealPlayerGoal(M mob, Class<T> targetType, int randomInterval, boolean mustSee, boolean mustReach, @Nullable Predicate<LivingEntity> selector) {
        super(mob, targetType, randomInterval, mustSee, mustReach, selector);
    }

    protected void findTarget() {
        LivingEntity owner = ((OwnableEntity)mob).getOwner();
        if (owner != null && owner.getHealth() < owner.getMaxHealth() && owner.distanceTo(mob) < 10) {
            this.target = owner;
        } else {
            this.target = null;
        }
    }
}
