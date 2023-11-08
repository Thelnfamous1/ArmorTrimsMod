package com.marwinekk.armortrims.mixin;

import com.marwinekk.armortrims.ArmorTrimsMod;
import net.minecraft.world.damagesource.DamageSource;
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
        ArmorTrimsMod.giveTotemToDyingPlayer($elf());
    }

    @Unique private LivingEntity $elf() {
        return (LivingEntity) (Object)this;
    }
}
