package com.marwinekk.armortrims.mixin;

import com.marwinekk.armortrims.commands.ATCommands;
import com.marwinekk.armortrims.entity.TNTArrowEntity;
import com.marwinekk.armortrims.util.ArmorTrimAbilities;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArrowItem.class)
public class ArrowItemMixin {

    @Inject(method = "createArrow",at = @At("RETURN"),cancellable = true)
    private void makeCustomArrow(Level $$0, ItemStack stack, LivingEntity $$2, CallbackInfoReturnable<AbstractArrow> cir) {
        if (ATCommands.isTNTArrow(stack)) {
            TNTArrowEntity tntArrow = new TNTArrowEntity($$0, $$2);
            ArmorTrimAbilities.lockOn($$2, tntArrow);
            cir.setReturnValue(tntArrow);
        }
    }

}
