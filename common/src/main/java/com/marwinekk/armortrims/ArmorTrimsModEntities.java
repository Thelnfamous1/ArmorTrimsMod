
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package com.marwinekk.armortrims;

import com.marwinekk.armortrims.entity.TNTArrowEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;


public class ArmorTrimsModEntities {
	public static final EntityType<TNTArrowEntity> TNT_ARROW = EntityType.Builder.<TNTArrowEntity>of(TNTArrowEntity::new, MobCategory.MISC)
			.clientTrackingRange(64).sized(0.5f, 0.5f).build("tnt_arrow");

}
