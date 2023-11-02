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
import com.marwinekk.armortrims.ducks.PlayerDuck;
import com.marwinekk.armortrims.init.ArmorTrimsModEntities;
import com.marwinekk.armortrims.init.ArmorTrimsModItems;
import com.marwinekk.armortrims.network.PacketHandler;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Mod(ArmorTrimsMod.MOD_ID)
public class ArmorTrimsModForge extends ArmorTrimsMod {
	public ArmorTrimsModForge() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		bus.addListener(ModDatagen::start);
		bus.addListener(this::setup);
		ArmorTrimsModItems.REGISTRY.register(bus);
		ArmorTrimsModEntities.REGISTRY.register(bus);

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
		MinecraftForge.EVENT_BUS.addListener(this::onDamage);
		MinecraftForge.EVENT_BUS.addListener(this::knockback);
		MinecraftForge.EVENT_BUS.addListener(this::handleVisibility);
	}

	private void handleVisibility(LivingEvent.LivingVisibilityEvent event) {
		if (event.getEntity().hasEffect(MobEffects.INVISIBILITY)) {
			event.modifyVisibility(0);
		}
	}

	private void knockback(LivingKnockBackEvent event) {
		LivingEntity livingEntity = event.getEntity();


		LivingEntity lastAttacker = livingEntity.getLastAttacker();
		if (lastAttacker instanceof Player player) {
			PlayerDuck playerDuck = (PlayerDuck) player;
			for (EquipmentSlot slot : slots) {
				Item trim = slot == null ? playerDuck.regularSetBonus() : getTrimItem(player.level(), player.getItemBySlot(slot));
				int timer = playerDuck.abilityTimer(slot);
				if (trim == Items.IRON_INGOT && timer > 0) {
					event.setStrength(5);
				//	livingEntity.addDeltaMovement(new Vec3(0,4,0));
					return;
				}
			}
		}

		//onKnockback(livingEntity);
	}

	private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

	public static void queueServerWork(int tick, Runnable action) {
		workQueue.add(new AbstractMap.SimpleEntry<>(action, tick));
	}

	public void playerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.START) {
			tickPlayer(event.player);
		}
	}

	private void onDamage(LivingDamageEvent event) {
		onDamaged(event.getEntity(),event.getSource());
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
		onInventoryChange(player.getInventory(), null);
	}

	private void serverLogin(PlayerEvent.PlayerLoggedInEvent event) {
		playerLogin(event.getEntity());
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
