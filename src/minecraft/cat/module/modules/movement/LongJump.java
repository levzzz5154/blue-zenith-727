package cat.module.modules.movement;

import cat.events.Subscriber;
import cat.events.impl.MoveEvent;
import cat.events.impl.UpdatePlayerEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.types.BooleanValue;
import cat.module.value.types.ModeValue;
import cat.util.BypassUtil;
import cat.util.MathUtil;
import cat.util.MovementUtil;
import cat.util.PlayerUtil;

@SuppressWarnings("SpellCheckingInspection")
public class LongJump extends Module {
    public ModeValue mode = new ModeValue("Mode", "OldVerus", true, null, "OldVerus");
    public ModeValue bypassMode = new ModeValue("DamageMode", "Old", false, i -> mode.get().equals("OldVerus"), "None", "Old", "New");
    public LongJump(){
        super("LongJump", "", ModuleCategory.MOVEMENT);
    }
    public final int[] b = new int[]{0};
    public final int jumps = 4;
    private int c = 0;
    private long f = System.currentTimeMillis();
    private boolean maccacokkk = false; // thx levzzz
    @Override
    public void onEnable(){
        maccacokkk = false;
        if(this.mc.thePlayer != null && !bypassMode.get().equals("None")){
            if(bypassMode.get().equals("Old")){
                PlayerUtil.damageNormal((float) Math.PI);
                b[0] = -1;
            }else{
                b[0] = 0;
            }
            c = 0;
        }
    }
    @Subscriber
    public void onMove(MoveEvent e){
        if(b[0] <= jumps && bypassMode.get().equals("New")){
            e.x = 0;
            e.z = 0;
        }
    }
    public boolean canFly(){
        return bypassMode.get().equals("None") || bypassMode.get().equals("Old") || (bypassMode.get().equals("New") && b[0] > jumps);
    }
    @Subscriber
    public void onPlayerUpdate(UpdatePlayerEvent e){
        if(bypassMode.get().equals("New") && !PlayerUtil.damageVerus(e, jumps, b)){
            return;
        }
        if(mc.thePlayer.hurtTime == 9){
            c = 0;
            maccacokkk = true;
        }
        if(!maccacokkk || !canFly()) return;
        if(c <= 6){
            mc.thePlayer.jump();
            c++;
            if(c == 6){
                f = System.currentTimeMillis();
            }
        }
        long boostTime = 700;
        float lol = (boostTime - MathUtil.inRange((System.currentTimeMillis() - f), 0, boostTime)) / boostTime;
        if(lol > 0.1){
            MovementUtil.setSpeed(lol / BypassUtil.bypass_value);
        }
        if(mc.thePlayer.onGround && lol == 0 && c > 3 && maccacokkk){
            setState(false);
        }
    }
}
