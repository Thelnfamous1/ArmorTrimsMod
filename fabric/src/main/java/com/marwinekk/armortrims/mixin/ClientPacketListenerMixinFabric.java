package com.marwinekk.armortrims.mixin;

import com.marwinekk.armortrims.ArmorTrimsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixinFabric {

    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "handleLogin",at = @At("RETURN"))
    private void onPlayerLogin(ClientboundLoginPacket packet, CallbackInfo ci) {
        ArmorTrimsMod.playerLogin(this.minecraft.player);
    }
}
