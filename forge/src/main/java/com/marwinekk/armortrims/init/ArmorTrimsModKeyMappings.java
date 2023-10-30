
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

import com.marwinekk.armortrims.network.CombatMessage;
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


	@SubscribeEvent
	public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
		event.register(COMBAT);
	}

	@Mod.EventBusSubscriber({Dist.CLIENT})
	public static class KeyEventListener {
		@SubscribeEvent
		public static void onClientTick(TickEvent.ClientTickEvent event) {
			if (Minecraft.getInstance().screen == null) {
				COMBAT.consumeClick();
			}
		}
	}
}
