package com.marwinekk.armortrims.mixin;

import com.marwinekk.armortrims.ArmorTrimsMod;
import com.marwinekk.armortrims.ArmorTrimsModEntities;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Inventory.class)
public class InventoryMixin {

    @Inject(method = "setItem",at = @At("RETURN"))
    private void onInventoryChange(CallbackInfo ci) {
        ArmorTrimsMod.onInventoryChange((Inventory) (Object)this,null);
    }



    @ModifyVariable(method = "hurtArmor", at = @At(value = "STORE", ordinal = 0), ordinal = 0, argsOnly = true)
    private float modifyArmorDamage(float currentDamage, DamageSource source, float originalDamage, int[] slots){
        Entity directEntity = source.getDirectEntity();
        if(directEntity != null && directEntity.getType() == ArmorTrimsModEntities.BLOCK_BREAKER_ARROW){
            return currentDamage * 4; // full damage is applied to each piece of armor instead of only 25%
        } else {
            return currentDamage;
        }
    }
}
