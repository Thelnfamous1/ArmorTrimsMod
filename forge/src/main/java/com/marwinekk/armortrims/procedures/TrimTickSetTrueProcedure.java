package com.marwinekk.armortrims.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import javax.annotation.Nullable;

import java.util.Map;

import com.marwinekk.armortrims.network.ArmorTrimsModVariables;

@Mod.EventBusSubscriber
public class TrimTickSetTrueProcedure {
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			execute(event, event.player);
		}
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

	private static void execute(@Nullable Event event, Entity entity) {
		if (entity == null)
			return;
		double copies = 0;
		if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).dragonegg == false) {
			if (!((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).getItem() == Blocks.AIR.asItem())
					&& !((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.CHEST) : ItemStack.EMPTY).getItem() == Blocks.AIR.asItem())
					&& !((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY).getItem() == Blocks.AIR.asItem())
					&& !((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY).getItem() == Blocks.AIR.asItem())) {
				if (!(new Object() {
					public String getTrimMaterial() {
						CompoundTag dataIndex0 = new CompoundTag();
						entity.saveWithoutId(dataIndex0);
						ListTag inventory = dataIndex0.getList("Inventory", 10);
						for (int i = 0; i < inventory.size(); i++) {
							CompoundTag item = inventory.getCompound(i);
							byte slot = item.getByte("Slot");
							if (slot == 103) {
								return item.getCompound("tag").getCompound("Trim").getString("material");
							}
						}
						return null;
					}
				}.getTrimMaterial()).equals("null") && !(new Object() {
					public String getTrimMaterial() {
						CompoundTag dataIndex0 = new CompoundTag();
						entity.saveWithoutId(dataIndex0);
						ListTag inventory = dataIndex0.getList("Inventory", 10);
						for (int i = 0; i < inventory.size(); i++) {
							CompoundTag item = inventory.getCompound(i);
							byte slot = item.getByte("Slot");
							if (slot == 102) {
								return item.getCompound("tag").getCompound("Trim").getString("material");
							}
						}
						return null;
					}
				}.getTrimMaterial()).equals("null") && !(new Object() {
					public String getTrimMaterial() {
						CompoundTag dataIndex0 = new CompoundTag();
						entity.saveWithoutId(dataIndex0);
						ListTag inventory = dataIndex0.getList("Inventory", 10);
						for (int i = 0; i < inventory.size(); i++) {
							CompoundTag item = inventory.getCompound(i);
							byte slot = item.getByte("Slot");
							if (slot == 101) {
								return item.getCompound("tag").getCompound("Trim").getString("material");
							}
						}
						return null;
					}
				}.getTrimMaterial()).equals("null") && !(new Object() {
					public String getTrimMaterial() {
						CompoundTag dataIndex0 = new CompoundTag();
						entity.saveWithoutId(dataIndex0);
						ListTag inventory = dataIndex0.getList("Inventory", 10);
						for (int i = 0; i < inventory.size(); i++) {
							CompoundTag item = inventory.getCompound(i);
							byte slot = item.getByte("Slot");
							if (slot == 100) {
								return item.getCompound("tag").getCompound("Trim").getString("material");
							}
						}
						return null;
					}
				}.getTrimMaterial()).equals("null")) {
					if ((new Object() {
						public String getTrimMaterial() {
							CompoundTag dataIndex0 = new CompoundTag();
							entity.saveWithoutId(dataIndex0);
							ListTag inventory = dataIndex0.getList("Inventory", 10);
							for (int i = 0; i < inventory.size(); i++) {
								CompoundTag item = inventory.getCompound(i);
								byte slot = item.getByte("Slot");
								if (slot == 103) {
									return item.getCompound("tag").getCompound("Trim").getString("material");
								}
							}
							return null;
						}
					}.getTrimMaterial()).equals("minecraft:iron") && (new Object() {
						public String getTrimMaterial() {
							CompoundTag dataIndex0 = new CompoundTag();
							entity.saveWithoutId(dataIndex0);
							ListTag inventory = dataIndex0.getList("Inventory", 10);
							for (int i = 0; i < inventory.size(); i++) {
								CompoundTag item = inventory.getCompound(i);
								byte slot = item.getByte("Slot");
								if (slot == 102) {
									return item.getCompound("tag").getCompound("Trim").getString("material");
								}
							}
							return null;
						}
					}.getTrimMaterial()).equals("minecraft:iron") && (new Object() {
						public String getTrimMaterial() {
							CompoundTag dataIndex0 = new CompoundTag();
							entity.saveWithoutId(dataIndex0);
							ListTag inventory = dataIndex0.getList("Inventory", 10);
							for (int i = 0; i < inventory.size(); i++) {
								CompoundTag item = inventory.getCompound(i);
								byte slot = item.getByte("Slot");
								if (slot == 101) {
									return item.getCompound("tag").getCompound("Trim").getString("material");
								}
							}
							return null;
						}
					}.getTrimMaterial()).equals("minecraft:iron") && (new Object() {
						public String getTrimMaterial() {
							CompoundTag dataIndex0 = new CompoundTag();
							entity.saveWithoutId(dataIndex0);
							ListTag inventory = dataIndex0.getList("Inventory", 10);
							for (int i = 0; i < inventory.size(); i++) {
								CompoundTag item = inventory.getCompound(i);
								byte slot = item.getByte("Slot");
								if (slot == 100) {
									return item.getCompound("tag").getCompound("Trim").getString("material");
								}
							}
							return null;
						}
					}.getTrimMaterial()).equals("minecraft:iron")) {
						if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).iron == false) {
							{
								boolean _setval = true;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.iron = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							{
								double _setval = 0;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.timer = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
						}
					} else if ((new Object() {
						public String getTrimMaterial() {
							CompoundTag dataIndex0 = new CompoundTag();
							entity.saveWithoutId(dataIndex0);
							ListTag inventory = dataIndex0.getList("Inventory", 10);
							for (int i = 0; i < inventory.size(); i++) {
								CompoundTag item = inventory.getCompound(i);
								byte slot = item.getByte("Slot");
								if (slot == 103) {
									return item.getCompound("tag").getCompound("Trim").getString("material");
								}
							}
							return null;
						}
					}.getTrimMaterial()).equals("minecraft:lapis") && (new Object() {
						public String getTrimMaterial() {
							CompoundTag dataIndex0 = new CompoundTag();
							entity.saveWithoutId(dataIndex0);
							ListTag inventory = dataIndex0.getList("Inventory", 10);
							for (int i = 0; i < inventory.size(); i++) {
								CompoundTag item = inventory.getCompound(i);
								byte slot = item.getByte("Slot");
								if (slot == 102) {
									return item.getCompound("tag").getCompound("Trim").getString("material");
								}
							}
							return null;
						}
					}.getTrimMaterial()).equals("minecraft:lapis") && (new Object() {
						public String getTrimMaterial() {
							CompoundTag dataIndex0 = new CompoundTag();
							entity.saveWithoutId(dataIndex0);
							ListTag inventory = dataIndex0.getList("Inventory", 10);
							for (int i = 0; i < inventory.size(); i++) {
								CompoundTag item = inventory.getCompound(i);
								byte slot = item.getByte("Slot");
								if (slot == 101) {
									return item.getCompound("tag").getCompound("Trim").getString("material");
								}
							}
							return null;
						}
					}.getTrimMaterial()).equals("minecraft:lapis") && (new Object() {
						public String getTrimMaterial() {
							CompoundTag dataIndex0 = new CompoundTag();
							entity.saveWithoutId(dataIndex0);
							ListTag inventory = dataIndex0.getList("Inventory", 10);
							for (int i = 0; i < inventory.size(); i++) {
								CompoundTag item = inventory.getCompound(i);
								byte slot = item.getByte("Slot");
								if (slot == 100) {
									return item.getCompound("tag").getCompound("Trim").getString("material");
								}
							}
							return null;
						}
					}.getTrimMaterial()).equals("minecraft:lapis")) {
						if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).lapis == false) {
							{
								double _setval = 0;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.timer = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							{
								boolean _setval = true;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.lapis = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
						}
					} else if ((new Object() {
						public String getTrimMaterial() {
							CompoundTag dataIndex0 = new CompoundTag();
							entity.saveWithoutId(dataIndex0);
							ListTag inventory = dataIndex0.getList("Inventory", 10);
							for (int i = 0; i < inventory.size(); i++) {
								CompoundTag item = inventory.getCompound(i);
								byte slot = item.getByte("Slot");
								if (slot == 103) {
									return item.getCompound("tag").getCompound("Trim").getString("material");
								}
							}
							return null;
						}
					}.getTrimMaterial()).equals("minecraft:gold") && (new Object() {
						public String getTrimMaterial() {
							CompoundTag dataIndex0 = new CompoundTag();
							entity.saveWithoutId(dataIndex0);
							ListTag inventory = dataIndex0.getList("Inventory", 10);
							for (int i = 0; i < inventory.size(); i++) {
								CompoundTag item = inventory.getCompound(i);
								byte slot = item.getByte("Slot");
								if (slot == 102) {
									return item.getCompound("tag").getCompound("Trim").getString("material");
								}
							}
							return null;
						}
					}.getTrimMaterial()).equals("minecraft:gold") && (new Object() {
						public String getTrimMaterial() {
							CompoundTag dataIndex0 = new CompoundTag();
							entity.saveWithoutId(dataIndex0);
							ListTag inventory = dataIndex0.getList("Inventory", 10);
							for (int i = 0; i < inventory.size(); i++) {
								CompoundTag item = inventory.getCompound(i);
								byte slot = item.getByte("Slot");
								if (slot == 101) {
									return item.getCompound("tag").getCompound("Trim").getString("material");
								}
							}
							return null;
						}
					}.getTrimMaterial()).equals("minecraft:gold") && (new Object() {
						public String getTrimMaterial() {
							CompoundTag dataIndex0 = new CompoundTag();
							entity.saveWithoutId(dataIndex0);
							ListTag inventory = dataIndex0.getList("Inventory", 10);
							for (int i = 0; i < inventory.size(); i++) {
								CompoundTag item = inventory.getCompound(i);
								byte slot = item.getByte("Slot");
								if (slot == 100) {
									return item.getCompound("tag").getCompound("Trim").getString("material");
								}
							}
							return null;
						}
					}.getTrimMaterial()).equals("minecraft:gold")) {
						if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).gold == false) {
							{
								boolean _setval = true;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.gold = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							{
								Entity _ent = entity;
								if (!_ent.level().isClientSide() && _ent.getServer() != null) {
									_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null,
											4, _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "attribute @s minecraft:generic.max_health base set 24");
								}
							}
							{
								double _setval = 0;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.timer = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
						}
					} else if ((new Object() {
						public String getTrimMaterial() {
							CompoundTag dataIndex0 = new CompoundTag();
							entity.saveWithoutId(dataIndex0);
							ListTag inventory = dataIndex0.getList("Inventory", 10);
							for (int i = 0; i < inventory.size(); i++) {
								CompoundTag item = inventory.getCompound(i);
								byte slot = item.getByte("Slot");
								if (slot == 103) {
									return item.getCompound("tag").getCompound("Trim").getString("material");
								}
							}
							return null;
						}
					}.getTrimMaterial()).equals("minecraft:amethyst") && (new Object() {
						public String getTrimMaterial() {
							CompoundTag dataIndex0 = new CompoundTag();
							entity.saveWithoutId(dataIndex0);
							ListTag inventory = dataIndex0.getList("Inventory", 10);
							for (int i = 0; i < inventory.size(); i++) {
								CompoundTag item = inventory.getCompound(i);
								byte slot = item.getByte("Slot");
								if (slot == 102) {
									return item.getCompound("tag").getCompound("Trim").getString("material");
								}
							}
							return null;
						}
					}.getTrimMaterial()).equals("minecraft:amethyst") && (new Object() {
						public String getTrimMaterial() {
							CompoundTag dataIndex0 = new CompoundTag();
							entity.saveWithoutId(dataIndex0);
							ListTag inventory = dataIndex0.getList("Inventory", 10);
							for (int i = 0; i < inventory.size(); i++) {
								CompoundTag item = inventory.getCompound(i);
								byte slot = item.getByte("Slot");
								if (slot == 101) {
									return item.getCompound("tag").getCompound("Trim").getString("material");
								}
							}
							return null;
						}
					}.getTrimMaterial()).equals("minecraft:amethyst") && (new Object() {
						public String getTrimMaterial() {
							CompoundTag dataIndex0 = new CompoundTag();
							entity.saveWithoutId(dataIndex0);
							ListTag inventory = dataIndex0.getList("Inventory", 10);
							for (int i = 0; i < inventory.size(); i++) {
								CompoundTag item = inventory.getCompound(i);
								byte slot = item.getByte("Slot");
								if (slot == 100) {
									return item.getCompound("tag").getCompound("Trim").getString("material");
								}
							}
							return null;
						}
					}.getTrimMaterial()).equals("minecraft:amethyst")) {
						if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).amethyst == false) {
							{
								boolean _setval = true;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.amethyst = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							{
								double _setval = 0;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.timer = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
						}
					} else if ((new Object() {
						public String getTrimMaterial() {
							CompoundTag dataIndex0 = new CompoundTag();
							entity.saveWithoutId(dataIndex0);
							ListTag inventory = dataIndex0.getList("Inventory", 10);
							for (int i = 0; i < inventory.size(); i++) {
								CompoundTag item = inventory.getCompound(i);
								byte slot = item.getByte("Slot");
								if (slot == 103) {
									return item.getCompound("tag").getCompound("Trim").getString("material");
								}
							}
							return null;
						}
					}.getTrimMaterial()).equals("minecraft:diamond") && (new Object() {
						public String getTrimMaterial() {
							CompoundTag dataIndex0 = new CompoundTag();
							entity.saveWithoutId(dataIndex0);
							ListTag inventory = dataIndex0.getList("Inventory", 10);
							for (int i = 0; i < inventory.size(); i++) {
								CompoundTag item = inventory.getCompound(i);
								byte slot = item.getByte("Slot");
								if (slot == 102) {
									return item.getCompound("tag").getCompound("Trim").getString("material");
								}
							}
							return null;
						}
					}.getTrimMaterial()).equals("minecraft:diamond") && (new Object() {
						public String getTrimMaterial() {
							CompoundTag dataIndex0 = new CompoundTag();
							entity.saveWithoutId(dataIndex0);
							ListTag inventory = dataIndex0.getList("Inventory", 10);
							for (int i = 0; i < inventory.size(); i++) {
								CompoundTag item = inventory.getCompound(i);
								byte slot = item.getByte("Slot");
								if (slot == 101) {
									return item.getCompound("tag").getCompound("Trim").getString("material");
								}
							}
							return null;
						}
					}.getTrimMaterial()).equals("minecraft:diamond") && (new Object() {
						public String getTrimMaterial() {
							CompoundTag dataIndex0 = new CompoundTag();
							entity.saveWithoutId(dataIndex0);
							ListTag inventory = dataIndex0.getList("Inventory", 10);
							for (int i = 0; i < inventory.size(); i++) {
								CompoundTag item = inventory.getCompound(i);
								byte slot = item.getByte("Slot");
								if (slot == 100) {
									return item.getCompound("tag").getCompound("Trim").getString("material");
								}
							}
							return null;
						}
					}.getTrimMaterial()).equals("minecraft:diamond")) {
						if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).diamond == false) {
							{
								boolean _setval = true;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.diamond = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							{
								double _setval = 0;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.timer = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							{
								Map<Enchantment, Integer> _enchantments = EnchantmentHelper.getEnchantments((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY));
								if (_enchantments.containsKey(Enchantments.UNBREAKING)) {
									_enchantments.remove(Enchantments.UNBREAKING);
									EnchantmentHelper.setEnchantments(_enchantments, (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY));
								}
							}
							{
								Map<Enchantment, Integer> _enchantments = EnchantmentHelper.getEnchantments((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.CHEST) : ItemStack.EMPTY));
								if (_enchantments.containsKey(Enchantments.UNBREAKING)) {
									_enchantments.remove(Enchantments.UNBREAKING);
									EnchantmentHelper.setEnchantments(_enchantments, (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.CHEST) : ItemStack.EMPTY));
								}
							}
							{
								Map<Enchantment, Integer> _enchantments = EnchantmentHelper.getEnchantments((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY));
								if (_enchantments.containsKey(Enchantments.UNBREAKING)) {
									_enchantments.remove(Enchantments.UNBREAKING);
									EnchantmentHelper.setEnchantments(_enchantments, (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY));
								}
							}
							{
								Map<Enchantment, Integer> _enchantments = EnchantmentHelper.getEnchantments((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY));
								if (_enchantments.containsKey(Enchantments.UNBREAKING)) {
									_enchantments.remove(Enchantments.UNBREAKING);
									EnchantmentHelper.setEnchantments(_enchantments, (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY));
								}
							}
							(entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).enchant(Enchantments.UNBREAKING, 4);
							(entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.CHEST) : ItemStack.EMPTY).enchant(Enchantments.UNBREAKING, 4);
							(entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY).enchant(Enchantments.UNBREAKING, 4);
							(entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY).enchant(Enchantments.UNBREAKING, 4);
						}
					} else if ((new Object() {
						public String getTrimMaterial() {
							CompoundTag dataIndex0 = new CompoundTag();
							entity.saveWithoutId(dataIndex0);
							ListTag inventory = dataIndex0.getList("Inventory", 10);
							for (int i = 0; i < inventory.size(); i++) {
								CompoundTag item = inventory.getCompound(i);
								byte slot = item.getByte("Slot");
								if (slot == 103) {
									return item.getCompound("tag").getCompound("Trim").getString("material");
								}
							}
							return null;
						}
					}.getTrimMaterial()).equals("minecraft:netherite") && (new Object() {
						public String getTrimMaterial() {
							CompoundTag dataIndex0 = new CompoundTag();
							entity.saveWithoutId(dataIndex0);
							ListTag inventory = dataIndex0.getList("Inventory", 10);
							for (int i = 0; i < inventory.size(); i++) {
								CompoundTag item = inventory.getCompound(i);
								byte slot = item.getByte("Slot");
								if (slot == 102) {
									return item.getCompound("tag").getCompound("Trim").getString("material");
								}
							}
							return null;
						}
					}.getTrimMaterial()).equals("minecraft:netherite") && (new Object() {
						public String getTrimMaterial() {
							CompoundTag dataIndex0 = new CompoundTag();
							entity.saveWithoutId(dataIndex0);
							ListTag inventory = dataIndex0.getList("Inventory", 10);
							for (int i = 0; i < inventory.size(); i++) {
								CompoundTag item = inventory.getCompound(i);
								byte slot = item.getByte("Slot");
								if (slot == 101) {
									return item.getCompound("tag").getCompound("Trim").getString("material");
								}
							}
							return null;
						}
					}.getTrimMaterial()).equals("minecraft:netherite") && (new Object() {
						public String getTrimMaterial() {
							CompoundTag dataIndex0 = new CompoundTag();
							entity.saveWithoutId(dataIndex0);
							ListTag inventory = dataIndex0.getList("Inventory", 10);
							for (int i = 0; i < inventory.size(); i++) {
								CompoundTag item = inventory.getCompound(i);
								byte slot = item.getByte("Slot");
								if (slot == 100) {
									return item.getCompound("tag").getCompound("Trim").getString("material");
								}
							}
							return null;
						}
					}.getTrimMaterial()).equals("minecraft:netherite")) {
						if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).netherite == false) {
							{
								boolean _setval = true;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.netherite = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							{
								double _setval = 0;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.timer = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
						}
					} else if ((new Object() {
						public String getTrimMaterial() {
							CompoundTag dataIndex0 = new CompoundTag();
							entity.saveWithoutId(dataIndex0);
							ListTag inventory = dataIndex0.getList("Inventory", 10);
							for (int i = 0; i < inventory.size(); i++) {
								CompoundTag item = inventory.getCompound(i);
								byte slot = item.getByte("Slot");
								if (slot == 103) {
									return item.getCompound("tag").getCompound("Trim").getString("material");
								}
							}
							return null;
						}
					}.getTrimMaterial()).equals("minecraft:redstone") && (new Object() {
						public String getTrimMaterial() {
							CompoundTag dataIndex0 = new CompoundTag();
							entity.saveWithoutId(dataIndex0);
							ListTag inventory = dataIndex0.getList("Inventory", 10);
							for (int i = 0; i < inventory.size(); i++) {
								CompoundTag item = inventory.getCompound(i);
								byte slot = item.getByte("Slot");
								if (slot == 102) {
									return item.getCompound("tag").getCompound("Trim").getString("material");
								}
							}
							return null;
						}
					}.getTrimMaterial()).equals("minecraft:redstone") && (new Object() {
						public String getTrimMaterial() {
							CompoundTag dataIndex0 = new CompoundTag();
							entity.saveWithoutId(dataIndex0);
							ListTag inventory = dataIndex0.getList("Inventory", 10);
							for (int i = 0; i < inventory.size(); i++) {
								CompoundTag item = inventory.getCompound(i);
								byte slot = item.getByte("Slot");
								if (slot == 101) {
									return item.getCompound("tag").getCompound("Trim").getString("material");
								}
							}
							return null;
						}
					}.getTrimMaterial()).equals("minecraft:redstone") && (new Object() {
						public String getTrimMaterial() {
							CompoundTag dataIndex0 = new CompoundTag();
							entity.saveWithoutId(dataIndex0);
							ListTag inventory = dataIndex0.getList("Inventory", 10);
							for (int i = 0; i < inventory.size(); i++) {
								CompoundTag item = inventory.getCompound(i);
								byte slot = item.getByte("Slot");
								if (slot == 100) {
									return item.getCompound("tag").getCompound("Trim").getString("material");
								}
							}
							return null;
						}
					}.getTrimMaterial()).equals("minecraft:redstone")) {
						if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).redstone == false) {
							{
								boolean _setval = true;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.redstone = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							{
								double _setval = 0;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.timer = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							(entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).enchant(Enchantments.BLAST_PROTECTION, 4);
							(entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.CHEST) : ItemStack.EMPTY).enchant(Enchantments.BLAST_PROTECTION, 4);
							(entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY).enchant(Enchantments.BLAST_PROTECTION, 4);
							(entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY).enchant(Enchantments.BLAST_PROTECTION, 4);
						}
					} else if ((new Object() {
						public String getTrimMaterial() {
							CompoundTag dataIndex0 = new CompoundTag();
							entity.saveWithoutId(dataIndex0);
							ListTag inventory = dataIndex0.getList("Inventory", 10);
							for (int i = 0; i < inventory.size(); i++) {
								CompoundTag item = inventory.getCompound(i);
								byte slot = item.getByte("Slot");
								if (slot == 103) {
									return item.getCompound("tag").getCompound("Trim").getString("material");
								}
							}
							return null;
						}
					}.getTrimMaterial()).equals("minecraft:copper") && (new Object() {
						public String getTrimMaterial() {
							CompoundTag dataIndex0 = new CompoundTag();
							entity.saveWithoutId(dataIndex0);
							ListTag inventory = dataIndex0.getList("Inventory", 10);
							for (int i = 0; i < inventory.size(); i++) {
								CompoundTag item = inventory.getCompound(i);
								byte slot = item.getByte("Slot");
								if (slot == 102) {
									return item.getCompound("tag").getCompound("Trim").getString("material");
								}
							}
							return null;
						}
					}.getTrimMaterial()).equals("minecraft:copper") && (new Object() {
						public String getTrimMaterial() {
							CompoundTag dataIndex0 = new CompoundTag();
							entity.saveWithoutId(dataIndex0);
							ListTag inventory = dataIndex0.getList("Inventory", 10);
							for (int i = 0; i < inventory.size(); i++) {
								CompoundTag item = inventory.getCompound(i);
								byte slot = item.getByte("Slot");
								if (slot == 101) {
									return item.getCompound("tag").getCompound("Trim").getString("material");
								}
							}
							return null;
						}
					}.getTrimMaterial()).equals("minecraft:copper") && (new Object() {
						public String getTrimMaterial() {
							CompoundTag dataIndex0 = new CompoundTag();
							entity.saveWithoutId(dataIndex0);
							ListTag inventory = dataIndex0.getList("Inventory", 10);
							for (int i = 0; i < inventory.size(); i++) {
								CompoundTag item = inventory.getCompound(i);
								byte slot = item.getByte("Slot");
								if (slot == 100) {
									return item.getCompound("tag").getCompound("Trim").getString("material");
								}
							}
							return null;
						}
					}.getTrimMaterial()).equals("minecraft:copper")) {
						if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).copper == false) {
							{
								boolean _setval = true;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.copper = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							{
								double _setval = 0;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.timer = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
						}
					} else if ((new Object() {
						public String getTrimMaterial() {
							CompoundTag dataIndex0 = new CompoundTag();
							entity.saveWithoutId(dataIndex0);
							ListTag inventory = dataIndex0.getList("Inventory", 10);
							for (int i = 0; i < inventory.size(); i++) {
								CompoundTag item = inventory.getCompound(i);
								byte slot = item.getByte("Slot");
								if (slot == 103) {
									return item.getCompound("tag").getCompound("Trim").getString("material");
								}
							}
							return null;
						}
					}.getTrimMaterial()).equals("minecraft:quartz") && (new Object() {
						public String getTrimMaterial() {
							CompoundTag dataIndex0 = new CompoundTag();
							entity.saveWithoutId(dataIndex0);
							ListTag inventory = dataIndex0.getList("Inventory", 10);
							for (int i = 0; i < inventory.size(); i++) {
								CompoundTag item = inventory.getCompound(i);
								byte slot = item.getByte("Slot");
								if (slot == 102) {
									return item.getCompound("tag").getCompound("Trim").getString("material");
								}
							}
							return null;
						}
					}.getTrimMaterial()).equals("minecraft:quartz") && (new Object() {
						public String getTrimMaterial() {
							CompoundTag dataIndex0 = new CompoundTag();
							entity.saveWithoutId(dataIndex0);
							ListTag inventory = dataIndex0.getList("Inventory", 10);
							for (int i = 0; i < inventory.size(); i++) {
								CompoundTag item = inventory.getCompound(i);
								byte slot = item.getByte("Slot");
								if (slot == 101) {
									return item.getCompound("tag").getCompound("Trim").getString("material");
								}
							}
							return null;
						}
					}.getTrimMaterial()).equals("minecraft:quartz") && (new Object() {
						public String getTrimMaterial() {
							CompoundTag dataIndex0 = new CompoundTag();
							entity.saveWithoutId(dataIndex0);
							ListTag inventory = dataIndex0.getList("Inventory", 10);
							for (int i = 0; i < inventory.size(); i++) {
								CompoundTag item = inventory.getCompound(i);
								byte slot = item.getByte("Slot");
								if (slot == 100) {
									return item.getCompound("tag").getCompound("Trim").getString("material");
								}
							}
							return null;
						}
					}.getTrimMaterial()).equals("minecraft:quartz")) {
						if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).quartz == false) {
							{
								boolean _setval = true;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.quartz = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							{
								double _setval = 0;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.timer = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
						}
					} else if ((new Object() {
						public String getTrimMaterial() {
							CompoundTag dataIndex0 = new CompoundTag();
							entity.saveWithoutId(dataIndex0);
							ListTag inventory = dataIndex0.getList("Inventory", 10);
							for (int i = 0; i < inventory.size(); i++) {
								CompoundTag item = inventory.getCompound(i);
								byte slot = item.getByte("Slot");
								if (slot == 103) {
									return item.getCompound("tag").getCompound("Trim").getString("material");
								}
							}
							return null;
						}
					}.getTrimMaterial()).equals("minecraft:emerald") && (new Object() {
						public String getTrimMaterial() {
							CompoundTag dataIndex0 = new CompoundTag();
							entity.saveWithoutId(dataIndex0);
							ListTag inventory = dataIndex0.getList("Inventory", 10);
							for (int i = 0; i < inventory.size(); i++) {
								CompoundTag item = inventory.getCompound(i);
								byte slot = item.getByte("Slot");
								if (slot == 102) {
									return item.getCompound("tag").getCompound("Trim").getString("material");
								}
							}
							return null;
						}
					}.getTrimMaterial()).equals("minecraft:emerald") && (new Object() {
						public String getTrimMaterial() {
							CompoundTag dataIndex0 = new CompoundTag();
							entity.saveWithoutId(dataIndex0);
							ListTag inventory = dataIndex0.getList("Inventory", 10);
							for (int i = 0; i < inventory.size(); i++) {
								CompoundTag item = inventory.getCompound(i);
								byte slot = item.getByte("Slot");
								if (slot == 101) {
									return item.getCompound("tag").getCompound("Trim").getString("material");
								}
							}
							return null;
						}
					}.getTrimMaterial()).equals("minecraft:emerald") && (new Object() {
						public String getTrimMaterial() {
							CompoundTag dataIndex0 = new CompoundTag();
							entity.saveWithoutId(dataIndex0);
							ListTag inventory = dataIndex0.getList("Inventory", 10);
							for (int i = 0; i < inventory.size(); i++) {
								CompoundTag item = inventory.getCompound(i);
								byte slot = item.getByte("Slot");
								if (slot == 100) {
									return item.getCompound("tag").getCompound("Trim").getString("material");
								}
							}
							return null;
						}
					}.getTrimMaterial()).equals("minecraft:emerald")) {
						if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).emerald == false) {
							{
								boolean _setval = true;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.emerald = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							{
								double _setval = 0;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.timer = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
						}
					} else {
						if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).iron == true) {
							{
								boolean _setval = false;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.iron = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
						}
						if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).lapis == true) {
							{
								boolean _setval = false;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.lapis = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
						}
						if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).gold == true) {
							{
								boolean _setval = false;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.gold = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
						}
						if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).amethyst == true) {
							{
								boolean _setval = false;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.amethyst = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
						}
						if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).diamond == true) {
							{
								boolean _setval = false;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.diamond = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
						}
						if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).netherite == true) {
							{
								boolean _setval = false;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.netherite = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
						}
						if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).redstone == true) {
							{
								boolean _setval = false;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.redstone = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
						}
						if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).copper == true) {
							{
								boolean _setval = false;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.copper = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
						}
						if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).quartz == true) {
							{
								boolean _setval = false;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.quartz = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
						}
						if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).emerald == true) {
							{
								boolean _setval = false;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.emerald = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
						}
					}
				}
			} else if ((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).getItem() == Blocks.AIR.asItem()
					|| (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.CHEST) : ItemStack.EMPTY).getItem() == Blocks.AIR.asItem()
					|| (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY).getItem() == Blocks.AIR.asItem()
					|| (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY).getItem() == Blocks.AIR.asItem()) {
				{
					double _setval = 0;
					entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
						capability.timer = _setval;
						capability.syncPlayerVariables(entity);
					});
				}
				if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).iron == true) {
					{
						boolean _setval = false;
						entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
							capability.iron = _setval;
							capability.syncPlayerVariables(entity);
						});
					}
					{
						Entity _ent = entity;
						if (!_ent.level().isClientSide() && _ent.getServer() != null) {
							_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
									_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "attribute @s minecraft:generic.knockback_resistance base set 0");
						}
					}
				}
				if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).lapis == true) {
					{
						boolean _setval = false;
						entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
							capability.lapis = _setval;
							capability.syncPlayerVariables(entity);
						});
					}
				}
				if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).gold == true) {
					{
						boolean _setval = false;
						entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
							capability.gold = _setval;
							capability.syncPlayerVariables(entity);
						});
					}
					{
						Entity _ent = entity;
						if (!_ent.level().isClientSide() && _ent.getServer() != null) {
							_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
									_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "attribute @s minecraft:generic.max_health base set 20");
						}
					}
				}
				if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).amethyst == true) {
					{
						boolean _setval = false;
						entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
							capability.amethyst = _setval;
							capability.syncPlayerVariables(entity);
						});
					}
				}
				if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).diamond == true) {
					{
						boolean _setval = false;
						entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
							capability.diamond = _setval;
							capability.syncPlayerVariables(entity);
						});
					}
				}
				if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).netherite == true) {
					{
						boolean _setval = false;
						entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
							capability.netherite = _setval;
							capability.syncPlayerVariables(entity);
						});
					}
				}
				if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).redstone == true) {
					{
						boolean _setval = false;
						entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
							capability.redstone = _setval;
							capability.syncPlayerVariables(entity);
						});
					}
				}
				if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).copper == true) {
					{
						boolean _setval = false;
						entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
							capability.copper = _setval;
							capability.syncPlayerVariables(entity);
						});
					}
				}
				if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).quartz == true) {
					{
						boolean _setval = false;
						entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
							capability.quartz = _setval;
							capability.syncPlayerVariables(entity);
						});
					}
				}
				if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).emerald == true) {
					{
						boolean _setval = false;
						entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
							capability.emerald = _setval;
							capability.syncPlayerVariables(entity);
						});
					}
					if (entity instanceof LivingEntity _entity)
						_entity.removeAllEffects();
				}
			}
		}
	}
}
