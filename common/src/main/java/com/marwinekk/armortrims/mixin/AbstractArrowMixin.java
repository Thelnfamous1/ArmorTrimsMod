package com.marwinekk.armortrims.mixin;

import com.marwinekk.armortrims.ArmorTrimsMod;
import com.marwinekk.armortrims.ducks.PhysicsCheck;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin implements PhysicsCheck {

    @Shadow public abstract boolean isNoPhysics();

    @Shadow protected boolean inGround;

    @Unique
    private boolean armorTrimsMod$wasInGround;

    @Inject(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;inGround:Z", opcode = ArmorTrimsMod.PUTFIELD_OPCODE, ordinal = 0))
    private void preModifyInGroundForCollision(CallbackInfo ci){
        this.armorTrimsMod$wasInGround = this.inGround;
    }

    @Inject(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;inGround:Z", shift = At.Shift.AFTER, opcode = ArmorTrimsMod.PUTFIELD_OPCODE, ordinal = 0))
    private void postModifyInGroundForCollision(CallbackInfo ci){
        if(!this.armorTrimsMod$wasInGround && this.canBypassInGround()){
            this.inGround = false;
        }
    }

    @ModifyVariable(method = "tick", at = @At(value = "LOAD", ordinal = 4))
    private boolean modifyIsNoPhysicsLocalForGravity(boolean isNoPhysics){
        if(!isNoPhysics && this.canBypassGravity()){
            return true;
        }
        return this.isNoPhysics();
    }
}