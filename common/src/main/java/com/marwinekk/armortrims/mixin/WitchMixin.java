package com.marwinekk.armortrims.mixin;

import com.marwinekk.armortrims.ducks.WitchDuck;
import com.marwinekk.armortrims.entity.ai.HealPlayerGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(Witch.class)
abstract class WitchMixin extends Mob implements WitchDuck {

    @Nullable
    private UUID ownerUUID;

    protected WitchMixin(EntityType<? extends Mob> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Inject(method = "registerGoals",at = @At("RETURN"))
    private void addCustomGoals(CallbackInfo ci) {
        this.targetSelector.addGoal(2,new HealPlayerGoal<>(this, Player.class,10,true,false,null));
    }

    public void addAdditionalSaveData(CompoundTag $$0) {
        super.addAdditionalSaveData($$0);
        if (ownerUUID != null) {
            $$0.putUUID("owner",ownerUUID);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag $$0) {
        super.readAdditionalSaveData($$0);
        if ($$0.hasUUID("owner")) {
            ownerUUID = $$0.getUUID("owner");
        }
    }

    @ModifyVariable(method = "performRangedAttack",at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/projectile/ThrownPotion;<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;)V"),ordinal = 0)
    private Potion modifyPotion(Potion original,LivingEntity target,float damage) {
        if (target.getUUID().equals(ownerUUID)) {
            if (target.getHealth() <= 4.0F) {
                original = Potions.HEALING;
            } else {
                original = Potions.REGENERATION;
            }
            setTarget(null);
        }
        return original;
    }

    @Nullable
    @Override
    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    @Override
    public void setOwnerUUID(UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
    }
}
