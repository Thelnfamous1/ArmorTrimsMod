package com.marwinekk.armortrims.mixin;

import com.marwinekk.armortrims.ArmorTrimsMod;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TargetingConditions.class)
public class TargetingConditionsMixin {

    @Shadow @Final private boolean isCombat;

    @Inject(method = "test", at = @At("RETURN"), cancellable = true)
    private void handleTest(LivingEntity pAttacker, LivingEntity pTarget, CallbackInfoReturnable<Boolean> cir){
        if(cir.getReturnValue() && pAttacker instanceof Mob mobAttacker){
            if(!this.isCombat) return;

            if(ArmorTrimsMod.isUnableToTarget(mobAttacker, pTarget)){
                cir.setReturnValue(false);
            }
        }
    }
}