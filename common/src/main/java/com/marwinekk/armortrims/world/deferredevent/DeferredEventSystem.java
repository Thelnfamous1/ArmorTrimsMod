package com.marwinekk.armortrims.world.deferredevent;

import com.marwinekk.armortrims.ArmorTrimsMod;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class DeferredEventSystem extends SavedData {

    public List<DeferredEvent> futureEvents = new ArrayList<>();

    public List<DeferredEvent> oldEvents = new ArrayList<>();

    public void addDeferredEvent(DeferredEvent deferredEvent) {
        futureEvents.add(deferredEvent);
    }

    public void tickDeferredEvents(ServerLevel level) {
        for (DeferredEvent deferredEvent : futureEvents) {
            deferredEvent.tick();
            if (deferredEvent.isReady()) {
                boolean finished = deferredEvent.attemptRun(level);
                if (finished) {
                    oldEvents.add(deferredEvent);
                }
            }
        }
        futureEvents.removeAll(oldEvents);
        oldEvents.clear();
        if (!futureEvents.isEmpty()) {
            setDirty();
        }
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag listTag = new ListTag();
        for (DeferredEvent deferredEvent : futureEvents) {
            listTag.add(deferredEvent.write());
        }
        tag.put("deferredevents",listTag);
        return tag;
    }

    public void load(CompoundTag tag) {
        ListTag listTag = tag.getList("deferredevents", Tag.TAG_COMPOUND);
        for (Tag tag1 : listTag) {
            CompoundTag compoundTag = (CompoundTag) tag1;
            String id = compoundTag.getString("id");
            DeferredEventType<?> deferredEventType = DeferredEventTypes.REGISTRY.get(new ResourceLocation(id));
            if (deferredEventType == null) {
                ArmorTrimsMod.LOG.warn("Unregistered deferred event: "+id);
                continue;
            }
            DeferredEvent deferredEvent = deferredEventType.createFromTag(compoundTag);
            futureEvents.add(deferredEvent);
        }
    }

    public static DeferredEventSystem loadStatic(CompoundTag compoundTag) {
        DeferredEventSystem id = new DeferredEventSystem();
        id.load(compoundTag);
        return id;
    }


    public void removeDeferredEvent(Predicate<DeferredEvent> predicate) {
        this.futureEvents.removeIf(predicate);
    }
}
