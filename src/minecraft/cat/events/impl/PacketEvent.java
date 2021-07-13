package cat.events.impl;

import cat.events.Event;
import net.minecraft.network.Packet;

public class PacketEvent extends Event {
    public Packet<?> packet;
    public PacketEvent(Packet<?> packet){
        this.packet = packet;
    }
}
