package com.marwinekk.armortrims.mixin;

import com.marwinekk.armortrims.ducks.PiglinBruteDuck;
import com.marwinekk.armortrims.ducks.PlayerDuck;
import com.marwinekk.armortrims.util.AmethystTrimAbilities;
import com.marwinekk.armortrims.util.EmeraldTrimAbilities;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "checkTotemDeathProtection",at = @At("HEAD"))
    private void giveTotem(DamageSource $$0, CallbackInfoReturnable<Boolean> cir) {
        EmeraldTrimAbilities.onDeathProtectionCheck($elf());
    }

    @Inject(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z", at = @At("HEAD"), cancellable = true)
    private void handleAddEffect(MobEffectInstance $$0, Entity $$1, CallbackInfoReturnable<Boolean> cir){
        if($$0.getEffect().getCategory().equals(MobEffectCategory.HARMFUL)
                && $elf() instanceof PlayerDuck playerDuck
                && AmethystTrimAbilities.cannotReceiveNegativeMobEffects(playerDuck)){
            cir.setReturnValue(false);
        } else if($elf() instanceof PiglinBruteDuck bruteDuck && bruteDuck.getOwner() != null){
            cir.setReturnValue(false);
        }
    }

    @Unique private LivingEntity $elf() {
        return (LivingEntity) (Object)this;
    }
}
