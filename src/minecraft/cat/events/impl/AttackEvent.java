package cat.events.impl;

import cat.events.Event;
import cat.events.EventType;
import net.minecraft.entity.Entity;

@SuppressWarnings("unused")
public class AttackEvent extends Event {

    public final Entity target;
    public EventType type;

    public AttackEvent(Entity target, EventType type) {
        this.target = target;
        this.type = type;
    }
}
