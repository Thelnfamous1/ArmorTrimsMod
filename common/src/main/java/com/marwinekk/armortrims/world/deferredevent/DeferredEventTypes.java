package com.marwinekk.armortrims.world.deferredevent;

import com.marwinekk.armortrims.ArmorTrimsMod;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class DeferredEventTypes {

    public static final Map<ResourceLocation,DeferredEventType<?>> REGISTRY = new HashMap<>();

    public static final DeferredEventType<IronFist> IRON_FIST =
            register(IronFist::new, new ResourceLocation(ArmorTrimsMod.MOD_ID,"iron_fist"));
    public static final DeferredEventType<DespawnLater> DESPAWN_LATER =
            register(DespawnLater::new, new ResourceLocation(ArmorTrimsMod.MOD_ID,"despawn_later"));
    public static final DeferredEventType<SetAbilityCooldown> SET_ABILITY_COOLDOWN =
            register(SetAbilityCooldown::new, new ResourceLocation(ArmorTrimsMod.MOD_ID,"set_ability_cooldown"));

    public static <T extends DeferredEvent> DeferredEventType<T> register(Supplier<T> function, ResourceLocation resourceLocation) {
        DeferredEventType<T> deferredEventType = new DeferredEventType<>(function,resourceLocation);
        REGISTRY.put(resourceLocation,deferredEventType);
        return deferredEventType;
    }
}
