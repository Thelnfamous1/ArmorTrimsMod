package com.marwinekk.armortrims.ducks;

import net.minecraft.world.entity.OwnableEntity;

import java.util.UUID;

public interface WitchDuck extends OwnableEntity {
    void setOwnerUUID(UUID ownerUUID);

}
