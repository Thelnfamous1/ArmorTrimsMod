
package com.marwinekk.armortrims.client.screens;

import com.marwinekk.armortrims.procedures.CooldownProcedure;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class ShowAbilitiesOverlay {
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void eventHandler(RenderGuiEvent.Pre event) {
		int w = event.getWindow().getGuiScaledWidth();
		int h = event.getWindow().getGuiScaledHeight();
		int posX = w / 2;
		Player entity = Minecraft.getInstance().player;
		event.getGuiGraphics().drawString(Minecraft.getInstance().font, CooldownProcedure.execute(entity), posX + 10, h + -50, -1);
	}
}
