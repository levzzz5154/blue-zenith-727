package cat.module.modules.fun;

import cat.events.Subscriber;
import cat.events.impl.UpdatePlayerEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.util.PacketUtil;
import net.minecraft.network.play.client.C03PacketPlayer;

@SuppressWarnings("unused")
public class YesFall extends Module {
    // lmao thx levzzz for the idea
    public YesFall(){
        super("YesFall", "", ModuleCategory.FUN);
    }

    @Subscriber
    public void onMove(UpdatePlayerEvent e){
        if(Math.round(mc.thePlayer.fallDistance) % 3 == 0){
            // rip i couldn't make it :((( :cry:
            //i am gaming :sunglasses:
            PacketUtil.send(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.fallDistance, mc.thePlayer.posZ, false));
        }
    }
}
