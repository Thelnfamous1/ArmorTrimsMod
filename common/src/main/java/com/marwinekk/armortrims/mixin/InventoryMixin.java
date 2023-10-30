package com.marwinekk.armortrims.mixin;

import com.marwinekk.armortrims.ArmorTrimsMod;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Inventory.class)
public class InventoryMixin {

    @Inject(method = "setItem",at = @At("RETURN"))
    private void onInventoryChange(CallbackInfo ci) {
        ArmorTrimsMod.onInventoryChange((Inventory) (Object)this);
    }
}
