package com.marwinekk.armortrims.mixin;

import com.marwinekk.armortrims.util.ArmorTrimAbilities;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightningBolt.class)
public abstract class LightningBoltMixin extends Entity {

    @Shadow @javax.annotation.Nullable public abstract ServerPlayer getCause();

    @Unique
    @Nullable
    private Entity currentHitEntity;

    public LightningBoltMixin(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }

    @ModifyVariable(method = "tick", at = @At(value = "LOAD", ordinal = 0), ordinal = 0)
    private Entity preThunderHit(Entity entity){
        this.currentHitEntity = entity;
        return entity;
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;thunderHit(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LightningBolt;)V"))
    private void postThunderHit(CallbackInfo ci){
        if(this.currentHitEntity instanceof LivingEntity living && this.getTags().contains(ArmorTrimAbilities.ARMOR_TRIMS_TAG)){
            if(living.getLastDamageSource() != null && living.getLastDamageSource().getDirectEntity() == this){
                living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * 2), this.getCause());
            }
        }
        if(this.currentHitEntity != null) this.currentHitEntity = null;
    }
}
