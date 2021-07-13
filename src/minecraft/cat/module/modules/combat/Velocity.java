package cat.module.modules.combat;

import cat.events.Subscriber;
import cat.events.impl.PacketEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public class Velocity extends Module {
    public Velocity() {
        super("Velocity", "", ModuleCategory.COMBAT);
    }
    @Subscriber
    public void onPacket(PacketEvent e){
        Packet<?> packet = e.packet;
        if(packet instanceof S12PacketEntityVelocity){
            S12PacketEntityVelocity s = (S12PacketEntityVelocity) packet;
            if(s.getEntityID() == mc.thePlayer.getEntityId()){
                s.motionX *= 0;
                s.motionZ *= 0;
                s.motionY *= 0;
            }
        }
    }
}
