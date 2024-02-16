package com.marwinekk.armortrims.mixin;

import com.marwinekk.armortrims.ArmorTrimsMod;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer {

    public LocalPlayerMixin(ClientLevel $$0, GameProfile $$1) {
        super($$0, $$1);
    }

    @Inject(method = "drop", at = @At("HEAD"), cancellable = true)
    private void handleDropLockedSlot(boolean fullStack, CallbackInfoReturnable<Boolean> cir){
        if(!this.isCreative() && ArmorTrimsMod.isLocked(this.getInventory().getSelected())){
            cir.setReturnValue(false);
        }
    }
}
