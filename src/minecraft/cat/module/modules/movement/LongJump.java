package cat.module.modules.movement;

import cat.events.Subscriber;
import cat.events.impl.BlockBBEvent;
import cat.events.impl.MoveEvent;
import cat.events.impl.UpdatePlayerEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.util.MovementUtil;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;

public class LongJump extends Module {
    public LongJump(){
        super("LongJump", "", ModuleCategory.MOVEMENT);
    }
    int c = 0;
    @Override
    public void onEnable(){
        c = 0;
        if(this.mc.thePlayer != null){
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3.77, mc.thePlayer.posZ, false));
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
        }
    }
    float lol = 0;
    @Subscriber
    public void onPlayerUpdate(UpdatePlayerEvent e){
        if(c <= 3){
            mc.thePlayer.jump();
            c++;
            if(c == 3){
                f = System.currentTimeMillis();
            }
        }
        long boostTime = 500;
        lol = (boostTime - v((System.currentTimeMillis() - f), 0, boostTime)) / boostTime;
        if(lol > 0.1){
            MovementUtil.setSpeed(lol / 0.1536f);
        }
        if(mc.thePlayer.onGround && lol == 0 && c > 3){
            setState(false);
        }
    }
    public float v(float v, float m, float md){
        if(v < m) return m;
        return Math.min(md, v);
    }
    long f = System.currentTimeMillis();
}
