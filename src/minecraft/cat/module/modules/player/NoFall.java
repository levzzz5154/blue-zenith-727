package cat.module.modules.player;

import cat.events.Subscriber;
import cat.events.impl.PacketEvent;
import cat.events.impl.UpdatePlayerEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.types.ModeValue;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.play.client.C03PacketPlayer;

public class NoFall extends Module {
    private ModeValue mode = new ModeValue("1", "Mode", "Edit", true, null, "Edit");
    public NoFall(){
        super("NoFall", "", ModuleCategory.PLAYER, "NoFall");
    }
    @Override
    public String getTag(){
        return this.mode.get();
    }
    @Subscriber
    public void onPacket(UpdatePlayerEvent e){
        if (mode.get().equals("Edit")) {
            if(mc.thePlayer.fallDistance >= 3){
                e.onGround = true;
            }
        }
    }
}
