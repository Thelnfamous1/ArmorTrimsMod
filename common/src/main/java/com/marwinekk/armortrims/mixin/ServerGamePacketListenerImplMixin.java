package com.marwinekk.armortrims.mixin;

import com.marwinekk.armortrims.ArmorTrimsMod;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {

    @Shadow public ServerPlayer player;

    @Inject(method = "handlePlayerAction", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ServerboundPlayerActionPacket;getAction()Lnet/minecraft/network/protocol/game/ServerboundPlayerActionPacket$Action;", ordinal = 0), cancellable = true)
    private void handlePlayerActionLockedSlot(ServerboundPlayerActionPacket packet, CallbackInfo ci){
        if(packet.getAction() == ServerboundPlayerActionPacket.Action.SWAP_ITEM_WITH_OFFHAND){
            if(!this.player.isCreative() && (ArmorTrimsMod.isLocked(this.player.getMainHandItem()) || ArmorTrimsMod.isLocked(this.player.getOffhandItem()))){
                ci.cancel();
            }
        }
    }
}
