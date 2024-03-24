
package com.marwinekk.armortrims.network;

import com.marwinekk.armortrims.util.EmeraldTrimAbilities;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SMobEffectPacket {
	private final MobEffect mobEffect;
	private final int amplifier;

	public C2SMobEffectPacket(FriendlyByteBuf buffer) {
		this.mobEffect = MobEffect.byId(buffer.readInt());
		this.amplifier = buffer.readInt();
	}

	public C2SMobEffectPacket(MobEffect mobEffect, int amplifier) {
		this.mobEffect = mobEffect;
		this.amplifier = amplifier;
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(MobEffect.getId(mobEffect));
		buffer.writeInt(this.amplifier);
	}

	public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();
		context.enqueueWork(() -> {
			ServerPlayer player = context.getSender();
			EmeraldTrimAbilities.updateBeaconEffect(player, mobEffect, this.amplifier);
		});
		context.setPacketHandled(true);
	}
}
