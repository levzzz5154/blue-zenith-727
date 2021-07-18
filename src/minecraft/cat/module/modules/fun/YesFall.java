package cat.module.modules.fun;

import cat.events.Subscriber;
import cat.events.impl.MoveEvent;
import cat.events.impl.PacketEvent;
import cat.events.impl.UpdatePlayerEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import net.minecraft.network.play.client.C03PacketPlayer;

public class YesFall extends Module {
    // lmao thx levzzz for the idea
    public YesFall(){
        super("YesFall", "", ModuleCategory.FUN);
    }

    @Subscriber
    public void onMove(UpdatePlayerEvent e){
        if(mc.thePlayer.fallDistance >= 3){
            // rip i couldn't make it :((( :cry:
            e.y += 10;
        }
    }
}
