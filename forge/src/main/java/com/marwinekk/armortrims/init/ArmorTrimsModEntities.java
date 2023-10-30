
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package com.marwinekk.armortrims.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;

import com.marwinekk.armortrims.entity.TNTArrowEntity;
import com.marwinekk.armortrims.entity.PotionThrowEntity;
import com.marwinekk.armortrims.entity.PiercingArrowEntity;
import com.marwinekk.armortrims.entity.Pa3Entity;
import com.marwinekk.armortrims.entity.Pa2Entity;
import com.marwinekk.armortrims.entity.MiniGuyEntity;
import com.marwinekk.armortrims.entity.CloneEntity;
import com.marwinekk.armortrims.ArmorTrimsModForge;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ArmorTrimsModEntities {
	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ArmorTrimsModForge.MOD_ID);

	public static final RegistryObject<EntityType<CloneEntity>> ALLY_WITCH = register("ally_witch",
			EntityType.Builder.<CloneEntity>of(CloneEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(CloneEntity::new)

					.sized(0.6f, 1.95f));
	public static final RegistryObject<EntityType<PiercingArrowEntity>> PIERCING_ARROW = register("projectile_piercing_arrow",
			EntityType.Builder.<PiercingArrowEntity>of(PiercingArrowEntity::new, MobCategory.MISC).setCustomClientFactory(PiercingArrowEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final RegistryObject<EntityType<Pa2Entity>> PA_2 = register("projectile_pa_2",
			EntityType.Builder.<Pa2Entity>of(Pa2Entity::new, MobCategory.MISC).setCustomClientFactory(Pa2Entity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final RegistryObject<EntityType<Pa3Entity>> PA_3 = register("projectile_pa_3",
			EntityType.Builder.<Pa3Entity>of(Pa3Entity::new, MobCategory.MISC).setCustomClientFactory(Pa3Entity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final RegistryObject<EntityType<TNTArrowEntity>> TNT_ARROW = register("projectile_tnt_arrow",
			EntityType.Builder.<TNTArrowEntity>of(TNTArrowEntity::new, MobCategory.MISC).setCustomClientFactory(TNTArrowEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final RegistryObject<EntityType<MiniGuyEntity>> MINI_GUY = register("mini_guy",
			EntityType.Builder.<MiniGuyEntity>of(MiniGuyEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(MiniGuyEntity::new)

					.sized(0.6f, 1.8f));
	public static final RegistryObject<EntityType<PotionThrowEntity>> POTION_THROW = register("projectile_potion_throw",
			EntityType.Builder.<PotionThrowEntity>of(PotionThrowEntity::new, MobCategory.MISC).setCustomClientFactory(PotionThrowEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));

	private static <T extends Entity> RegistryObject<EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
		return REGISTRY.register(registryname, () -> entityTypeBuilder.build(registryname));
	}

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			CloneEntity.init();
			MiniGuyEntity.init();
		});
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(ALLY_WITCH.get(), CloneEntity.createAttributes().build());
		event.put(MINI_GUY.get(), MiniGuyEntity.createAttributes().build());
	}
}
