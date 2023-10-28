
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package com.marwinekk.armortrims.init;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.client.renderer.entity.ThrownItemRenderer;

import com.marwinekk.armortrims.client.renderer.TNTArrowRenderer;
import com.marwinekk.armortrims.client.renderer.PiercingArrowRenderer;
import com.marwinekk.armortrims.client.renderer.Pa3Renderer;
import com.marwinekk.armortrims.client.renderer.Pa2Renderer;
import com.marwinekk.armortrims.client.renderer.MiniGuyRenderer;
import com.marwinekk.armortrims.client.renderer.CloneRenderer;
import com.marwinekk.armortrims.client.renderer.BruteRenderer;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ArmorTrimsModEntityRenderers {
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(ArmorTrimsModEntities.BRUTE.get(), BruteRenderer::new);
		event.registerEntityRenderer(ArmorTrimsModEntities.ALLY_WITCH.get(), CloneRenderer::new);
		event.registerEntityRenderer(ArmorTrimsModEntities.PIERCING_ARROW.get(), PiercingArrowRenderer::new);
		event.registerEntityRenderer(ArmorTrimsModEntities.PA_2.get(), Pa2Renderer::new);
		event.registerEntityRenderer(ArmorTrimsModEntities.PA_3.get(), Pa3Renderer::new);
		event.registerEntityRenderer(ArmorTrimsModEntities.TNT_ARROW.get(), TNTArrowRenderer::new);
		event.registerEntityRenderer(ArmorTrimsModEntities.MINI_GUY.get(), MiniGuyRenderer::new);
		event.registerEntityRenderer(ArmorTrimsModEntities.POTION_THROW.get(), ThrownItemRenderer::new);
	}
}
