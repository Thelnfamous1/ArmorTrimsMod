/*
 *    MCreator note:
 *
 *    If you lock base mod element files, you can edit this file and it won't get overwritten.
 *    If you change your modid or package, you need to apply these changes to this file MANUALLY.
 *
 *    Settings in @Mod annotation WON'T be changed in case of the base mod element
 *    files lock too, so you need to set them manually here in such case.
 *
 *    If you do not lock base mod element files in Workspace settings, this file
 *    will be REGENERATED on each build.
 *
 */
package com.marwinekk.armortrims;

import com.marwinekk.armortrims.network.PacketHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;

public class ArmorTrimsModFabric extends ArmorTrimsMod implements ModInitializer {

	@Override
	public void onInitialize() {
		this.register();
		ServerLifecycleEvents.SERVER_STARTED.register(this::serverStartedF);
		ServerLifecycleEvents.SERVER_STOPPED.register(this::serverStoppedF);
		PacketHandler.registerPackets();
	}

	private void register() {
		Registry.register(BuiltInRegistries.ENTITY_TYPE,new ResourceLocation(MOD_ID,"tnt_arrow"),ArmorTrimsModEntities.TNT_ARROW);
		Registry.register(BuiltInRegistries.ENTITY_TYPE,new ResourceLocation(MOD_ID,"damageless_arrow"),ArmorTrimsModEntities.DAMAGELESS_ARROW);
	}

	private void serverStartedF(MinecraftServer server) {
		serverStarted(server);
	}

	private void serverStoppedF(MinecraftServer server) {
		serverStopped(server);
	}

}
