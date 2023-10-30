
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package com.marwinekk.armortrims.init;

import com.marwinekk.armortrims.client.renderer.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ArmorTrimsModEntityRenderers {
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(ArmorTrimsModEntities.PIERCING_ARROW.get(), PiercingArrowRenderer::new);
		event.registerEntityRenderer(ArmorTrimsModEntities.PA_2.get(), Pa2Renderer::new);
		event.registerEntityRenderer(ArmorTrimsModEntities.PA_3.get(), Pa3Renderer::new);
		event.registerEntityRenderer(ArmorTrimsModEntities.TNT_ARROW.get(), TNTArrowRenderer::new);
		event.registerEntityRenderer(ArmorTrimsModEntities.MINI_GUY.get(), MiniGuyRenderer::new);
	}
}
