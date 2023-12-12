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

import com.marwinekk.armortrims.commands.ATCommands;
import com.marwinekk.armortrims.ducks.PlayerDuck;
import com.marwinekk.armortrims.network.PacketHandler;
import com.marwinekk.armortrims.platform.Services;
import com.marwinekk.armortrims.util.ArmorTrimAbilities;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ArmorTrimsModFabric extends ArmorTrimsMod implements ModInitializer {

	@Override
	public void onInitialize() {
		this.register();
		ServerLifecycleEvents.SERVER_STARTED.register(this::serverStartedF);
		ServerLifecycleEvents.SERVER_STOPPED.register(this::serverStoppedF);
		ServerTickEvents.START_WORLD_TICK.register(this::serverLevelTick);
		ServerPlayerEvents.AFTER_RESPAWN.register(((oldPlayer, newPlayer, alive) -> {
			PlayerDuck playerDuck = (PlayerDuck)newPlayer;
			if(!playerDuck.hasSetBonus(Items.DIAMOND)){
				Services.PLATFORM.removeExtraInventorySlots(newPlayer);
			}
		}));
		ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
			if(entity instanceof ItemEntity itemEntity){
				ArmorTrimsMod.removeAllBonusEnchants(itemEntity.getItem());
				ArmorTrimsMod.UNLOCK_SLOT.accept(itemEntity.getItem());
				ArmorTrimAbilities.toggleEnchantBoost(itemEntity.getItem(), false);
			}
		});
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			ATCommands.register(dispatcher);
		});
		ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register(((world, entity, killedEntity) -> {
			if(entity instanceof PlayerDuck playerDuck && playerDuck.hasSetBonus(Items.GOLD_INGOT)){
				killedEntity.spawnAtLocation(new ItemStack(Items.GOLD_NUGGET, entity.level().random.nextIntBetweenInclusive(1, 5)));
			}
		}));
		PacketHandler.registerPackets();
	}

	private void register() {
		Registry.register(BuiltInRegistries.ENTITY_TYPE,new ResourceLocation(MOD_ID,"tnt_arrow"),ArmorTrimsModEntities.TNT_ARROW);
		Registry.register(BuiltInRegistries.ENTITY_TYPE,new ResourceLocation(MOD_ID,"damageless_arrow"),ArmorTrimsModEntities.DAMAGELESS_ARROW);
		Registry.register(BuiltInRegistries.ENTITY_TYPE,new ResourceLocation(MOD_ID,"block_breaker_arrow"),ArmorTrimsModEntities.BLOCK_BREAKER_ARROW);
	}

	private void serverLevelTick(ServerLevel level) {
		tickLevel(level);
	}

	private void serverStartedF(MinecraftServer server) {
		serverStarted(server);
	}

	private void serverStoppedF(MinecraftServer server) {
		serverStopped(server);
	}

}
