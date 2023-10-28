package com.marwinekk.armortrims.procedures;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Comparator;

import com.marwinekk.armortrims.network.ArmorTrimsModVariables;
import com.marwinekk.armortrims.init.ArmorTrimsModParticleTypes;
import com.marwinekk.armortrims.init.ArmorTrimsModEntities;
import com.marwinekk.armortrims.entity.TNTArrowEntity;
import com.marwinekk.armortrims.entity.PiercingArrowEntity;
import com.marwinekk.armortrims.ArmorTrimsModForge;

public class DragonCombatProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables()).dragonegg) {
			if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).combatpressed == 1) {
				if (((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).helmet).contains("iron")) {
					if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseIron) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("(\u00A7c!\u00A7f) Iron Fists \u00A7a\u00A7o[active]"), true);
						{
							boolean _setval = true;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.cooldownIron = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						{
							Entity _ent = entity;
							if (!_ent.level().isClientSide() && _ent.getServer() != null) {
								_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
										_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "attribute @s minecraft:generic.knockback_resistance base set 1");
							}
						}
						{
							boolean _setval = false;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.canUseIron = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						ArmorTrimsModForge.queueServerWork(300, () -> {
							{
								Entity _ent = entity;
								if (!_ent.level().isClientSide() && _ent.getServer() != null) {
									_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null,
											4, _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "attribute @s minecraft:generic.knockback_resistance base set 0");
								}
							}
							{
								boolean _setval = false;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.cooldownIron = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							{
								double _setval = 900 / 20;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.timer = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							ArmorTrimsModForge.queueServerWork(900, () -> {
								{
									boolean _setval = true;
									entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
										capability.canUseIron = _setval;
										capability.syncPlayerVariables(entity);
									});
								}
							});
						});
					} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseIron) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet!"), true);
					}
				} else if (((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).helmet).contains("lapis")) {
					if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseLapis) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("(\u00A7c!\u00A7f) Lapis Enchantments \u00A7a\u00A7o[active]"), true);
						{
							boolean _setval = false;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.canUseLapis = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						if (entity instanceof Player _player && !_player.level().isClientSide()) {
							for (int i = 0; i < _player.getInventory().getContainerSize(); i++) {
								ItemStack itemstackiterator = _player.getInventory().getItem(i);
								if (itemstackiterator.isEnchanted()) {
									Map<Enchantment, Integer> existingEnchantments = EnchantmentHelper.getEnchantments(itemstackiterator);
									Map<Enchantment, Integer> newEnchantments = new HashMap<>();
									existingEnchantments.forEach((enchantment, level) -> {
										newEnchantments.put(enchantment, level + 1); // Incrementing the enchantment level by 1
									});
									EnchantmentHelper.setEnchantments(newEnchantments, itemstackiterator); // Setting the updated enchantments
									itemstackiterator.getOrCreateTag().putBoolean("tagName", true); // Adding the tag
								}
							}
						}
						ArmorTrimsModForge.queueServerWork(600, () -> {
							if (entity instanceof Player _player && !_player.level().isClientSide()) {
								for (int i = 0; i < _player.getInventory().getContainerSize(); i++) {
									ItemStack itemstackiterator = _player.getInventory().getItem(i);
									if (itemstackiterator.isEnchanted()) {
										Map<Enchantment, Integer> existingEnchantments = EnchantmentHelper.getEnchantments(itemstackiterator);
										Map<Enchantment, Integer> newEnchantments = new HashMap<>();
										existingEnchantments.forEach((enchantment, level) -> {
											newEnchantments.put(enchantment, Math.max(level - 1, 0)); // Decrementing the enchantment level by 1, ensuring it stays non-negative
										});
										EnchantmentHelper.setEnchantments(newEnchantments, itemstackiterator); // Setting the updated enchantments
									}
								}
							}
							{
								double _setval = 940 / 20;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.timer = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							ArmorTrimsModForge.queueServerWork(940, () -> {
								{
									boolean _setval = true;
									entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
										capability.canUseLapis = _setval;
										capability.syncPlayerVariables(entity);
									});
								}
							});
						});
					} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseLapis) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet!"), true);
					}
				} else if (((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).helmet).contains("gold")) {
					if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseGold) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("(\u00A7c!\u00A7f) Brutes Summoned \u00A7a\u00A7o[active]"), true);
						for (int index0 = 0; index0 < Mth.nextInt(RandomSource.create(), 5, 6); index0++) {
							if (world instanceof ServerLevel _level) {
								Entity entityToSpawn = ArmorTrimsModEntities.BRUTE.get().spawn(_level, BlockPos.containing(entity.getX(), entity.getY(), entity.getZ()), MobSpawnType.MOB_SUMMONED);
								if (entityToSpawn != null) {
								}
							}
						}
						{
							boolean _setval = false;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.canUseGold = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						{
							double _setval = 900 / 20;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.timer = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						ArmorTrimsModForge.queueServerWork(900, () -> {
							{
								boolean _setval = true;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.canUseGold = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
						});
					} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseGold) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet!"), true);
					}
				} else if (((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).helmet).contains("amethyst")) {
					if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseAmethyst) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("(\u00A7c!\u00A7f) Ally Witch summoned \u00A7a\u00A7o[active]"), true);
						if (world instanceof ServerLevel _level) {
							Entity entityToSpawn = ArmorTrimsModEntities.ALLY_WITCH.get().spawn(_level, BlockPos.containing(x, y, z), MobSpawnType.MOB_SUMMONED);
							if (entityToSpawn != null) {
								entityToSpawn.setYRot(world.getRandom().nextFloat() * 360F);
							}
						}
						if (world instanceof ServerLevel _level) {
							Entity entityToSpawn = ArmorTrimsModEntities.ALLY_WITCH.get().spawn(_level, BlockPos.containing(x, y, z), MobSpawnType.MOB_SUMMONED);
							if (entityToSpawn != null) {
								entityToSpawn.setYRot(world.getRandom().nextFloat() * 360F);
							}
						}
						{
							double _setval = 1200 / 20;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.timer = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						{
							boolean _setval = false;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.canUseAmethyst = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						ArmorTrimsModForge.queueServerWork(1200, () -> {
							{
								boolean _setval = true;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.canUseAmethyst = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
						});
					} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseAmethyst) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet!"), true);
					}
				} else if (((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).helmet).contains("diamond")) {
					if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseDiamond) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("(\u00A7c!\u00A7f) Piercing Arrow Shot \u00A7a\u00A7o[active]"), true);
						{
							Entity _shootFrom = entity;
							Level projectileLevel = _shootFrom.level();
							if (!projectileLevel.isClientSide()) {
								Projectile _entityToSpawn = new Object() {
									public Projectile getArrow(Level level, Entity shooter, float damage, int knockback) {
										AbstractArrow entityToSpawn = new PiercingArrowEntity(ArmorTrimsModEntities.PIERCING_ARROW.get(), level);
										entityToSpawn.setOwner(shooter);
										entityToSpawn.setBaseDamage(damage);
										entityToSpawn.setKnockback(knockback);
										entityToSpawn.setSilent(true);
										entityToSpawn.setCritArrow(true);
										return entityToSpawn;
									}
								}.getArrow(projectileLevel, entity, 8, 0);
								_entityToSpawn.setPos(_shootFrom.getX(), _shootFrom.getEyeY() - 0.1, _shootFrom.getZ());
								_entityToSpawn.shoot(_shootFrom.getLookAngle().x, _shootFrom.getLookAngle().y, _shootFrom.getLookAngle().z, 3, 0);
								projectileLevel.addFreshEntity(_entityToSpawn);
							}
						}
						if (world instanceof Level _level) {
							if (!_level.isClientSide()) {
								_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.arrow.shoot")), SoundSource.NEUTRAL, 1, 1);
							} else {
								_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.arrow.shoot")), SoundSource.NEUTRAL, 1, 1, false);
							}
						}
						entity.getPersistentData().putDouble("counter1", (entity.getPersistentData().getDouble("counter1") + 1));
						if (entity.getPersistentData().getDouble("counter1") % 3 == 0) {
							{
								boolean _setval = false;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.canUseDiamond = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							{
								double _setval = 700 / 20;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.timer = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							ArmorTrimsModForge.queueServerWork(700, () -> {
								{
									boolean _setval = true;
									entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
										capability.canUseDiamond = _setval;
										capability.syncPlayerVariables(entity);
									});
								}
							});
						}
					} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseDiamond) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet!"), true);
					}
				} else if (((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).helmet).contains("redstone")) {
					if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseRedstone) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("(\u00A7c!\u00A7f) Homing Arrow Deployed \u00A7a\u00A7o[active]"), true);
						{
							Entity _shootFrom = entity;
							Level projectileLevel = _shootFrom.level();
							if (!projectileLevel.isClientSide()) {
								Projectile _entityToSpawn = new Object() {
									public Projectile getArrow(Level level, Entity shooter, float damage, int knockback) {
										AbstractArrow entityToSpawn = new TNTArrowEntity(ArmorTrimsModEntities.TNT_ARROW.get(), level);
										entityToSpawn.setOwner(shooter);
										entityToSpawn.setBaseDamage(damage);
										entityToSpawn.setKnockback(knockback);
										entityToSpawn.setSilent(true);
										entityToSpawn.setCritArrow(true);
										return entityToSpawn;
									}
								}.getArrow(projectileLevel, entity, 2, 0);
								_entityToSpawn.setPos(_shootFrom.getX(), _shootFrom.getEyeY() - 0.1, _shootFrom.getZ());
								_entityToSpawn.shoot(_shootFrom.getLookAngle().x, _shootFrom.getLookAngle().y, _shootFrom.getLookAngle().z, (float) 1.5, 0);
								projectileLevel.addFreshEntity(_entityToSpawn);
							}
						}
						if (world instanceof Level _level) {
							if (!_level.isClientSide()) {
								_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.arrow.shoot")), SoundSource.NEUTRAL, 1, 1);
							} else {
								_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.arrow.shoot")), SoundSource.NEUTRAL, 1, 1, false);
							}
						}
						if (world instanceof Level _level) {
							if (!_level.isClientSide()) {
								_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.tnt.primed")), SoundSource.NEUTRAL, 1, 1);
							} else {
								_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.tnt.primed")), SoundSource.NEUTRAL, 1, 1, false);
							}
						}
						entity.getPersistentData().putDouble("counter2", (entity.getPersistentData().getDouble("counter2") + 1));
						if (entity.getPersistentData().getDouble("counter2") % 4 == 0) {
							{
								boolean _setval = false;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.canUseRedstone = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							{
								double _setval = 900 / 20;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.timer = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							ArmorTrimsModForge.queueServerWork(900, () -> {
								{
									boolean _setval = true;
									entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
										capability.canUseRedstone = _setval;
										capability.syncPlayerVariables(entity);
									});
								}
							});
						}
					} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseRedstone) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet!"), true);
					}
				} else if (((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).helmet).contains("copper")) {
					if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseCopper) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("(\u00A7c!\u00A7f) Lightning Strike \u00A7a\u00A7o[active]"), true);
						for (int index1 = 0; index1 < 3; index1++) {
							if (world instanceof ServerLevel _level) {
								LightningBolt entityToSpawn = EntityType.LIGHTNING_BOLT.create(_level);
								entityToSpawn.moveTo(Vec3.atBottomCenterOf(new BlockPos(
										entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(128)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos()
												.getX(),
										entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(128)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos()
												.getY(),
										entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(128)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos()
												.getZ())));
								_level.addFreshEntity(entityToSpawn);
							}
						}
						{
							final Vec3 _center = new Vec3(
									(entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(128)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getX()),
									(entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(128)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getY()),
									(entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(128)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos()
											.getZ()));
							List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
							for (Entity entityiterator : _entfound) {
								if (entityiterator instanceof LivingEntity && !(entity == entityiterator)) {
									if (entityiterator instanceof LivingEntity _entity && !_entity.level().isClientSide())
										_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 0, true, true));
									if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION, (entityiterator instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY)) != 0
											|| EnchantmentHelper.getItemEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION,
													(entityiterator instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.CHEST) : ItemStack.EMPTY)) != 0
											|| EnchantmentHelper.getItemEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION,
													(entityiterator instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY)) != 0
											|| EnchantmentHelper.getItemEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION,
													(entityiterator instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY)) != 0) {
										entityiterator.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC)), 8);
									}
								}
							}
						}
						entity.getPersistentData().putDouble("counter3", (entity.getPersistentData().getDouble("counter3") + 1));
						if (entity.getPersistentData().getDouble("counter3") % 10 == 0) {
							{
								boolean _setval = false;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.canUseCopper = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							{
								double _setval = 900 / 20;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.timer = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							ArmorTrimsModForge.queueServerWork(900, () -> {
								{
									boolean _setval = true;
									entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
										capability.canUseCopper = _setval;
										capability.syncPlayerVariables(entity);
									});
								}
							});
						}
					} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseCopper) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet!"), true);
					}
				} else if (((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).helmet).contains("quartz")) {
					if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseQuartz) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("(\u00A7c!\u00A7f) Smoke Cloud \u00A7a\u00A7o[active]"), true);
						{
							Entity _ent = entity;
							if (!_ent.level().isClientSide() && _ent.getServer() != null) {
								_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
										_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "/effect give @p trueinvis:true_invis 12 10 true");
							}
						}
						{
							final Vec3 _center = new Vec3(x, y, z);
							List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(32 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
							for (Entity entityiterator : _entfound) {
								if (!(entityiterator == entity) && entityiterator instanceof LivingEntity) {
									if (entityiterator instanceof LivingEntity _entity && !_entity.level().isClientSide())
										_entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 300, 9, false, false));
								}
							}
						}
						int horizontalRadiusHemiTop = 10 - 1;
						int verticalRadiusHemiTop = 7;
						int yIterationsHemiTop = verticalRadiusHemiTop;
						for (int i = 0; i < yIterationsHemiTop; i++) {
							if (i == verticalRadiusHemiTop) {
								continue;
							}
							for (int xi = -horizontalRadiusHemiTop; xi <= horizontalRadiusHemiTop; xi++) {
								for (int zi = -horizontalRadiusHemiTop; zi <= horizontalRadiusHemiTop; zi++) {
									double distanceSq = (xi * xi) / (double) (horizontalRadiusHemiTop * horizontalRadiusHemiTop) + (i * i) / (double) (verticalRadiusHemiTop * verticalRadiusHemiTop)
											+ (zi * zi) / (double) (horizontalRadiusHemiTop * horizontalRadiusHemiTop);
									if (distanceSq <= 1.0) {
										if (world instanceof ServerLevel _level)
											_level.sendParticles(ArmorTrimsModParticleTypes.POOFPOOF.get(), x + xi, (y + i + 0), z + zi, 100, 1, 1, 1, 0.05);
									}
								}
							}
						}
						{
							boolean _setval = false;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.canUseQuartz = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						{
							double _setval = 1200 / 20;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.timer = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						ArmorTrimsModForge.queueServerWork(1200, () -> {
							{
								boolean _setval = true;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.canUseQuartz = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
						});
					} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseQuartz) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet!"), true);
					}
				} else if (((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).helmet).contains("emerald")) {
					if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseEmerald) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("(\u00A7c!\u00A7f) Armor Totem \u00A7a\u00A7o[active 15 seconds]"), true);
						{
							boolean _setval = true;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.died = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						{
							boolean _setval = false;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.didntDie = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						{
							boolean _setval = false;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.canUseEmerald = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						ArmorTrimsModForge.queueServerWork(300, () -> {
							if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).died) {
								{
									boolean _setval = false;
									entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
										capability.died = _setval;
										capability.syncPlayerVariables(entity);
									});
								}
								{
									boolean _setval = true;
									entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
										capability.didntDie = _setval;
										capability.syncPlayerVariables(entity);
									});
								}
								ArmorTrimsModForge.queueServerWork(6000, () -> {
									{
										boolean _setval = true;
										entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
											capability.canUseEmerald = _setval;
											capability.syncPlayerVariables(entity);
										});
									}
								});
							} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).died) {
								ArmorTrimsModForge.queueServerWork(72000, () -> {
									{
										boolean _setval = true;
										entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
											capability.canUseEmerald = _setval;
											capability.syncPlayerVariables(entity);
										});
									}
								});
							}
						});
					} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseEmerald) {
						if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).didntDie) {
							if (entity instanceof Player _player && !_player.level().isClientSide())
								_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet! \u00A7f\u00A7o[5 min cooldown]"), true);
						} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).didntDie) {
							if (entity instanceof Player _player && !_player.level().isClientSide())
								_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet! \u00A7f\u00A7o[26min cooldown]"), true);
						}
					}
				} else if (((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).helmet).contains("netherite")) {
					if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseNetherite) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("(\u00A7c!\u00A7f) Wither Touch \u00A7a\u00A7o[active]"), true);
						{
							boolean _setval = true;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.cooldownNetherite = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						{
							boolean _setval = false;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.canUseNetherite = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						ArmorTrimsModForge.queueServerWork(100, () -> {
							{
								boolean _setval = false;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.cooldownNetherite = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							{
								double _setval = 1100 / 20;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.timer = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							ArmorTrimsModForge.queueServerWork(900, () -> {
								{
									boolean _setval = true;
									entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
										capability.canUseNetherite = _setval;
										capability.syncPlayerVariables(entity);
									});
								}
							});
						});
					} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseNetherite) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet!"), true);
					}
				}
			}
			if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).combatpressed == 2) {
				if (((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).chestplate).contains("iron")) {
					if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseIron) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("(\u00A7c!\u00A7f) Iron Fists \u00A7a\u00A7o[active]"), true);
						{
							boolean _setval = true;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.cooldownIron = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						{
							Entity _ent = entity;
							if (!_ent.level().isClientSide() && _ent.getServer() != null) {
								_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
										_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "attribute @s minecraft:generic.knockback_resistance base set 1");
							}
						}
						{
							boolean _setval = false;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.canUseIron = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						ArmorTrimsModForge.queueServerWork(300, () -> {
							{
								Entity _ent = entity;
								if (!_ent.level().isClientSide() && _ent.getServer() != null) {
									_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null,
											4, _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "attribute @s minecraft:generic.knockback_resistance base set 0");
								}
							}
							{
								boolean _setval = false;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.cooldownIron = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							{
								double _setval = 900 / 20;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.timer = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							ArmorTrimsModForge.queueServerWork(900, () -> {
								{
									boolean _setval = true;
									entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
										capability.canUseIron = _setval;
										capability.syncPlayerVariables(entity);
									});
								}
							});
						});
					} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseIron) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet!"), true);
					}
				} else if (((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).chestplate).contains("lapis")) {
					if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseLapis) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("(\u00A7c!\u00A7f) Lapis Enchantments \u00A7a\u00A7o[active]"), true);
						{
							boolean _setval = false;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.canUseLapis = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						if (entity instanceof Player _player && !_player.level().isClientSide()) {
							for (int i = 0; i < _player.getInventory().getContainerSize(); i++) {
								ItemStack itemstackiterator = _player.getInventory().getItem(i);
								if (itemstackiterator.isEnchanted()) {
									Map<Enchantment, Integer> existingEnchantments = EnchantmentHelper.getEnchantments(itemstackiterator);
									Map<Enchantment, Integer> newEnchantments = new HashMap<>();
									existingEnchantments.forEach((enchantment, level) -> {
										newEnchantments.put(enchantment, level + 1); // Incrementing the enchantment level by 1
									});
									EnchantmentHelper.setEnchantments(newEnchantments, itemstackiterator); // Setting the updated enchantments
									itemstackiterator.getOrCreateTag().putBoolean("tagName", true); // Adding the tag
								}
							}
						}
						ArmorTrimsModForge.queueServerWork(600, () -> {
							if (entity instanceof Player _player && !_player.level().isClientSide()) {
								for (int i = 0; i < _player.getInventory().getContainerSize(); i++) {
									ItemStack itemstackiterator = _player.getInventory().getItem(i);
									if (itemstackiterator.isEnchanted()) {
										Map<Enchantment, Integer> existingEnchantments = EnchantmentHelper.getEnchantments(itemstackiterator);
										Map<Enchantment, Integer> newEnchantments = new HashMap<>();
										existingEnchantments.forEach((enchantment, level) -> {
											newEnchantments.put(enchantment, Math.max(level - 1, 0)); // Decrementing the enchantment level by 1, ensuring it stays non-negative
										});
										EnchantmentHelper.setEnchantments(newEnchantments, itemstackiterator); // Setting the updated enchantments
									}
								}
							}
							{
								double _setval = 940 / 20;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.timer = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							ArmorTrimsModForge.queueServerWork(940, () -> {
								{
									boolean _setval = true;
									entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
										capability.canUseLapis = _setval;
										capability.syncPlayerVariables(entity);
									});
								}
							});
						});
					} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseLapis) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet!"), true);
					}
				} else if (((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).chestplate).contains("gold")) {
					if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseGold) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("(\u00A7c!\u00A7f) Brutes Summoned \u00A7a\u00A7o[active]"), true);
						for (int index2 = 0; index2 < Mth.nextInt(RandomSource.create(), 5, 6); index2++) {
							if (world instanceof ServerLevel _level) {
								Entity entityToSpawn = ArmorTrimsModEntities.BRUTE.get().spawn(_level, BlockPos.containing(entity.getX(), entity.getY(), entity.getZ()), MobSpawnType.MOB_SUMMONED);
								if (entityToSpawn != null) {
								}
							}
						}
						{
							boolean _setval = false;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.canUseGold = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						{
							double _setval = 900 / 20;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.timer = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						ArmorTrimsModForge.queueServerWork(900, () -> {
							{
								boolean _setval = true;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.canUseGold = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
						});
					} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseGold) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet!"), true);
					}
				} else if (((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).chestplate).contains("amethyst")) {
					if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseAmethyst) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("(\u00A7c!\u00A7f) Ally Witch summoned \u00A7a\u00A7o[active]"), true);
						if (world instanceof ServerLevel _level) {
							Entity entityToSpawn = ArmorTrimsModEntities.ALLY_WITCH.get().spawn(_level, BlockPos.containing(x, y, z), MobSpawnType.MOB_SUMMONED);
							if (entityToSpawn != null) {
								entityToSpawn.setYRot(world.getRandom().nextFloat() * 360F);
							}
						}
						if (world instanceof ServerLevel _level) {
							Entity entityToSpawn = ArmorTrimsModEntities.ALLY_WITCH.get().spawn(_level, BlockPos.containing(x, y, z), MobSpawnType.MOB_SUMMONED);
							if (entityToSpawn != null) {
								entityToSpawn.setYRot(world.getRandom().nextFloat() * 360F);
							}
						}
						{
							double _setval = 1200 / 20;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.timer = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						{
							boolean _setval = false;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.canUseAmethyst = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						ArmorTrimsModForge.queueServerWork(1200, () -> {
							{
								boolean _setval = true;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.canUseAmethyst = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
						});
					} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseAmethyst) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet!"), true);
					}
				} else if (((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).chestplate).contains("diamond")) {
					if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseDiamond) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("(\u00A7c!\u00A7f) Piercing Arrow Shot \u00A7a\u00A7o[active]"), true);
						{
							Entity _shootFrom = entity;
							Level projectileLevel = _shootFrom.level();
							if (!projectileLevel.isClientSide()) {
								Projectile _entityToSpawn = new Object() {
									public Projectile getArrow(Level level, Entity shooter, float damage, int knockback) {
										AbstractArrow entityToSpawn = new PiercingArrowEntity(ArmorTrimsModEntities.PIERCING_ARROW.get(), level);
										entityToSpawn.setOwner(shooter);
										entityToSpawn.setBaseDamage(damage);
										entityToSpawn.setKnockback(knockback);
										entityToSpawn.setSilent(true);
										entityToSpawn.setCritArrow(true);
										return entityToSpawn;
									}
								}.getArrow(projectileLevel, entity, 8, 0);
								_entityToSpawn.setPos(_shootFrom.getX(), _shootFrom.getEyeY() - 0.1, _shootFrom.getZ());
								_entityToSpawn.shoot(_shootFrom.getLookAngle().x, _shootFrom.getLookAngle().y, _shootFrom.getLookAngle().z, 3, 0);
								projectileLevel.addFreshEntity(_entityToSpawn);
							}
						}
						if (world instanceof Level _level) {
							if (!_level.isClientSide()) {
								_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.arrow.shoot")), SoundSource.NEUTRAL, 1, 1);
							} else {
								_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.arrow.shoot")), SoundSource.NEUTRAL, 1, 1, false);
							}
						}
						entity.getPersistentData().putDouble("counter1", (entity.getPersistentData().getDouble("counter1") + 1));
						if (entity.getPersistentData().getDouble("counter1") % 3 == 0) {
							{
								boolean _setval = false;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.canUseDiamond = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							{
								double _setval = 700 / 20;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.timer = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							ArmorTrimsModForge.queueServerWork(700, () -> {
								{
									boolean _setval = true;
									entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
										capability.canUseDiamond = _setval;
										capability.syncPlayerVariables(entity);
									});
								}
							});
						}
					} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseDiamond) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet!"), true);
					}
				} else if (((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).chestplate).contains("redstone")) {
					if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseRedstone) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("(\u00A7c!\u00A7f) Homing Arrow Deployed \u00A7a\u00A7o[active]"), true);
						{
							Entity _shootFrom = entity;
							Level projectileLevel = _shootFrom.level();
							if (!projectileLevel.isClientSide()) {
								Projectile _entityToSpawn = new Object() {
									public Projectile getArrow(Level level, Entity shooter, float damage, int knockback) {
										AbstractArrow entityToSpawn = new TNTArrowEntity(ArmorTrimsModEntities.TNT_ARROW.get(), level);
										entityToSpawn.setOwner(shooter);
										entityToSpawn.setBaseDamage(damage);
										entityToSpawn.setKnockback(knockback);
										entityToSpawn.setSilent(true);
										entityToSpawn.setCritArrow(true);
										return entityToSpawn;
									}
								}.getArrow(projectileLevel, entity, 2, 0);
								_entityToSpawn.setPos(_shootFrom.getX(), _shootFrom.getEyeY() - 0.1, _shootFrom.getZ());
								_entityToSpawn.shoot(_shootFrom.getLookAngle().x, _shootFrom.getLookAngle().y, _shootFrom.getLookAngle().z, (float) 1.5, 0);
								projectileLevel.addFreshEntity(_entityToSpawn);
							}
						}
						if (world instanceof Level _level) {
							if (!_level.isClientSide()) {
								_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.arrow.shoot")), SoundSource.NEUTRAL, 1, 1);
							} else {
								_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.arrow.shoot")), SoundSource.NEUTRAL, 1, 1, false);
							}
						}
						if (world instanceof Level _level) {
							if (!_level.isClientSide()) {
								_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.tnt.primed")), SoundSource.NEUTRAL, 1, 1);
							} else {
								_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.tnt.primed")), SoundSource.NEUTRAL, 1, 1, false);
							}
						}
						entity.getPersistentData().putDouble("counter2", (entity.getPersistentData().getDouble("counter2") + 1));
						if (entity.getPersistentData().getDouble("counter2") % 4 == 0) {
							{
								boolean _setval = false;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.canUseRedstone = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							{
								double _setval = 900 / 20;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.timer = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							ArmorTrimsModForge.queueServerWork(900, () -> {
								{
									boolean _setval = true;
									entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
										capability.canUseRedstone = _setval;
										capability.syncPlayerVariables(entity);
									});
								}
							});
						}
					} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseRedstone) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet!"), true);
					}
				} else if (((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).chestplate).contains("copper")) {
					if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseCopper) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("(\u00A7c!\u00A7f) Lightning Strike \u00A7a\u00A7o[active]"), true);
						for (int index3 = 0; index3 < 3; index3++) {
							if (world instanceof ServerLevel _level) {
								LightningBolt entityToSpawn = EntityType.LIGHTNING_BOLT.create(_level);
								entityToSpawn.moveTo(Vec3.atBottomCenterOf(new BlockPos(
										entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(128)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos()
												.getX(),
										entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(128)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos()
												.getY(),
										entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(128)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos()
												.getZ())));
								_level.addFreshEntity(entityToSpawn);
							}
						}
						{
							final Vec3 _center = new Vec3(
									(entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(128)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getX()),
									(entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(128)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getY()),
									(entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(128)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos()
											.getZ()));
							List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
							for (Entity entityiterator : _entfound) {
								if (entityiterator instanceof LivingEntity && !(entity == entityiterator)) {
									if (entityiterator instanceof LivingEntity _entity && !_entity.level().isClientSide())
										_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 0, true, true));
									if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION, (entityiterator instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY)) != 0
											|| EnchantmentHelper.getItemEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION,
													(entityiterator instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.CHEST) : ItemStack.EMPTY)) != 0
											|| EnchantmentHelper.getItemEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION,
													(entityiterator instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY)) != 0
											|| EnchantmentHelper.getItemEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION,
													(entityiterator instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY)) != 0) {
										entityiterator.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC)), 8);
									}
								}
							}
						}
						entity.getPersistentData().putDouble("counter3", (entity.getPersistentData().getDouble("counter3") + 1));
						if (entity.getPersistentData().getDouble("counter3") % 10 == 0) {
							{
								boolean _setval = false;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.canUseCopper = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							{
								double _setval = 900 / 20;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.timer = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							ArmorTrimsModForge.queueServerWork(900, () -> {
								{
									boolean _setval = true;
									entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
										capability.canUseCopper = _setval;
										capability.syncPlayerVariables(entity);
									});
								}
							});
						}
					} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseCopper) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet!"), true);
					}
				} else if (((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).chestplate).contains("quartz")) {
					if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseQuartz) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("(\u00A7c!\u00A7f) Smoke Cloud \u00A7a\u00A7o[active]"), true);
						{
							Entity _ent = entity;
							if (!_ent.level().isClientSide() && _ent.getServer() != null) {
								_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
										_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "/effect give @p trueinvis:true_invis 12 10 true");
							}
						}
						{
							final Vec3 _center = new Vec3(x, y, z);
							List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(32 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
							for (Entity entityiterator : _entfound) {
								if (!(entityiterator == entity) && entityiterator instanceof LivingEntity) {
									if (entityiterator instanceof LivingEntity _entity && !_entity.level().isClientSide())
										_entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 300, 9, false, false));
								}
							}
						}
						int horizontalRadiusHemiTop = 10 - 1;
						int verticalRadiusHemiTop = 7;
						int yIterationsHemiTop = verticalRadiusHemiTop;
						for (int i = 0; i < yIterationsHemiTop; i++) {
							if (i == verticalRadiusHemiTop) {
								continue;
							}
							for (int xi = -horizontalRadiusHemiTop; xi <= horizontalRadiusHemiTop; xi++) {
								for (int zi = -horizontalRadiusHemiTop; zi <= horizontalRadiusHemiTop; zi++) {
									double distanceSq = (xi * xi) / (double) (horizontalRadiusHemiTop * horizontalRadiusHemiTop) + (i * i) / (double) (verticalRadiusHemiTop * verticalRadiusHemiTop)
											+ (zi * zi) / (double) (horizontalRadiusHemiTop * horizontalRadiusHemiTop);
									if (distanceSq <= 1.0) {
										if (world instanceof ServerLevel _level)
											_level.sendParticles(ArmorTrimsModParticleTypes.POOFPOOF.get(), x + xi, (y + i + 0), z + zi, 100, 1, 1, 1, 0.05);
									}
								}
							}
						}
						{
							boolean _setval = false;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.canUseQuartz = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						{
							double _setval = 1200 / 20;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.timer = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						ArmorTrimsModForge.queueServerWork(1200, () -> {
							{
								boolean _setval = true;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.canUseQuartz = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
						});
					} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseQuartz) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet!"), true);
					}
				} else if (((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).chestplate).contains("emerald")) {
					if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseEmerald) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("(\u00A7c!\u00A7f) Armor Totem \u00A7a\u00A7o[active 15 seconds]"), true);
						{
							boolean _setval = true;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.died = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						{
							boolean _setval = false;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.didntDie = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						{
							boolean _setval = false;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.canUseEmerald = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						ArmorTrimsModForge.queueServerWork(300, () -> {
							if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).died) {
								{
									boolean _setval = false;
									entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
										capability.died = _setval;
										capability.syncPlayerVariables(entity);
									});
								}
								{
									boolean _setval = true;
									entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
										capability.didntDie = _setval;
										capability.syncPlayerVariables(entity);
									});
								}
								ArmorTrimsModForge.queueServerWork(6000, () -> {
									{
										boolean _setval = true;
										entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
											capability.canUseEmerald = _setval;
											capability.syncPlayerVariables(entity);
										});
									}
								});
							} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).died) {
								ArmorTrimsModForge.queueServerWork(72000, () -> {
									{
										boolean _setval = true;
										entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
											capability.canUseEmerald = _setval;
											capability.syncPlayerVariables(entity);
										});
									}
								});
							}
						});
					} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseEmerald) {
						if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).didntDie) {
							if (entity instanceof Player _player && !_player.level().isClientSide())
								_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet! \u00A7f\u00A7o[5 min cooldown]"), true);
						} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).didntDie) {
							if (entity instanceof Player _player && !_player.level().isClientSide())
								_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet! \u00A7f\u00A7o[26min cooldown]"), true);
						}
					}
				} else if (((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).chestplate).contains("netherite")) {
					if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseNetherite) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("(\u00A7c!\u00A7f) Wither Touch \u00A7a\u00A7o[active]"), true);
						{
							boolean _setval = true;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.cooldownNetherite = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						{
							boolean _setval = false;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.canUseNetherite = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						ArmorTrimsModForge.queueServerWork(100, () -> {
							{
								boolean _setval = false;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.cooldownNetherite = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							{
								double _setval = 1100 / 20;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.timer = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							ArmorTrimsModForge.queueServerWork(900, () -> {
								{
									boolean _setval = true;
									entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
										capability.canUseNetherite = _setval;
										capability.syncPlayerVariables(entity);
									});
								}
							});
						});
					} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseNetherite) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet!"), true);
					}
				}
			}
			if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).combatpressed == 3) {
				if (((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).leggings).contains("iron")) {
					if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseIron) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("(\u00A7c!\u00A7f) Iron Fists \u00A7a\u00A7o[active]"), true);
						{
							boolean _setval = true;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.cooldownIron = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						{
							Entity _ent = entity;
							if (!_ent.level().isClientSide() && _ent.getServer() != null) {
								_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
										_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "attribute @s minecraft:generic.knockback_resistance base set 1");
							}
						}
						{
							boolean _setval = false;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.canUseIron = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						ArmorTrimsModForge.queueServerWork(300, () -> {
							{
								Entity _ent = entity;
								if (!_ent.level().isClientSide() && _ent.getServer() != null) {
									_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null,
											4, _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "attribute @s minecraft:generic.knockback_resistance base set 0");
								}
							}
							{
								boolean _setval = false;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.cooldownIron = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							{
								double _setval = 900 / 20;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.timer = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							ArmorTrimsModForge.queueServerWork(900, () -> {
								{
									boolean _setval = true;
									entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
										capability.canUseIron = _setval;
										capability.syncPlayerVariables(entity);
									});
								}
							});
						});
					} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseIron) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet!"), true);
					}
				} else if (((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).leggings).contains("lapis")) {
					if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseLapis) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("(\u00A7c!\u00A7f) Lapis Enchantments \u00A7a\u00A7o[active]"), true);
						{
							boolean _setval = false;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.canUseLapis = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						if (entity instanceof Player _player && !_player.level().isClientSide()) {
							for (int i = 0; i < _player.getInventory().getContainerSize(); i++) {
								ItemStack itemstackiterator = _player.getInventory().getItem(i);
								if (itemstackiterator.isEnchanted()) {
									Map<Enchantment, Integer> existingEnchantments = EnchantmentHelper.getEnchantments(itemstackiterator);
									Map<Enchantment, Integer> newEnchantments = new HashMap<>();
									existingEnchantments.forEach((enchantment, level) -> {
										newEnchantments.put(enchantment, level + 1); // Incrementing the enchantment level by 1
									});
									EnchantmentHelper.setEnchantments(newEnchantments, itemstackiterator); // Setting the updated enchantments
									itemstackiterator.getOrCreateTag().putBoolean("tagName", true); // Adding the tag
								}
							}
						}
						ArmorTrimsModForge.queueServerWork(600, () -> {
							if (entity instanceof Player _player && !_player.level().isClientSide()) {
								for (int i = 0; i < _player.getInventory().getContainerSize(); i++) {
									ItemStack itemstackiterator = _player.getInventory().getItem(i);
									if (itemstackiterator.isEnchanted()) {
										Map<Enchantment, Integer> existingEnchantments = EnchantmentHelper.getEnchantments(itemstackiterator);
										Map<Enchantment, Integer> newEnchantments = new HashMap<>();
										existingEnchantments.forEach((enchantment, level) -> {
											newEnchantments.put(enchantment, Math.max(level - 1, 0)); // Decrementing the enchantment level by 1, ensuring it stays non-negative
										});
										EnchantmentHelper.setEnchantments(newEnchantments, itemstackiterator); // Setting the updated enchantments
									}
								}
							}
							{
								double _setval = 940 / 20;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.timer = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							ArmorTrimsModForge.queueServerWork(940, () -> {
								{
									boolean _setval = true;
									entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
										capability.canUseLapis = _setval;
										capability.syncPlayerVariables(entity);
									});
								}
							});
						});
					} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseLapis) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet!"), true);
					}
				} else if (((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).leggings).contains("gold")) {
					if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseGold) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("(\u00A7c!\u00A7f) Brutes Summoned \u00A7a\u00A7o[active]"), true);
						for (int index4 = 0; index4 < Mth.nextInt(RandomSource.create(), 5, 6); index4++) {
							if (world instanceof ServerLevel _level) {
								Entity entityToSpawn = ArmorTrimsModEntities.BRUTE.get().spawn(_level, BlockPos.containing(entity.getX(), entity.getY(), entity.getZ()), MobSpawnType.MOB_SUMMONED);
								if (entityToSpawn != null) {
								}
							}
						}
						{
							boolean _setval = false;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.canUseGold = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						{
							double _setval = 900 / 20;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.timer = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						ArmorTrimsModForge.queueServerWork(900, () -> {
							{
								boolean _setval = true;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.canUseGold = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
						});
					} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseGold) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet!"), true);
					}
				} else if (((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).leggings).contains("amethyst")) {
					if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseAmethyst) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("(\u00A7c!\u00A7f) Ally Witch summoned \u00A7a\u00A7o[active]"), true);
						if (world instanceof ServerLevel _level) {
							Entity entityToSpawn = ArmorTrimsModEntities.ALLY_WITCH.get().spawn(_level, BlockPos.containing(x, y, z), MobSpawnType.MOB_SUMMONED);
							if (entityToSpawn != null) {
								entityToSpawn.setYRot(world.getRandom().nextFloat() * 360F);
							}
						}
						if (world instanceof ServerLevel _level) {
							Entity entityToSpawn = ArmorTrimsModEntities.ALLY_WITCH.get().spawn(_level, BlockPos.containing(x, y, z), MobSpawnType.MOB_SUMMONED);
							if (entityToSpawn != null) {
								entityToSpawn.setYRot(world.getRandom().nextFloat() * 360F);
							}
						}
						{
							double _setval = 1200 / 20;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.timer = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						{
							boolean _setval = false;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.canUseAmethyst = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						ArmorTrimsModForge.queueServerWork(1200, () -> {
							{
								boolean _setval = true;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.canUseAmethyst = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
						});
					} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseAmethyst) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet!"), true);
					}
				} else if (((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).leggings).contains("diamond")) {
					if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseDiamond) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("(\u00A7c!\u00A7f) Piercing Arrow Shot \u00A7a\u00A7o[active]"), true);
						{
							Entity _shootFrom = entity;
							Level projectileLevel = _shootFrom.level();
							if (!projectileLevel.isClientSide()) {
								Projectile _entityToSpawn = new Object() {
									public Projectile getArrow(Level level, Entity shooter, float damage, int knockback) {
										AbstractArrow entityToSpawn = new PiercingArrowEntity(ArmorTrimsModEntities.PIERCING_ARROW.get(), level);
										entityToSpawn.setOwner(shooter);
										entityToSpawn.setBaseDamage(damage);
										entityToSpawn.setKnockback(knockback);
										entityToSpawn.setSilent(true);
										entityToSpawn.setCritArrow(true);
										return entityToSpawn;
									}
								}.getArrow(projectileLevel, entity, 8, 0);
								_entityToSpawn.setPos(_shootFrom.getX(), _shootFrom.getEyeY() - 0.1, _shootFrom.getZ());
								_entityToSpawn.shoot(_shootFrom.getLookAngle().x, _shootFrom.getLookAngle().y, _shootFrom.getLookAngle().z, 3, 0);
								projectileLevel.addFreshEntity(_entityToSpawn);
							}
						}
						if (world instanceof Level _level) {
							if (!_level.isClientSide()) {
								_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.arrow.shoot")), SoundSource.NEUTRAL, 1, 1);
							} else {
								_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.arrow.shoot")), SoundSource.NEUTRAL, 1, 1, false);
							}
						}
						entity.getPersistentData().putDouble("counter1", (entity.getPersistentData().getDouble("counter1") + 1));
						if (entity.getPersistentData().getDouble("counter1") % 3 == 0) {
							{
								boolean _setval = false;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.canUseDiamond = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							{
								double _setval = 700 / 20;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.timer = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							ArmorTrimsModForge.queueServerWork(700, () -> {
								{
									boolean _setval = true;
									entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
										capability.canUseDiamond = _setval;
										capability.syncPlayerVariables(entity);
									});
								}
							});
						}
					} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseDiamond) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet!"), true);
					}
				} else if (((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).leggings).contains("redstone")) {
					if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseRedstone) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("(\u00A7c!\u00A7f) Homing Arrow Deployed \u00A7a\u00A7o[active]"), true);
						{
							Entity _shootFrom = entity;
							Level projectileLevel = _shootFrom.level();
							if (!projectileLevel.isClientSide()) {
								Projectile _entityToSpawn = new Object() {
									public Projectile getArrow(Level level, Entity shooter, float damage, int knockback) {
										AbstractArrow entityToSpawn = new TNTArrowEntity(ArmorTrimsModEntities.TNT_ARROW.get(), level);
										entityToSpawn.setOwner(shooter);
										entityToSpawn.setBaseDamage(damage);
										entityToSpawn.setKnockback(knockback);
										entityToSpawn.setSilent(true);
										entityToSpawn.setCritArrow(true);
										return entityToSpawn;
									}
								}.getArrow(projectileLevel, entity, 2, 0);
								_entityToSpawn.setPos(_shootFrom.getX(), _shootFrom.getEyeY() - 0.1, _shootFrom.getZ());
								_entityToSpawn.shoot(_shootFrom.getLookAngle().x, _shootFrom.getLookAngle().y, _shootFrom.getLookAngle().z, (float) 1.5, 0);
								projectileLevel.addFreshEntity(_entityToSpawn);
							}
						}
						if (world instanceof Level _level) {
							if (!_level.isClientSide()) {
								_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.arrow.shoot")), SoundSource.NEUTRAL, 1, 1);
							} else {
								_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.arrow.shoot")), SoundSource.NEUTRAL, 1, 1, false);
							}
						}
						if (world instanceof Level _level) {
							if (!_level.isClientSide()) {
								_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.tnt.primed")), SoundSource.NEUTRAL, 1, 1);
							} else {
								_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.tnt.primed")), SoundSource.NEUTRAL, 1, 1, false);
							}
						}
						entity.getPersistentData().putDouble("counter2", (entity.getPersistentData().getDouble("counter2") + 1));
						if (entity.getPersistentData().getDouble("counter2") % 4 == 0) {
							{
								boolean _setval = false;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.canUseRedstone = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							{
								double _setval = 900 / 20;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.timer = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							ArmorTrimsModForge.queueServerWork(900, () -> {
								{
									boolean _setval = true;
									entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
										capability.canUseRedstone = _setval;
										capability.syncPlayerVariables(entity);
									});
								}
							});
						}
					} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseRedstone) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet!"), true);
					}
				} else if (((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).leggings).contains("copper")) {
					if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseCopper) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("(\u00A7c!\u00A7f) Lightning Strike \u00A7a\u00A7o[active]"), true);
						for (int index5 = 0; index5 < 3; index5++) {
							if (world instanceof ServerLevel _level) {
								LightningBolt entityToSpawn = EntityType.LIGHTNING_BOLT.create(_level);
								entityToSpawn.moveTo(Vec3.atBottomCenterOf(new BlockPos(
										entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(128)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos()
												.getX(),
										entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(128)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos()
												.getY(),
										entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(128)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos()
												.getZ())));
								_level.addFreshEntity(entityToSpawn);
							}
						}
						{
							final Vec3 _center = new Vec3(
									(entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(128)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getX()),
									(entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(128)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getY()),
									(entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(128)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos()
											.getZ()));
							List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
							for (Entity entityiterator : _entfound) {
								if (entityiterator instanceof LivingEntity && !(entity == entityiterator)) {
									if (entityiterator instanceof LivingEntity _entity && !_entity.level().isClientSide())
										_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 0, true, true));
									if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION, (entityiterator instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY)) != 0
											|| EnchantmentHelper.getItemEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION,
													(entityiterator instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.CHEST) : ItemStack.EMPTY)) != 0
											|| EnchantmentHelper.getItemEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION,
													(entityiterator instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY)) != 0
											|| EnchantmentHelper.getItemEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION,
													(entityiterator instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY)) != 0) {
										entityiterator.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC)), 8);
									}
								}
							}
						}
						entity.getPersistentData().putDouble("counter3", (entity.getPersistentData().getDouble("counter3") + 1));
						if (entity.getPersistentData().getDouble("counter3") % 10 == 0) {
							{
								boolean _setval = false;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.canUseCopper = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							{
								double _setval = 900 / 20;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.timer = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							ArmorTrimsModForge.queueServerWork(900, () -> {
								{
									boolean _setval = true;
									entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
										capability.canUseCopper = _setval;
										capability.syncPlayerVariables(entity);
									});
								}
							});
						}
					} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseCopper) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet!"), true);
					}
				} else if (((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).leggings).contains("quartz")) {
					if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseQuartz) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("(\u00A7c!\u00A7f) Smoke Cloud \u00A7a\u00A7o[active]"), true);
						{
							Entity _ent = entity;
							if (!_ent.level().isClientSide() && _ent.getServer() != null) {
								_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
										_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "/effect give @p trueinvis:true_invis 12 10 true");
							}
						}
						{
							final Vec3 _center = new Vec3(x, y, z);
							List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(32 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
							for (Entity entityiterator : _entfound) {
								if (!(entityiterator == entity) && entityiterator instanceof LivingEntity) {
									if (entityiterator instanceof LivingEntity _entity && !_entity.level().isClientSide())
										_entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 300, 9, false, false));
								}
							}
						}
						int horizontalRadiusHemiTop = 10 - 1;
						int verticalRadiusHemiTop = 7;
						int yIterationsHemiTop = verticalRadiusHemiTop;
						for (int i = 0; i < yIterationsHemiTop; i++) {
							if (i == verticalRadiusHemiTop) {
								continue;
							}
							for (int xi = -horizontalRadiusHemiTop; xi <= horizontalRadiusHemiTop; xi++) {
								for (int zi = -horizontalRadiusHemiTop; zi <= horizontalRadiusHemiTop; zi++) {
									double distanceSq = (xi * xi) / (double) (horizontalRadiusHemiTop * horizontalRadiusHemiTop) + (i * i) / (double) (verticalRadiusHemiTop * verticalRadiusHemiTop)
											+ (zi * zi) / (double) (horizontalRadiusHemiTop * horizontalRadiusHemiTop);
									if (distanceSq <= 1.0) {
										if (world instanceof ServerLevel _level)
											_level.sendParticles(ArmorTrimsModParticleTypes.POOFPOOF.get(), x + xi, (y + i + 0), z + zi, 100, 1, 1, 1, 0.05);
									}
								}
							}
						}
						{
							boolean _setval = false;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.canUseQuartz = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						{
							double _setval = 1200 / 20;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.timer = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						ArmorTrimsModForge.queueServerWork(1200, () -> {
							{
								boolean _setval = true;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.canUseQuartz = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
						});
					} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseQuartz) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet!"), true);
					}
				} else if (((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).leggings).contains("emerald")) {
					if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseEmerald) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("(\u00A7c!\u00A7f) Armor Totem \u00A7a\u00A7o[active 15 seconds]"), true);
						{
							boolean _setval = true;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.died = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						{
							boolean _setval = false;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.didntDie = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						{
							boolean _setval = false;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.canUseEmerald = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						ArmorTrimsModForge.queueServerWork(300, () -> {
							if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).died) {
								{
									boolean _setval = false;
									entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
										capability.died = _setval;
										capability.syncPlayerVariables(entity);
									});
								}
								{
									boolean _setval = true;
									entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
										capability.didntDie = _setval;
										capability.syncPlayerVariables(entity);
									});
								}
								ArmorTrimsModForge.queueServerWork(6000, () -> {
									{
										boolean _setval = true;
										entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
											capability.canUseEmerald = _setval;
											capability.syncPlayerVariables(entity);
										});
									}
								});
							} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).died) {
								ArmorTrimsModForge.queueServerWork(72000, () -> {
									{
										boolean _setval = true;
										entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
											capability.canUseEmerald = _setval;
											capability.syncPlayerVariables(entity);
										});
									}
								});
							}
						});
					} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseEmerald) {
						if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).didntDie) {
							if (entity instanceof Player _player && !_player.level().isClientSide())
								_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet! \u00A7f\u00A7o[5 min cooldown]"), true);
						} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).didntDie) {
							if (entity instanceof Player _player && !_player.level().isClientSide())
								_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet! \u00A7f\u00A7o[26min cooldown]"), true);
						}
					}
				} else if (((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).leggings).contains("netherite")) {
					if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseNetherite) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("(\u00A7c!\u00A7f) Wither Touch \u00A7a\u00A7o[active]"), true);
						{
							boolean _setval = true;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.cooldownNetherite = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						{
							boolean _setval = false;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.canUseNetherite = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						ArmorTrimsModForge.queueServerWork(100, () -> {
							{
								boolean _setval = false;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.cooldownNetherite = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							{
								double _setval = 1100 / 20;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.timer = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							ArmorTrimsModForge.queueServerWork(900, () -> {
								{
									boolean _setval = true;
									entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
										capability.canUseNetherite = _setval;
										capability.syncPlayerVariables(entity);
									});
								}
							});
						});
					} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseNetherite) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet!"), true);
					}
				}
			}
			if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).combatpressed == 4) {
				if (((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).boots).contains("iron")) {
					if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseIron) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("(\u00A7c!\u00A7f) Iron Fists \u00A7a\u00A7o[active]"), true);
						{
							boolean _setval = true;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.cooldownIron = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						{
							Entity _ent = entity;
							if (!_ent.level().isClientSide() && _ent.getServer() != null) {
								_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
										_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "attribute @s minecraft:generic.knockback_resistance base set 1");
							}
						}
						{
							boolean _setval = false;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.canUseIron = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						ArmorTrimsModForge.queueServerWork(300, () -> {
							{
								Entity _ent = entity;
								if (!_ent.level().isClientSide() && _ent.getServer() != null) {
									_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null,
											4, _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "attribute @s minecraft:generic.knockback_resistance base set 0");
								}
							}
							{
								boolean _setval = false;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.cooldownIron = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							{
								double _setval = 900 / 20;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.timer = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							ArmorTrimsModForge.queueServerWork(900, () -> {
								{
									boolean _setval = true;
									entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
										capability.canUseIron = _setval;
										capability.syncPlayerVariables(entity);
									});
								}
							});
						});
					} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseIron) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet!"), true);
					}
				} else if (((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).boots).contains("lapis")) {
					if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseLapis) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("(\u00A7c!\u00A7f) Lapis Enchantments \u00A7a\u00A7o[active]"), true);
						{
							boolean _setval = false;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.canUseLapis = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						if (entity instanceof Player _player && !_player.level().isClientSide()) {
							for (int i = 0; i < _player.getInventory().getContainerSize(); i++) {
								ItemStack itemstackiterator = _player.getInventory().getItem(i);
								if (itemstackiterator.isEnchanted()) {
									Map<Enchantment, Integer> existingEnchantments = EnchantmentHelper.getEnchantments(itemstackiterator);
									Map<Enchantment, Integer> newEnchantments = new HashMap<>();
									existingEnchantments.forEach((enchantment, level) -> {
										newEnchantments.put(enchantment, level + 1); // Incrementing the enchantment level by 1
									});
									EnchantmentHelper.setEnchantments(newEnchantments, itemstackiterator); // Setting the updated enchantments
									itemstackiterator.getOrCreateTag().putBoolean("tagName", true); // Adding the tag
								}
							}
						}
						ArmorTrimsModForge.queueServerWork(600, () -> {
							if (entity instanceof Player _player && !_player.level().isClientSide()) {
								for (int i = 0; i < _player.getInventory().getContainerSize(); i++) {
									ItemStack itemstackiterator = _player.getInventory().getItem(i);
									if (itemstackiterator.isEnchanted()) {
										Map<Enchantment, Integer> existingEnchantments = EnchantmentHelper.getEnchantments(itemstackiterator);
										Map<Enchantment, Integer> newEnchantments = new HashMap<>();
										existingEnchantments.forEach((enchantment, level) -> {
											newEnchantments.put(enchantment, Math.max(level - 1, 0)); // Decrementing the enchantment level by 1, ensuring it stays non-negative
										});
										EnchantmentHelper.setEnchantments(newEnchantments, itemstackiterator); // Setting the updated enchantments
									}
								}
							}
							{
								double _setval = 940 / 20;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.timer = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							ArmorTrimsModForge.queueServerWork(940, () -> {
								{
									boolean _setval = true;
									entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
										capability.canUseLapis = _setval;
										capability.syncPlayerVariables(entity);
									});
								}
							});
						});
					} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseLapis) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet!"), true);
					}
				} else if (((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).boots).contains("gold")) {
					if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseGold) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("(\u00A7c!\u00A7f) Brutes Summoned \u00A7a\u00A7o[active]"), true);
						for (int index6 = 0; index6 < Mth.nextInt(RandomSource.create(), 5, 6); index6++) {
							if (world instanceof ServerLevel _level) {
								Entity entityToSpawn = ArmorTrimsModEntities.BRUTE.get().spawn(_level, BlockPos.containing(entity.getX(), entity.getY(), entity.getZ()), MobSpawnType.MOB_SUMMONED);
								if (entityToSpawn != null) {
								}
							}
						}
						{
							boolean _setval = false;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.canUseGold = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						{
							double _setval = 900 / 20;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.timer = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						ArmorTrimsModForge.queueServerWork(900, () -> {
							{
								boolean _setval = true;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.canUseGold = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
						});
					} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseGold) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet!"), true);
					}
				} else if (((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).boots).contains("amethyst")) {
					if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseAmethyst) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("(\u00A7c!\u00A7f) Ally Witch summoned \u00A7a\u00A7o[active]"), true);
						if (world instanceof ServerLevel _level) {
							Entity entityToSpawn = ArmorTrimsModEntities.ALLY_WITCH.get().spawn(_level, BlockPos.containing(x, y, z), MobSpawnType.MOB_SUMMONED);
							if (entityToSpawn != null) {
								entityToSpawn.setYRot(world.getRandom().nextFloat() * 360F);
							}
						}
						if (world instanceof ServerLevel _level) {
							Entity entityToSpawn = ArmorTrimsModEntities.ALLY_WITCH.get().spawn(_level, BlockPos.containing(x, y, z), MobSpawnType.MOB_SUMMONED);
							if (entityToSpawn != null) {
								entityToSpawn.setYRot(world.getRandom().nextFloat() * 360F);
							}
						}
						{
							double _setval = 1200 / 20;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.timer = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						{
							boolean _setval = false;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.canUseAmethyst = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						ArmorTrimsModForge.queueServerWork(1200, () -> {
							{
								boolean _setval = true;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.canUseAmethyst = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
						});
					} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseAmethyst) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet!"), true);
					}
				} else if (((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).boots).contains("diamond")) {
					if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseDiamond) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("(\u00A7c!\u00A7f) Piercing Arrow Shot \u00A7a\u00A7o[active]"), true);
						{
							Entity _shootFrom = entity;
							Level projectileLevel = _shootFrom.level();
							if (!projectileLevel.isClientSide()) {
								Projectile _entityToSpawn = new Object() {
									public Projectile getArrow(Level level, Entity shooter, float damage, int knockback) {
										AbstractArrow entityToSpawn = new PiercingArrowEntity(ArmorTrimsModEntities.PIERCING_ARROW.get(), level);
										entityToSpawn.setOwner(shooter);
										entityToSpawn.setBaseDamage(damage);
										entityToSpawn.setKnockback(knockback);
										entityToSpawn.setSilent(true);
										entityToSpawn.setCritArrow(true);
										return entityToSpawn;
									}
								}.getArrow(projectileLevel, entity, 8, 0);
								_entityToSpawn.setPos(_shootFrom.getX(), _shootFrom.getEyeY() - 0.1, _shootFrom.getZ());
								_entityToSpawn.shoot(_shootFrom.getLookAngle().x, _shootFrom.getLookAngle().y, _shootFrom.getLookAngle().z, 3, 0);
								projectileLevel.addFreshEntity(_entityToSpawn);
							}
						}
						if (world instanceof Level _level) {
							if (!_level.isClientSide()) {
								_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.arrow.shoot")), SoundSource.NEUTRAL, 1, 1);
							} else {
								_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.arrow.shoot")), SoundSource.NEUTRAL, 1, 1, false);
							}
						}
						entity.getPersistentData().putDouble("counter1", (entity.getPersistentData().getDouble("counter1") + 1));
						if (entity.getPersistentData().getDouble("counter1") % 3 == 0) {
							{
								boolean _setval = false;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.canUseDiamond = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							{
								double _setval = 700 / 20;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.timer = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							ArmorTrimsModForge.queueServerWork(700, () -> {
								{
									boolean _setval = true;
									entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
										capability.canUseDiamond = _setval;
										capability.syncPlayerVariables(entity);
									});
								}
							});
						}
					} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseDiamond) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet!"), true);
					}
				} else if (((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).boots).contains("redstone")) {
					if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseRedstone) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("(\u00A7c!\u00A7f) Homing Arrow Deployed \u00A7a\u00A7o[active]"), true);
						{
							Entity _shootFrom = entity;
							Level projectileLevel = _shootFrom.level();
							if (!projectileLevel.isClientSide()) {
								Projectile _entityToSpawn = new Object() {
									public Projectile getArrow(Level level, Entity shooter, float damage, int knockback) {
										AbstractArrow entityToSpawn = new TNTArrowEntity(ArmorTrimsModEntities.TNT_ARROW.get(), level);
										entityToSpawn.setOwner(shooter);
										entityToSpawn.setBaseDamage(damage);
										entityToSpawn.setKnockback(knockback);
										entityToSpawn.setSilent(true);
										entityToSpawn.setCritArrow(true);
										return entityToSpawn;
									}
								}.getArrow(projectileLevel, entity, 2, 0);
								_entityToSpawn.setPos(_shootFrom.getX(), _shootFrom.getEyeY() - 0.1, _shootFrom.getZ());
								_entityToSpawn.shoot(_shootFrom.getLookAngle().x, _shootFrom.getLookAngle().y, _shootFrom.getLookAngle().z, (float) 1.5, 0);
								projectileLevel.addFreshEntity(_entityToSpawn);
							}
						}
						if (world instanceof Level _level) {
							if (!_level.isClientSide()) {
								_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.arrow.shoot")), SoundSource.NEUTRAL, 1, 1);
							} else {
								_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.arrow.shoot")), SoundSource.NEUTRAL, 1, 1, false);
							}
						}
						if (world instanceof Level _level) {
							if (!_level.isClientSide()) {
								_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.tnt.primed")), SoundSource.NEUTRAL, 1, 1);
							} else {
								_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.tnt.primed")), SoundSource.NEUTRAL, 1, 1, false);
							}
						}
						entity.getPersistentData().putDouble("counter2", (entity.getPersistentData().getDouble("counter2") + 1));
						if (entity.getPersistentData().getDouble("counter2") % 4 == 0) {
							{
								boolean _setval = false;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.canUseRedstone = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							{
								double _setval = 900 / 20;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.timer = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							ArmorTrimsModForge.queueServerWork(900, () -> {
								{
									boolean _setval = true;
									entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
										capability.canUseRedstone = _setval;
										capability.syncPlayerVariables(entity);
									});
								}
							});
						}
					} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseRedstone) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet!"), true);
					}
				} else if (((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).boots).contains("copper")) {
					if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseCopper) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("(\u00A7c!\u00A7f) Lightning Strike \u00A7a\u00A7o[active]"), true);
						for (int index7 = 0; index7 < 3; index7++) {
							if (world instanceof ServerLevel _level) {
								LightningBolt entityToSpawn = EntityType.LIGHTNING_BOLT.create(_level);
								entityToSpawn.moveTo(Vec3.atBottomCenterOf(new BlockPos(
										entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(128)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos()
												.getX(),
										entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(128)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos()
												.getY(),
										entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(128)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos()
												.getZ())));
								_level.addFreshEntity(entityToSpawn);
							}
						}
						{
							final Vec3 _center = new Vec3(
									(entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(128)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getX()),
									(entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(128)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getY()),
									(entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(128)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos()
											.getZ()));
							List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
							for (Entity entityiterator : _entfound) {
								if (entityiterator instanceof LivingEntity && !(entity == entityiterator)) {
									if (entityiterator instanceof LivingEntity _entity && !_entity.level().isClientSide())
										_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 0, true, true));
									if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION, (entityiterator instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY)) != 0
											|| EnchantmentHelper.getItemEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION,
													(entityiterator instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.CHEST) : ItemStack.EMPTY)) != 0
											|| EnchantmentHelper.getItemEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION,
													(entityiterator instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY)) != 0
											|| EnchantmentHelper.getItemEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION,
													(entityiterator instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY)) != 0) {
										entityiterator.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC)), 8);
									}
								}
							}
						}
						entity.getPersistentData().putDouble("counter3", (entity.getPersistentData().getDouble("counter3") + 1));
						if (entity.getPersistentData().getDouble("counter3") % 10 == 0) {
							{
								boolean _setval = false;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.canUseCopper = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							{
								double _setval = 900 / 20;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.timer = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							ArmorTrimsModForge.queueServerWork(900, () -> {
								{
									boolean _setval = true;
									entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
										capability.canUseCopper = _setval;
										capability.syncPlayerVariables(entity);
									});
								}
							});
						}
					} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseCopper) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet!"), true);
					}
				} else if (((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).boots).contains("quartz")) {
					if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseQuartz) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("(\u00A7c!\u00A7f) Smoke Cloud \u00A7a\u00A7o[active]"), true);
						{
							Entity _ent = entity;
							if (!_ent.level().isClientSide() && _ent.getServer() != null) {
								_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
										_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "/effect give @p trueinvis:true_invis 12 10 true");
							}
						}
						{
							final Vec3 _center = new Vec3(x, y, z);
							List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(32 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
							for (Entity entityiterator : _entfound) {
								if (!(entityiterator == entity) && entityiterator instanceof LivingEntity) {
									if (entityiterator instanceof LivingEntity _entity && !_entity.level().isClientSide())
										_entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 300, 9, false, false));
								}
							}
						}
						int horizontalRadiusHemiTop = 10 - 1;
						int verticalRadiusHemiTop = 7;
						int yIterationsHemiTop = verticalRadiusHemiTop;
						for (int i = 0; i < yIterationsHemiTop; i++) {
							if (i == verticalRadiusHemiTop) {
								continue;
							}
							for (int xi = -horizontalRadiusHemiTop; xi <= horizontalRadiusHemiTop; xi++) {
								for (int zi = -horizontalRadiusHemiTop; zi <= horizontalRadiusHemiTop; zi++) {
									double distanceSq = (xi * xi) / (double) (horizontalRadiusHemiTop * horizontalRadiusHemiTop) + (i * i) / (double) (verticalRadiusHemiTop * verticalRadiusHemiTop)
											+ (zi * zi) / (double) (horizontalRadiusHemiTop * horizontalRadiusHemiTop);
									if (distanceSq <= 1.0) {
										if (world instanceof ServerLevel _level)
											_level.sendParticles(ArmorTrimsModParticleTypes.POOFPOOF.get(), x + xi, (y + i + 0), z + zi, 100, 1, 1, 1, 0.05);
									}
								}
							}
						}
						{
							boolean _setval = false;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.canUseQuartz = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						{
							double _setval = 1200 / 20;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.timer = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						ArmorTrimsModForge.queueServerWork(1200, () -> {
							{
								boolean _setval = true;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.canUseQuartz = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
						});
					} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseQuartz) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet!"), true);
					}
				} else if (((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).boots).contains("emerald")) {
					if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseEmerald) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("(\u00A7c!\u00A7f) Armor Totem \u00A7a\u00A7o[active 15 seconds]"), true);
						{
							boolean _setval = true;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.died = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						{
							boolean _setval = false;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.didntDie = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						{
							boolean _setval = false;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.canUseEmerald = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						ArmorTrimsModForge.queueServerWork(300, () -> {
							if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).died) {
								{
									boolean _setval = false;
									entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
										capability.died = _setval;
										capability.syncPlayerVariables(entity);
									});
								}
								{
									boolean _setval = true;
									entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
										capability.didntDie = _setval;
										capability.syncPlayerVariables(entity);
									});
								}
								ArmorTrimsModForge.queueServerWork(6000, () -> {
									{
										boolean _setval = true;
										entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
											capability.canUseEmerald = _setval;
											capability.syncPlayerVariables(entity);
										});
									}
								});
							} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).died) {
								ArmorTrimsModForge.queueServerWork(72000, () -> {
									{
										boolean _setval = true;
										entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
											capability.canUseEmerald = _setval;
											capability.syncPlayerVariables(entity);
										});
									}
								});
							}
						});
					} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseEmerald) {
						if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).didntDie) {
							if (entity instanceof Player _player && !_player.level().isClientSide())
								_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet! \u00A7f\u00A7o[5 min cooldown]"), true);
						} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).didntDie) {
							if (entity instanceof Player _player && !_player.level().isClientSide())
								_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet! \u00A7f\u00A7o[26min cooldown]"), true);
						}
					}
				} else if (((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).boots).contains("netherite")) {
					if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseNetherite) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("(\u00A7c!\u00A7f) Wither Touch \u00A7a\u00A7o[active]"), true);
						{
							boolean _setval = true;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.cooldownNetherite = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						{
							boolean _setval = false;
							entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.canUseNetherite = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						ArmorTrimsModForge.queueServerWork(100, () -> {
							{
								boolean _setval = false;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.cooldownNetherite = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							{
								double _setval = 900 / 20;
								entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
									capability.timer = _setval;
									capability.syncPlayerVariables(entity);
								});
							}
							ArmorTrimsModForge.queueServerWork(900, () -> {
								{
									boolean _setval = true;
									entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
										capability.canUseNetherite = _setval;
										capability.syncPlayerVariables(entity);
									});
								}
							});
						});
					} else if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).canUseNetherite) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("\u00A7cYou can't use this yet!"), true);
					}
				}
			}
		}
	}
}
