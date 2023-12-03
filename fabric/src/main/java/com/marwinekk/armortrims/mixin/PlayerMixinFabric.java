package com.marwinekk.armortrims.mixin;

import com.marwinekk.armortrims.ArmorTrimsMod;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixinFabric {

    @Inject(method = "tick",at = @At("HEAD"))
    private void playerStartTick(CallbackInfo ci) {
        ArmorTrimsMod.tickPlayer((Player) (Object)this);
    }

    @Inject(method = "actuallyHurt",at = @At("RETURN"))
    private void applyWitherPunch(DamageSource damageSource, float damageAmount, CallbackInfo ci) {
        ArmorTrimsMod.onDamaged((Player) (Object)this,damageSource);
    }
}
