
package com.marwinekk.armortrims.network;

import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;

import java.util.function.Supplier;
import java.util.HashMap;

import com.marwinekk.armortrims.world.inventory.MenuBeaconMenu;
import com.marwinekk.armortrims.procedures.StrengthBeacProcedure;
import com.marwinekk.armortrims.procedures.SpeedBeacProcedure;
import com.marwinekk.armortrims.procedures.ResistanceBeacProcedure;
import com.marwinekk.armortrims.procedures.JumpBeacProcedure;
import com.marwinekk.armortrims.procedures.HeroBeacProcedure;
import com.marwinekk.armortrims.procedures.HasteBeacProcedure;
import com.marwinekk.armortrims.ArmorTrimsModForge;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MenuBeaconButtonMessage {
	private final int buttonID, x, y, z;

	public MenuBeaconButtonMessage(FriendlyByteBuf buffer) {
		this.buttonID = buffer.readInt();
		this.x = buffer.readInt();
		this.y = buffer.readInt();
		this.z = buffer.readInt();
	}

	public MenuBeaconButtonMessage(int buttonID, int x, int y, int z) {
		this.buttonID = buttonID;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public static void buffer(MenuBeaconButtonMessage message, FriendlyByteBuf buffer) {
		buffer.writeInt(message.buttonID);
		buffer.writeInt(message.x);
		buffer.writeInt(message.y);
		buffer.writeInt(message.z);
	}

	public static void handler(MenuBeaconButtonMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();
		context.enqueueWork(() -> {
			Player entity = context.getSender();
			int buttonID = message.buttonID;
			int x = message.x;
			int y = message.y;
			int z = message.z;
			handleButtonAction(entity, buttonID, x, y, z);
		});
		context.setPacketHandled(true);
	}

	public static void handleButtonAction(Player entity, int buttonID, int x, int y, int z) {
		Level world = entity.level();
		HashMap guistate = MenuBeaconMenu.guistate;
		// security measure to prevent arbitrary chunk generation
		if (!world.hasChunkAt(new BlockPos(x, y, z)))
			return;
		if (buttonID == 0) {

			SpeedBeacProcedure.execute(entity);
		}
		if (buttonID == 1) {

			HasteBeacProcedure.execute(entity);
		}
		if (buttonID == 2) {

			ResistanceBeacProcedure.execute(entity);
		}
		if (buttonID == 3) {

			JumpBeacProcedure.execute(entity);
		}
		if (buttonID == 4) {

			StrengthBeacProcedure.execute(entity);
		}
		if (buttonID == 5) {

			HeroBeacProcedure.execute(entity);
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		ArmorTrimsModForge.addNetworkMessage(MenuBeaconButtonMessage.class, MenuBeaconButtonMessage::buffer, MenuBeaconButtonMessage::new, MenuBeaconButtonMessage::handler);
	}
}
