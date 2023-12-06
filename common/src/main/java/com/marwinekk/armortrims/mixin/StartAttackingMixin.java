package com.marwinekk.armortrims.mixin;

import com.marwinekk.armortrims.ArmorTrimsMod;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.StartAttacking;
import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;
import java.util.function.Predicate;

@Debug(export = true)
@Mixin(StartAttacking.class)
public class StartAttackingMixin<E extends Mob> {
    private static LivingEntity currentTarget;

    @ModifyVariable(method = "lambda$create$1", at = @At(value = "LOAD", ordinal = 0), ordinal = 0)
    private static LivingEntity captureTarget(LivingEntity target){
        currentTarget = target;
        return target;
    }

    @Inject(method = "lambda$create$1", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/behavior/declarative/MemoryAccessor;set(Ljava/lang/Object;)V"), cancellable = true)
    private static void handleConditions(Predicate $$0x, Function $$1x, MemoryAccessor $$2x, MemoryAccessor $$3x, ServerLevel $$4, Mob attacker, long $$6, CallbackInfoReturnable<Boolean> cir){
        if(currentTarget != null && (ArmorTrimsMod.isOwnedBy(attacker, currentTarget) || ArmorTrimsMod.isImmuneToTargeting(attacker, currentTarget))) {
            cir.setReturnValue(false);
        }
        if(currentTarget != null) currentTarget = null;
    }
}