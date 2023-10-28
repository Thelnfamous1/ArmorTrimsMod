package com.marwinekk.armortrims.procedures;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.SimpleContainer;
import net.minecraft.tags.ItemTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

import java.util.concurrent.atomic.AtomicReference;

import com.marwinekk.armortrims.network.ArmorTrimsModVariables;

@Mod.EventBusSubscriber
public class QuartzPassiveProcedure {
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			execute(event, event.player.level(), event.player);
		}
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).quartz) {
			{
				AtomicReference<IItemHandler> _iitemhandlerref = new AtomicReference<>();
				entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(_iitemhandlerref::set);
				if (_iitemhandlerref.get() != null) {
					for (int _idx = 0; _idx < _iitemhandlerref.get().getSlots(); _idx++) {
						ItemStack itemstackiterator = _iitemhandlerref.get().getStackInSlot(_idx).copy();
						if (itemstackiterator.is(ItemTags.create(new ResourceLocation("armor_trims:smelters"))) && world instanceof Level _level3
								&& _level3.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(itemstackiterator), _level3).isPresent()) {
							if (entity instanceof Player _player) {
								ItemStack _stktoremove = itemstackiterator;
								_player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), itemstackiterator.getCount(), _player.inventoryMenu.getCraftSlots());
							}
							for (int index0 = 0; index0 < itemstackiterator.getCount(); index0++) {
								if (world instanceof ServerLevel _level) {
									ItemEntity entityToSpawn = new ItemEntity(_level, (entity.getX()), (entity.getY()), (entity.getZ()),
											(world instanceof Level _lvlSmeltResult
													? _lvlSmeltResult.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(itemstackiterator), _lvlSmeltResult)
															.map(recipe -> recipe.getResultItem(_lvlSmeltResult.registryAccess()).copy()).orElse(ItemStack.EMPTY)
													: ItemStack.EMPTY));
									entityToSpawn.setPickUpDelay(0);
									_level.addFreshEntity(entityToSpawn);
								}
							}
						}
					}
				}
			}
		}
	}
}
