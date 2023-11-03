package com.marwinekk.armortrims.mixin;

import com.marwinekk.armortrims.ArmorTrimsMod;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Arrow.class)
abstract class ArrowMixin extends AbstractArrow {

    protected ArrowMixin(EntityType<? extends AbstractArrow> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Inject(method = "setEffectsFromItem",at = @At("RETURN"))
    private void buffArrow(ItemStack stack, CallbackInfo ci) {
        if (stack.hasTag()) {
            int power = stack.getTag().getInt(ArmorTrimsMod.POWER_TAG);
            setBaseDamage(getBaseDamage() + power * .5);

        }
    }
}
