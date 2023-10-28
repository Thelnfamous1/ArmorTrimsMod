package com.marwinekk.armortrims.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

import java.util.Collections;

import com.marwinekk.armortrims.network.ArmorTrimsModVariables;

@Mod.EventBusSubscriber
public class OnPlayerTickProcedure {
	@SubscribeEvent
	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		if (event.getHand() != event.getEntity().getUsedItemHand())
			return;
		execute(event, event.getEntity());
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

	private static void execute(@Nullable Event event, Entity entity) {
		if (entity == null)
			return;
		if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).copper) {
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.awardRecipesByKey(new ResourceLocation[]{new ResourceLocation("armor_trims:activator_rail_cop")});
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.awardRecipesByKey(new ResourceLocation[]{new ResourceLocation("armor_trims:anvil_cop")});
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.awardRecipesByKey(new ResourceLocation[]{new ResourceLocation("armor_trims:blast_furnace_cop")});
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.awardRecipesByKey(new ResourceLocation[]{new ResourceLocation("armor_trims:bucket_cop")});
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.awardRecipesByKey(new ResourceLocation[]{new ResourceLocation("armor_trims:cauldron_cop")});
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.awardRecipesByKey(new ResourceLocation[]{new ResourceLocation("armor_trims:chain_cop")});
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.awardRecipesByKey(new ResourceLocation[]{new ResourceLocation("armor_trims:copper_cop")});
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.awardRecipesByKey(new ResourceLocation[]{new ResourceLocation("armor_trims:crossbow_cop")});
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.awardRecipesByKey(new ResourceLocation[]{new ResourceLocation("armor_trims:detector_rail")});
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.awardRecipesByKey(new ResourceLocation[]{new ResourceLocation("armor_trims:flint_and_steel_cop")});
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.awardRecipesByKey(new ResourceLocation[]{new ResourceLocation("armor_trims:heavt_weighted_pressure_plate_cop")});
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.awardRecipesByKey(new ResourceLocation[]{new ResourceLocation("armor_trims:hopper")});
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.awardRecipesByKey(new ResourceLocation[]{new ResourceLocation("armor_trims:iron_axe_cop")});
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.awardRecipesByKey(new ResourceLocation[]{new ResourceLocation("armor_trims:iron_trapdoor_cop")});
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.awardRecipesByKey(new ResourceLocation[]{new ResourceLocation("armor_trims:iron_axe_cop")});
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.awardRecipesByKey(new ResourceLocation[]{new ResourceLocation("armor_trims:iron_axe_cop_2")});
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.awardRecipesByKey(new ResourceLocation[]{new ResourceLocation("armor_trims:iron_bars")});
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.awardRecipesByKey(new ResourceLocation[]{new ResourceLocation("armor_trims:iron_boots_cop")});
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.awardRecipesByKey(new ResourceLocation[]{new ResourceLocation("armor_trims:iron_chestplate_cop")});
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.awardRecipesByKey(new ResourceLocation[]{new ResourceLocation("armor_trims:iron_door_cop")});
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.awardRecipesByKey(new ResourceLocation[]{new ResourceLocation("armor_trims:iron_door_cop_2")});
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.awardRecipesByKey(new ResourceLocation[]{new ResourceLocation("armor_trims:iron_helmet_cop")});
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.awardRecipesByKey(new ResourceLocation[]{new ResourceLocation("armor_trims:iron_hoe_cop")});
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.awardRecipesByKey(new ResourceLocation[]{new ResourceLocation("armor_trims:iron_hoe_cop_2")});
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.awardRecipesByKey(new ResourceLocation[]{new ResourceLocation("armor_trims:iron_leggings_cop")});
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.awardRecipesByKey(new ResourceLocation[]{new ResourceLocation("armor_trims:iron_nugget")});
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.awardRecipesByKey(new ResourceLocation[]{new ResourceLocation("armor_trims:iron_sword_cop")});
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.awardRecipesByKey(new ResourceLocation[]{new ResourceLocation("armor_trims:minecraft_cop")});
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.awardRecipesByKey(new ResourceLocation[]{new ResourceLocation("armor_trims:piston_cop")});
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.awardRecipesByKey(new ResourceLocation[]{new ResourceLocation("armor_trims:rail_cop")});
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.awardRecipesByKey(new ResourceLocation[]{new ResourceLocation("armor_trims:shears_cop")});
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.awardRecipesByKey(new ResourceLocation[]{new ResourceLocation("armor_trims:shield_cop")});
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.awardRecipesByKey(new ResourceLocation[]{new ResourceLocation("armor_trims:smithing_table_cop")});
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.awardRecipesByKey(new ResourceLocation[]{new ResourceLocation("armor_trims:stonecutter_cop")});
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.awardRecipesByKey(new ResourceLocation[]{new ResourceLocation("armor_trims:tripwire_hook")});
		} else {
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.server.getRecipeManager().byKey(new ResourceLocation("armor_trims:activator_rail_cop")).ifPresent(_rec -> _serverPlayer.resetRecipes(Collections.singleton(_rec)));
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.server.getRecipeManager().byKey(new ResourceLocation("armor_trims:anvil_cop")).ifPresent(_rec -> _serverPlayer.resetRecipes(Collections.singleton(_rec)));
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.server.getRecipeManager().byKey(new ResourceLocation("armor_trims:blast_furnace_cop")).ifPresent(_rec -> _serverPlayer.resetRecipes(Collections.singleton(_rec)));
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.server.getRecipeManager().byKey(new ResourceLocation("armor_trims:bucket_cop")).ifPresent(_rec -> _serverPlayer.resetRecipes(Collections.singleton(_rec)));
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.server.getRecipeManager().byKey(new ResourceLocation("armor_trims:cauldron_cop")).ifPresent(_rec -> _serverPlayer.resetRecipes(Collections.singleton(_rec)));
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.server.getRecipeManager().byKey(new ResourceLocation("armor_trims:chain_cop")).ifPresent(_rec -> _serverPlayer.resetRecipes(Collections.singleton(_rec)));
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.server.getRecipeManager().byKey(new ResourceLocation("armor_trims:copper_cop")).ifPresent(_rec -> _serverPlayer.resetRecipes(Collections.singleton(_rec)));
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.server.getRecipeManager().byKey(new ResourceLocation("armor_trims:crossbow_cop")).ifPresent(_rec -> _serverPlayer.resetRecipes(Collections.singleton(_rec)));
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.server.getRecipeManager().byKey(new ResourceLocation("armor_trims:detector_rail")).ifPresent(_rec -> _serverPlayer.resetRecipes(Collections.singleton(_rec)));
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.server.getRecipeManager().byKey(new ResourceLocation("armor_trims:flint_and_steel_cop")).ifPresent(_rec -> _serverPlayer.resetRecipes(Collections.singleton(_rec)));
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.server.getRecipeManager().byKey(new ResourceLocation("armor_trims:heavt_weighted_pressure_plate_cop")).ifPresent(_rec -> _serverPlayer.resetRecipes(Collections.singleton(_rec)));
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.server.getRecipeManager().byKey(new ResourceLocation("armor_trims:hopper")).ifPresent(_rec -> _serverPlayer.resetRecipes(Collections.singleton(_rec)));
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.server.getRecipeManager().byKey(new ResourceLocation("armor_trims:iron_axe_cop")).ifPresent(_rec -> _serverPlayer.resetRecipes(Collections.singleton(_rec)));
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.server.getRecipeManager().byKey(new ResourceLocation("armor_trims:iron_trapdoor_cop")).ifPresent(_rec -> _serverPlayer.resetRecipes(Collections.singleton(_rec)));
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.server.getRecipeManager().byKey(new ResourceLocation("armor_trims:iron_axe_cop")).ifPresent(_rec -> _serverPlayer.resetRecipes(Collections.singleton(_rec)));
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.server.getRecipeManager().byKey(new ResourceLocation("armor_trims:iron_axe_cop_2")).ifPresent(_rec -> _serverPlayer.resetRecipes(Collections.singleton(_rec)));
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.server.getRecipeManager().byKey(new ResourceLocation("armor_trims:iron_bars")).ifPresent(_rec -> _serverPlayer.resetRecipes(Collections.singleton(_rec)));
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.server.getRecipeManager().byKey(new ResourceLocation("armor_trims:iron_boots_cop")).ifPresent(_rec -> _serverPlayer.resetRecipes(Collections.singleton(_rec)));
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.server.getRecipeManager().byKey(new ResourceLocation("armor_trims:iron_chestplate_cop")).ifPresent(_rec -> _serverPlayer.resetRecipes(Collections.singleton(_rec)));
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.server.getRecipeManager().byKey(new ResourceLocation("armor_trims:iron_door_cop")).ifPresent(_rec -> _serverPlayer.resetRecipes(Collections.singleton(_rec)));
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.server.getRecipeManager().byKey(new ResourceLocation("armor_trims:iron_door_cop_2")).ifPresent(_rec -> _serverPlayer.resetRecipes(Collections.singleton(_rec)));
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.server.getRecipeManager().byKey(new ResourceLocation("armor_trims:iron_helmet_cop")).ifPresent(_rec -> _serverPlayer.resetRecipes(Collections.singleton(_rec)));
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.server.getRecipeManager().byKey(new ResourceLocation("armor_trims:iron_hoe_cop")).ifPresent(_rec -> _serverPlayer.resetRecipes(Collections.singleton(_rec)));
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.server.getRecipeManager().byKey(new ResourceLocation("armor_trims:iron_hoe_cop_2")).ifPresent(_rec -> _serverPlayer.resetRecipes(Collections.singleton(_rec)));
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.server.getRecipeManager().byKey(new ResourceLocation("armor_trims:iron_leggings_cop")).ifPresent(_rec -> _serverPlayer.resetRecipes(Collections.singleton(_rec)));
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.server.getRecipeManager().byKey(new ResourceLocation("armor_trims:iron_nugget")).ifPresent(_rec -> _serverPlayer.resetRecipes(Collections.singleton(_rec)));
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.server.getRecipeManager().byKey(new ResourceLocation("armor_trims:iron_sword_cop")).ifPresent(_rec -> _serverPlayer.resetRecipes(Collections.singleton(_rec)));
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.server.getRecipeManager().byKey(new ResourceLocation("armor_trims:minecraft_cop")).ifPresent(_rec -> _serverPlayer.resetRecipes(Collections.singleton(_rec)));
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.server.getRecipeManager().byKey(new ResourceLocation("armor_trims:piston_cop")).ifPresent(_rec -> _serverPlayer.resetRecipes(Collections.singleton(_rec)));
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.server.getRecipeManager().byKey(new ResourceLocation("armor_trims:rail_cop")).ifPresent(_rec -> _serverPlayer.resetRecipes(Collections.singleton(_rec)));
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.server.getRecipeManager().byKey(new ResourceLocation("armor_trims:shears_cop")).ifPresent(_rec -> _serverPlayer.resetRecipes(Collections.singleton(_rec)));
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.server.getRecipeManager().byKey(new ResourceLocation("armor_trims:shield_cop")).ifPresent(_rec -> _serverPlayer.resetRecipes(Collections.singleton(_rec)));
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.server.getRecipeManager().byKey(new ResourceLocation("armor_trims:smithing_table_cop")).ifPresent(_rec -> _serverPlayer.resetRecipes(Collections.singleton(_rec)));
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.server.getRecipeManager().byKey(new ResourceLocation("armor_trims:stonecutter_cop")).ifPresent(_rec -> _serverPlayer.resetRecipes(Collections.singleton(_rec)));
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.server.getRecipeManager().byKey(new ResourceLocation("armor_trims:tripwire_hook")).ifPresent(_rec -> _serverPlayer.resetRecipes(Collections.singleton(_rec)));
		}
	}
}
