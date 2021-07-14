package cat.module.modules.combat;

import cat.events.Subscriber;
import cat.events.impl.PacketEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.types.BoolValue;
import cat.module.value.types.FloatValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

public class Velocity extends Module {
    public Velocity() {
        super("Velocity", "", ModuleCategory.COMBAT);
    }
    private final FloatValue horizontal = new FloatValue("velocity_horizontal", "Horizontal", 100F, 0F, 500F, 1F, true, null);
    private final FloatValue vertical = new FloatValue("velocity_vertical", "Vertical", 100F, 0F, 100F, 1F, true, null);
    private final BoolValue explosions = new BoolValue("velocity_explosions", "Explosions", true, true, null);
    @Subscriber
    public void onPacket(PacketEvent e){
        this.setTag(horizontal.get() + "% " + vertical.get() + "%");
        Packet<?> packet = e.packet;
        if(packet instanceof S12PacketEntityVelocity){
            S12PacketEntityVelocity s = (S12PacketEntityVelocity) packet;
            if(s.getEntityID() == mc.thePlayer.getEntityId()){
                if(horizontal.get() == 0F && vertical.get() == 0F) {
                    e.cancel();
                }
                s.motionX *= (horizontal.get() / 100);
                s.motionZ *= (horizontal.get() / 100);
                s.motionY *= (vertical.get() / 100);
            }
        }
        if(packet instanceof S27PacketExplosion && explosions.get()) {
                e.cancel();
        }
    }
}
