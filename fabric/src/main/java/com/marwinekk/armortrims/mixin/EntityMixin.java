package com.marwinekk.armortrims.mixin;

import com.marwinekk.armortrims.util.ArmorTrimAbilities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Entity.class)
public class EntityMixin {

    @ModifyConstant(method = "thunderHit", constant = @Constant(floatValue = 5.0F, ordinal = 0), require = 0)
    private float modifyLightningDamage(float constant, ServerLevel level, LightningBolt bolt){
        if(bolt.getTags().contains(ArmorTrimAbilities.ARMOR_TRIMS_TAG)){
            return 10.0F;
        }
        return constant;
    }

}
