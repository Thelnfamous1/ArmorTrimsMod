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

import com.marwinekk.armortrims.client.Client;
import com.marwinekk.armortrims.datagen.ModDatagen;
import com.marwinekk.armortrims.network.PacketHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.common.MinecraftForge;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.util.AbstractMap;

import com.marwinekk.armortrims.init.ArmorTrimsModParticleTypes;
import com.marwinekk.armortrims.init.ArmorTrimsModItems;
import com.marwinekk.armortrims.init.ArmorTrimsModEntities;

@Mod(ArmorTrimsMod.MOD_ID)
public class ArmorTrimsModForge extends ArmorTrimsMod {
	public static final Logger LOGGER = LogManager.getLogger(ArmorTrimsModForge.class);
	public ArmorTrimsModForge() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		bus.addListener(ModDatagen::start);
		bus.addListener(this::setup);
		ArmorTrimsModItems.REGISTRY.register(bus);
		ArmorTrimsModEntities.REGISTRY.register(bus);

		ArmorTrimsModParticleTypes.REGISTRY.register(bus);

		if (FMLEnvironment.dist.isClient()) {
			bus.addListener(Client::keybinds);
			bus.addListener(this::setupClient);
		}
	}

	private void setupClient(FMLClientSetupEvent event) {
		Client.setup();
	}

	private void setup(FMLCommonSetupEvent event) {
		PacketHandler.registerPackets();
		MinecraftForge.EVENT_BUS.addListener(this::playerTick);
		MinecraftForge.EVENT_BUS.addListener(this::serverTick);
		MinecraftForge.EVENT_BUS.addListener(this::attributes);
		MinecraftForge.EVENT_BUS.addListener(this::serverStartedF);
		MinecraftForge.EVENT_BUS.addListener(this::serverStoppedF);
		MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST,this::toss);
		MinecraftForge.EVENT_BUS.addListener(this::serverLogin);
	}

	private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

	public static void queueServerWork(int tick, Runnable action) {
		workQueue.add(new AbstractMap.SimpleEntry<>(action, tick));
	}

	public void playerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.START) {
			if (event.side == LogicalSide.SERVER) {
				tickServerPlayer((ServerPlayer) event.player);
			}
		}
	}

	public void attributes(ItemAttributeModifierEvent e) {

	}

	private void serverStartedF(ServerStartedEvent event) {
		serverStarted(event.getServer());
	}
	private void serverStoppedF(ServerStoppedEvent event) {
		serverStopped(event.getServer());
	}

	private void toss(ItemTossEvent event) {
		Player player = event.getPlayer();
		onInventoryChange(player.getInventory());
	}

	private void serverLogin(PlayerEvent.PlayerLoggedInEvent event) {
		serverPlayerLogin((ServerPlayer) event.getEntity());
	}

	public void serverTick(TickEvent.ServerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			List<AbstractMap.SimpleEntry<Runnable, Integer>> actions = new ArrayList<>();
			workQueue.forEach(work -> {
				work.setValue(work.getValue() - 1);
				if (work.getValue() == 0)
					actions.add(work);
			});
			actions.forEach(e -> e.getKey().run());
			workQueue.removeAll(actions);
		}
	}
}
