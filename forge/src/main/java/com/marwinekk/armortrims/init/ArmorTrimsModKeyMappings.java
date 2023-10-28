
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package com.marwinekk.armortrims.init;

import org.lwjgl.glfw.GLFW;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;

import com.marwinekk.armortrims.network.LeggingsKeybindMessage;
import com.marwinekk.armortrims.network.HelmetKeybindMessage;
import com.marwinekk.armortrims.network.CombatMessage;
import com.marwinekk.armortrims.network.ChestplateKeybindMessage;
import com.marwinekk.armortrims.network.BootsKeybindMessage;
import com.marwinekk.armortrims.network.BeaconGUIMessage;
import com.marwinekk.armortrims.ArmorTrimsModForge;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class ArmorTrimsModKeyMappings {
	public static final KeyMapping COMBAT = new KeyMapping("key.armor_trims.combat", GLFW.GLFW_KEY_Z, "key.categories.trims") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				ArmorTrimsModForge.PACKET_HANDLER.sendToServer(new CombatMessage(0, 0));
				CombatMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping BEACON_GUI = new KeyMapping("key.armor_trims.beacon_gui", GLFW.GLFW_KEY_X, "key.categories.trims") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				ArmorTrimsModForge.PACKET_HANDLER.sendToServer(new BeaconGUIMessage(0, 0));
				BeaconGUIMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping HELMET_KEYBIND = new KeyMapping("key.armor_trims.helmet_keybind", GLFW.GLFW_KEY_1, "key.categories.dragonegg") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				ArmorTrimsModForge.PACKET_HANDLER.sendToServer(new HelmetKeybindMessage(0, 0));
				HelmetKeybindMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping CHESTPLATE_KEYBIND = new KeyMapping("key.armor_trims.chestplate_keybind", GLFW.GLFW_KEY_2, "key.categories.dragonegg") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				ArmorTrimsModForge.PACKET_HANDLER.sendToServer(new ChestplateKeybindMessage(0, 0));
				ChestplateKeybindMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping LEGGINGS_KEYBIND = new KeyMapping("key.armor_trims.leggings_keybind", GLFW.GLFW_KEY_3, "key.categories.dragonegg") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				ArmorTrimsModForge.PACKET_HANDLER.sendToServer(new LeggingsKeybindMessage(0, 0));
				LeggingsKeybindMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping BOOTS_KEYBIND = new KeyMapping("key.armor_trims.boots_keybind", GLFW.GLFW_KEY_4, "key.categories.dragonegg") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				ArmorTrimsModForge.PACKET_HANDLER.sendToServer(new BootsKeybindMessage(0, 0));
				BootsKeybindMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};

	@SubscribeEvent
	public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
		event.register(COMBAT);
		event.register(BEACON_GUI);
		event.register(HELMET_KEYBIND);
		event.register(CHESTPLATE_KEYBIND);
		event.register(LEGGINGS_KEYBIND);
		event.register(BOOTS_KEYBIND);
	}

	@Mod.EventBusSubscriber({Dist.CLIENT})
	public static class KeyEventListener {
		@SubscribeEvent
		public static void onClientTick(TickEvent.ClientTickEvent event) {
			if (Minecraft.getInstance().screen == null) {
				COMBAT.consumeClick();
				BEACON_GUI.consumeClick();
				HELMET_KEYBIND.consumeClick();
				CHESTPLATE_KEYBIND.consumeClick();
				LEGGINGS_KEYBIND.consumeClick();
				BOOTS_KEYBIND.consumeClick();
			}
		}
	}
}
