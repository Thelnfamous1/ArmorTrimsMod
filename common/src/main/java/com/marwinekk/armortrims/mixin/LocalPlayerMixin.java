package com.marwinekk.armortrims.mixin;

import com.marwinekk.armortrims.ArmorTrimsMod;
import com.marwinekk.armortrims.ducks.PlayerDuck;
import com.marwinekk.armortrims.platform.Services;
import com.marwinekk.armortrims.util.CopperTrimAbilities;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer {

    @Shadow public Input input;
    @Unique
    private int armorTrimsMod$jumpCount = 0;
    @Unique
    private boolean armorTrimsMod$jumpedLastTick = false;
    @Unique
    private int armorTrimsMod$jumpCooldown = 0;

    public LocalPlayerMixin(ClientLevel $$0, GameProfile $$1) {
        super($$0, $$1);
    }

    @Inject(method = "drop", at = @At("HEAD"), cancellable = true)
    private void handleDropLockedSlot(boolean fullStack, CallbackInfoReturnable<Boolean> cir){
        if(!this.isCreative() && ArmorTrimsMod.isLocked(this.getInventory().getSelected())){
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void tickMovement(CallbackInfo info) {
        if(this.armorTrimsMod$jumpCooldown > 0){
            this.armorTrimsMod$jumpCooldown--;
        }
        if (this.onGround() || this.onClimbable()) {
            this.armorTrimsMod$jumpCount = CopperTrimAbilities.canDoubleJump((PlayerDuck) this) ? 1 : 0;
        } else if (!this.armorTrimsMod$jumpedLastTick && this.armorTrimsMod$jumpCount > 0 && this.getDeltaMovement().y < 0 && this.armorTrimsMod$jumpCooldown <= 0) {
            if (this.input.jumping && !this.getAbilities().flying) {
                if (this.armorTrimsMod$canJump(this)) {
                    --this.armorTrimsMod$jumpCount;
                    this.jumpFromGround();
                    // increases jump height to 2 blocks instead of just 1
                    this.push(0, CopperTrimAbilities.getJumpBoostPower(1), 0);
                    CopperTrimAbilities.setDoubleJumping(((PlayerDuck) this), true);
                    CopperTrimAbilities.createDoubleJumpEffect(this);

                    Services.PLATFORM.sendDoubleJump(this);
                    this.armorTrimsMod$jumpCooldown = CopperTrimAbilities.JUMP_COOLDOWN_TICKS;
                }
            }
        }
        this.armorTrimsMod$jumpedLastTick = this.input.jumping;
    }

    @Unique
    private boolean armorTrimsMod$wearingUsableElytra(Player player) {
        ItemStack chestItemStack = player.getItemBySlot(EquipmentSlot.CHEST);
        return chestItemStack.getItem() == Items.ELYTRA && ElytraItem.isFlyEnabled(chestItemStack);
    }

    @Unique
    private boolean armorTrimsMod$canJump(Player player) {
        return !armorTrimsMod$wearingUsableElytra(player) && !player.isFallFlying() && !player.isPassenger()
                && !player.isInWater() && !player.hasEffect(MobEffects.LEVITATION);
    }
}
