
package com.marwinekk.armortrims.network;

import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

import com.marwinekk.armortrims.procedures.HelmetKeybindOnKeyPressedProcedure;
import com.marwinekk.armortrims.ArmorTrimsModForge;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class HelmetKeybindMessage {
	int type;

	public HelmetKeybindMessage(int type) {
		this.type = type;
	}

	public HelmetKeybindMessage(FriendlyByteBuf buffer) {
		this.type = buffer.readInt();
	}

	public static void buffer(HelmetKeybindMessage message, FriendlyByteBuf buffer) {
		buffer.writeInt(message.type);
	}

	public static void handler(HelmetKeybindMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();
		context.enqueueWork(() -> pressAction(context.getSender(), message.type));
		context.setPacketHandled(true);
	}

	public static void pressAction(Player entity, int type) {
		Level world = entity.level();
		double x = entity.getX();
		double y = entity.getY();
		double z = entity.getZ();
		// security measure to prevent arbitrary chunk generation
		if (!world.hasChunkAt(entity.blockPosition()))
			return;
		if (type == 0) {

			HelmetKeybindOnKeyPressedProcedure.execute(world, x, y, z, entity);
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		ArmorTrimsModForge.addNetworkMessage(HelmetKeybindMessage.class, HelmetKeybindMessage::buffer, HelmetKeybindMessage::new, HelmetKeybindMessage::handler);
	}
}
