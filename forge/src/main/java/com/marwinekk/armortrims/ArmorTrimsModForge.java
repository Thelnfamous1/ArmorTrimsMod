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
import com.marwinekk.armortrims.util.CopperTrimAbilities;
import com.marwinekk.armortrims.util.IronTrimAbilities;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.*;
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
import net.minecraftforge.registries.RegisterEvent;

@Mod(ArmorTrimsMod.MOD_ID)
public class ArmorTrimsModForge extends ArmorTrimsMod {
	public ArmorTrimsModForge() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		bus.addListener(ModDatagen::start);
		bus.addListener(this::setup);
		bus.addListener(this::register);

		if (FMLEnvironment.dist.isClient()) {
			bus.addListener(Client::keybinds);
			bus.addListener(this::setupClient);
			bus.addListener(Client::registerEntityRenderers);
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
		MinecraftForge.EVENT_BUS.addListener(this::onEntityFall);
		MinecraftForge.EVENT_BUS.addListener(this::onEntityTarget);
		MinecraftForge.EVENT_BUS.addListener(this::onEntityAttacked);
	}

	private void register(RegisterEvent event) {
		event.register(Registries.ENTITY_TYPE,new ResourceLocation(MOD_ID,"tnt_arrow"),() -> ArmorTrimsModEntities.TNT_ARROW);
	}

	private void handleVisibility(LivingEvent.LivingVisibilityEvent event) {
		if (event.getEntity().hasEffect(MobEffects.INVISIBILITY)) {
			event.modifyVisibility(0);
		}
	}

	public void onEntityAttacked(LivingAttackEvent event) {
		LivingEntity livingEntity = event.getEntity();
		DamageSource source = event.getSource();
		if (CopperTrimAbilities.canPreventDamage(livingEntity,source)) {
			event.setCanceled(true);
		}
	}

	public void onEntityFall(LivingFallEvent event) {
		LivingEntity livingEntity = event.getEntity();
		if (IronTrimAbilities.onFallDamage(livingEntity)) {
			event.setCanceled(true);
		}
	}

	public void onEntityTarget(LivingChangeTargetEvent event) {
		LivingEntity victim = event.getOriginalTarget();
		if(ArmorTrimsMod.changeTarget(victim,event.getEntity())) {
			event.setNewTarget(null);
		}
	}

	private void knockback(LivingKnockBackEvent event) {
		float newStrength = onKnockback(event.getOriginalStrength(),event.getEntity());
		event.setStrength(newStrength);
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

		}
	}
}
