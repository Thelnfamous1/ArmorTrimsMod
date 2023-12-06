package com.marwinekk.armortrims.ducks;

public interface PhysicsCheck {

    default boolean canBypassGravity(){
        return false;
    }

}