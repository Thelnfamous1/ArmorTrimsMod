package com.marwinekk.armortrims.mixin;

import com.marwinekk.armortrims.ArmorTrimsMod;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public abstract class MobMixinFabric {

    @Shadow public abstract void setTarget(@Nullable LivingEntity $$0);

    @Inject(method = "setTarget",at = @At("HEAD"))
    private void forceTargetChange(LivingEntity target, CallbackInfo ci) {
        if (ArmorTrimsMod.changeTarget(target,(Mob)(Object)this)) {
            setTarget(null);
        }
    }
}
