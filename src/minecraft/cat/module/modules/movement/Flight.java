package cat.module.modules.movement;

import cat.events.Subscriber;
import cat.events.impl.BlockBBEvent;
import cat.events.impl.PacketEvent;
import cat.events.impl.UpdateEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.types.FloatValue;
import cat.module.value.types.ModeValue;
import cat.util.MovementUtil;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;

@SuppressWarnings("unused")
public class Flight extends Module {
    private final ModeValue mode = new ModeValue("flight_mode", "Mode", "Vanilla", true, null, "Vanilla", "OldVerus");
    private final FloatValue speed = new FloatValue("1", "Speed", 2f, 0f, 5f, 0.1f, true, __ -> mode.get().equals("Vanilla")); //example of a visibility modifier
    public Flight() {
        super("Flight", "", ModuleCategory.MOVEMENT, Keyboard.KEY_F);
    }
    public int dmgProgress = 5;
    private final float[] movementSpeed = new float[]{0, 0};
    private long st = System.currentTimeMillis();
    @Subscriber
    public void onUpdate(UpdateEvent e) {
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
                if(dmgProgress > f){
                    if(MovementUtil.areMovementKeysPressed()){
                        float mv = movementSpeed[0] / 0.1536f;
                        if(mv <= 2.213541 && movementSpeed[1] != 1){
                            movementSpeed[0] += 0.01f;
                        }else if(mv >= 0.1536f){
                            movementSpeed[1] = 1;
                            movementSpeed[0] -= 0.03;
                        }

                        MovementUtil.setSpeed(mv);
                    }else{
                        mc.thePlayer.motionX = 0;
                        mc.thePlayer.motionZ = 0;
                    }
                }
                break;
        }
    }
    @Subscriber
    public void onBlockBB(BlockBBEvent e){
        if(!e.block.getMaterial().isSolid() && e.pos.getY() < mc.thePlayer.posY){
            if(mode.get().equals("OldVerus") && dmgProgress > f){
                e.blockBB = AxisAlignedBB.fromBounds(-5, -1, -5, 5, 0.42, 5).offset(e.pos.getX(), e.pos.getY(), e.pos.getZ());
            }
        }
    }
    public int f = 2;
    @Subscriber
    public void onPacket(PacketEvent e){
        if(e.packet instanceof C03PacketPlayer && e.direction == EnumPacketDirection.CLIENTBOUND && mode.get().equals("OldVerus")){
            C03PacketPlayer p3 = (C03PacketPlayer) e.packet;
            if(dmgProgress <= f){
                switch (dmgProgress){
                    case 0:
                    case 2:
                        p3.onGround = false;
                        p3.y = mc.thePlayer.posY;
                        break;
                    case 1:
                        p3.onGround = false;
                        p3.y = mc.thePlayer.posY + 3.1;
                        break;
                }
                dmgProgress++;
            }
        }
    }
    @Override
    public void onEnable(){
        if (mode.get().equals("OldVerus")) {
            dmgProgress = 0;
            movementSpeed[0] = 0.24f;
            movementSpeed[1] = 0;
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
