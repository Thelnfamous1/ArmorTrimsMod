
package com.marwinekk.armortrims.network;

import com.marwinekk.armortrims.ArmorTrimsMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MenuBeaconButtonMessage {
	private final MobEffect mobEffect;

	public MenuBeaconButtonMessage(FriendlyByteBuf buffer) {
		this.mobEffect = MobEffect.byId(buffer.readInt());
	}

	public MenuBeaconButtonMessage(MobEffect mobEffect) {
		this.mobEffect = mobEffect;
	}

	public static void buffer(MenuBeaconButtonMessage message, FriendlyByteBuf buffer) {
		buffer.writeInt(MobEffect.getId(message.mobEffect));
	}

	public static void handler(MenuBeaconButtonMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();
		context.enqueueWork(() -> {
			ServerPlayer entity = context.getSender();
			handleButtonAction(entity, message.mobEffect);
		});
		context.setPacketHandled(true);
	}

	public static void handleButtonAction(ServerPlayer player, MobEffect mobEffect) {
		ArmorTrimsMod.addOneEffectRemoveOther(player, mobEffect);
	}
}
