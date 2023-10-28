
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package com.marwinekk.armortrims.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleType;

import com.marwinekk.armortrims.ArmorTrimsModForge;

public class ArmorTrimsModParticleTypes {
	public static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, ArmorTrimsModForge.MOD_ID);
	public static final RegistryObject<SimpleParticleType> POOFPOOF = REGISTRY.register("poofpoof", () -> new SimpleParticleType(true));
}
