package cat.module.modules.movement;

import cat.events.Subscriber;
import cat.events.impl.BlockBBEvent;
import cat.events.impl.MoveEvent;
import cat.events.impl.UpdatePlayerEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.types.FloatValue;
import cat.module.value.types.ModeValue;
import cat.util.MillisTimer;
import cat.util.MovementUtil;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.input.Keyboard;

@SuppressWarnings("unused")
public class Flight extends Module {
    private final ModeValue mode = new ModeValue("flight_mode", "Mode", "Vanilla", true, null, "Vanilla", "OldVerus");
    private final FloatValue speed = new FloatValue("1", "Speed", 2f, 0f, 5f, 0.1f, true, __ -> mode.get().equals("Vanilla")); //example of a visibility modifier
    public Flight() {
        super("Flight", "", ModuleCategory.MOVEMENT, Keyboard.KEY_F);
    }
    public final float[] movementSpeed = new float[]{0, 0, 0};
    private long st = System.currentTimeMillis();
    private final MillisTimer i_hate_ticks = new MillisTimer();
    @Subscriber
    public void onUpdate(UpdatePlayerEvent e) {
        switch (mode.get()) {
            case "Vanilla":
                if (MovementUtil.areMovementKeysPressed()) {
                    MovementUtil.setSpeed(speed.get());
                } else {
                    mc.thePlayer.motionX = 0;
                    mc.thePlayer.motionZ = 0;
                }
                if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    mc.thePlayer.motionY = speed.get();
                } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                    mc.thePlayer.motionY = -speed.get();
                } else {
                    mc.thePlayer.motionY = 0;
                }
                break;
            case "OldVerus":
                if(MovementUtil.areMovementKeysPressed() && movementSpeed[2] > f){
                    float mv = movementSpeed[0] / 0.1536f;
                    if(mv <= 2.213541 && movementSpeed[1] != 1){
                        movementSpeed[0] += 0.01f;
                    }else if(mv >= 0.1536f){
                        movementSpeed[1] = 1;
                        movementSpeed[0] -= 0.03;
                    }

                    MovementUtil.setSpeed(mv);
                }else if(movementSpeed[2] <= f){
                    if(movementSpeed[2] <= f - 1){
                        e.onGround = false;
                        if(i_hate_ticks.hasTicksPassed(12)){
                            mc.thePlayer.jump();
                            i_hate_ticks.reset();
                            movementSpeed[2]++;
                        }
                    }else if(i_hate_ticks.hasTicksPassed(12)){
                        e.onGround = true;
                        movementSpeed[2]++;
                        i_hate_ticks.reset();
                    }
                    if(mc.thePlayer.hurtTime == 9){
                        movementSpeed[2] = f + 1;
                    }
                }else{
                    mc.thePlayer.motionX = 0;
                    mc.thePlayer.motionZ = 0;
                }
                break;
        }
    }
    @Subscriber
    public void onMove(MoveEvent e){
        if(movementSpeed[2] <= f && this.mode.get().equals("OldVerus")){
            e.x = 0;
            e.z = 0;
        }
    }
    @Subscriber
    public void onBlockBB(BlockBBEvent e){
        assert mc.thePlayer != null;
        if(!e.block.getMaterial().isSolid() && e.pos.getY() < mc.thePlayer.posY){
            if(mode.get().equals("OldVerus") && movementSpeed[2] > f){
                e.blockBB = AxisAlignedBB.fromBounds(-5, -1, -5, 5, 0.42, 5).offset(e.pos.getX(), e.pos.getY(), e.pos.getZ());
            }
        }
    }
    public int f = 3;
    @Override
    public void onEnable(){
        if (mode.get().equals("OldVerus")) {
            movementSpeed[0] = 0.24f;
            movementSpeed[1] = 0;
            movementSpeed[2] = 0;
        }
    }
    @Override
    public void onDisable(){
        if (mode.get().equals("OldVerus")) {
            mc.thePlayer.motionX = 0;
            mc.thePlayer.motionZ = 0;
        }
    }
    @Override
    public String getTag(){
        return this.mode.get();
    }
}
