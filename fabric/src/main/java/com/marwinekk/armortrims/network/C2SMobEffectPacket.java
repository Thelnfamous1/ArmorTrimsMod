
package com.marwinekk.armortrims.network;

import com.marwinekk.armortrims.ArmorTrimsMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SMobEffectPacket {
	private final MobEffect mobEffect;

	public C2SMobEffectPacket(FriendlyByteBuf buffer) {
		this.mobEffect = MobEffect.byId(buffer.readInt());
	}

	public C2SMobEffectPacket(MobEffect mobEffect) {
		this.mobEffect = mobEffect;
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(MobEffect.getId(mobEffect));
	}

	public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();
		context.enqueueWork(() -> {
			ServerPlayer player = context.getSender();
			ArmorTrimsMod.addOneEffectRemoveOther(player, mobEffect);
		});
		context.setPacketHandled(true);
	}
}
