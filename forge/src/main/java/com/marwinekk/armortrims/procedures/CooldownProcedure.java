package com.marwinekk.armortrims.procedures;

import net.minecraft.world.entity.Entity;

import com.marwinekk.armortrims.network.ArmorTrimsModVariables;

public class CooldownProcedure {
	public static String execute(Entity entity) {
		if (entity == null)
			return "";
		String string = "";
		if (!(entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).dragonegg) {
			string = "" + ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).timer == 0
					? " "
					: "\u00A7c\u00A7lAbility Unavailable " + "\u00A7r\u00A7fCooldown: \u00A7r\u00A73"
							+ new java.text.DecimalFormat("##").format((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).timer) + "\u00A7rs");
		}
		return string;
	}
}
