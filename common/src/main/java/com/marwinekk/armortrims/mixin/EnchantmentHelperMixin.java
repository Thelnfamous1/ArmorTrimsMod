package com.marwinekk.armortrims.mixin;

import com.marwinekk.armortrims.ArmorTrimsMod;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @Inject(method = "hasBindingCurse", at = @At("HEAD"), cancellable = true)
    private static void handleLockedSlot(ItemStack stack, CallbackInfoReturnable<Boolean> cir){
        if(ArmorTrimsMod.isLocked(stack)){
            cir.setReturnValue(true);
        }
    }
}
