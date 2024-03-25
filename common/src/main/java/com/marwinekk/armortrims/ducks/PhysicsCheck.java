package com.marwinekk.armortrims.ducks;

public interface PhysicsCheck {

    default boolean canBypassGravity(){
        return false;
    }

    default boolean canBypassInGround(){
        return false;
    }

}