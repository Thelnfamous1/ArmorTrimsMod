package com.marwinekk.armortrims.mixin;

import com.marwinekk.armortrims.ducks.PhysicsCheck;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin implements PhysicsCheck {

    @Shadow public abstract boolean isNoPhysics();

    @ModifyVariable(method = "tick", at = @At(value = "LOAD", ordinal = 4))
    private boolean modifyIsNoPhysicsLocalForGravity(boolean isNoPhysics){
        if(!isNoPhysics && this.canBypassGravity()){
            return true;
        }
        return this.isNoPhysics();
    }
}