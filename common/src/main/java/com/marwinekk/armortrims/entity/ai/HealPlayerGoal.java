package com.marwinekk.armortrims.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class HealPlayerGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

    public HealPlayerGoal(Mob mob, Class<T> $$1, int $$2, boolean $$3, boolean $$4, @Nullable Predicate<LivingEntity> $$5) {
        super(mob, $$1, $$2, $$3, $$4, $$5);
    }

    protected void findTarget() {
        LivingEntity owner = ((OwnableEntity)mob).getOwner();

        if (owner != null && owner.getHealth() < owner.getMaxHealth() && owner.distanceTo(mob) < 10) {
            this.target = owner;
        } else {
            target = null;
        }
    }
}
