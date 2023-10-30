package com.marwinekk.armortrims.util;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ArmorTrimTicks {

    public static final Map<Item, Consumer<Player>> TRIM_SET_TICKS = new HashMap<>();

    static  {
        TRIM_SET_TICKS.put(Items.IRON_INGOT,player -> player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 40, 9, false, false)));
        //give 1 xp level every 10 minutes
        TRIM_SET_TICKS.put(Items.LAPIS_LAZULI,player -> {
            if (player.level().getGameTime() % (20 * 60 * 10) == 0) {
                player.giveExperienceLevels(1);
                player.level().playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.NEUTRAL, 1, 1);
                player.displayClientMessage(Component.literal("[§c!§f] Level §agained§f!"), true);

            }
        });
        TRIM_SET_TICKS.put(Items.NETHERITE_INGOT,player -> {
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 40, 9, true, false));
            if (player.isInLava()) {
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20, 1, true, false));
            }
        });
    }
}
