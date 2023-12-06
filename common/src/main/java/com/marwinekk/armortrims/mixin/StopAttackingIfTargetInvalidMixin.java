package com.marwinekk.armortrims.mixin;

import com.marwinekk.armortrims.ArmorTrimsMod;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

@Debug(export = true)
@Mixin(StopAttackingIfTargetInvalid.class)
public abstract class StopAttackingIfTargetInvalidMixin<E extends Mob> {

    private static LivingEntity currentTarget;

    @ModifyVariable(method = {"method_47135", "m_257275_", "lambda$create$4"}, at = @At(value = "LOAD", ordinal = 0), ordinal = 0)
    private static LivingEntity captureTarget(LivingEntity target){
        currentTarget = target;
        return target;
    }

    @Inject(method = {"method_47135", "m_257275_", "lambda$create$4"}, at = @At(value = "RETURN", ordinal = 1), cancellable = true)
    private static void forceTargetErasure(BehaviorBuilder.Instance $$0x, MemoryAccessor attackTargetMemory, boolean $$2x, MemoryAccessor $$3x, Predicate $$4x, BiConsumer onTargetErased, ServerLevel $$6, Mob attacker, long $$8, CallbackInfoReturnable<Boolean> cir){
        if(currentTarget != null && (ArmorTrimsMod.isOwnedBy(attacker, currentTarget) || ArmorTrimsMod.isImmuneToTargeting(attacker, currentTarget))){
            onTargetErased.accept(attacker, currentTarget);
            attackTargetMemory.erase();
            cir.setReturnValue(true);
        }
        if(currentTarget != null) currentTarget = null;
    }
}