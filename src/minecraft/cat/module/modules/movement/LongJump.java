package cat.module.modules.movement;

import cat.events.Subscriber;
import cat.events.impl.UpdatePlayerEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.types.BooleanValue;
import cat.ui.notifications.NotiManager;
import cat.util.MovementUtil;
import net.minecraft.network.play.client.C03PacketPlayer;

@SuppressWarnings("SpellCheckingInspection")
public class LongJump extends Module {
    BooleanValue autoDamage = new BooleanValue("AutoDamage", true, true);
    public LongJump(){
        super("LongJump", "", ModuleCategory.MOVEMENT);
    }
    int c = 0;
    @Override
    public void onEnable(){
        maccacokkk = false;
        if(this.mc.thePlayer != null && autoDamage.get()){
            if(mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0, 3.77, 0).expand(0, 0, 0)).isEmpty()){
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3.77, mc.thePlayer.posZ, false));
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
            }else{
                NotiManager.addNoti("Couldn't damage you: not enough space", "", NotiManager.NotiType.ERROR, 3000);
            }
            c = 0;
        }
    }
    boolean maccacokkk = Boolean.FALSE;
    float lol = 0;
    @Subscriber
    public void onPlayerUpdate(UpdatePlayerEvent e){
        if(mc.thePlayer.hurtTime == 9){
            c = 0;
            maccacokkk = true;
        }
        if(!maccacokkk) return;
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
        if(mc.thePlayer.onGround && lol == 0 && c > 3 && maccacokkk){
            setState(false);
        }
    }
    public float v(float v, float m, float md){
        if(v < m) return m;
        return Math.min(md, v);
    }
    long f = System.currentTimeMillis();
}
