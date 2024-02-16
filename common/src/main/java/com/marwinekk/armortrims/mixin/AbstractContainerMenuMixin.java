package com.marwinekk.armortrims.mixin;

import com.marwinekk.armortrims.ArmorTrimsMod;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractContainerMenu.class)
public class AbstractContainerMenuMixin {

    @Redirect(method = "doClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/Slot;mayPickup(Lnet/minecraft/world/entity/player/Player;)Z"))
    private boolean handleTryRemoveForLockedSlot(Slot instance, Player player){
        return ArmorTrimsMod.mayPickupCheckLocked(instance, player);
    }
}
