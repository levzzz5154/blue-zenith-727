package cat.module.modules.movement;

import cat.events.Subscriber;
import cat.events.impl.BlockBBEvent;
import cat.events.impl.UpdateEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.types.BoolValue;
import cat.module.value.types.FloatValue;
import cat.module.value.types.ModeValue;
import cat.util.MovementUtil;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.input.Keyboard;

@SuppressWarnings("unused")
public class Flight extends Module {
    FloatValue speed = new FloatValue("1", "Speed", 2f, 0f, 5f, true);
    private final ModeValue mode = new ModeValue("flight_mode", "Mode", "Vanilla", true, "Vanilla", "Verus", "Mode3", "Mode4");
    public Flight() {
        super("Flight", "Vanilla", ModuleCategory.MOVEMENT, Keyboard.KEY_F);
    }
    @Subscriber
    public void onUpdate(UpdateEvent e){
        switch(mode.get()){
            case "Vanilla":
                if(MovementUtil.isMovementKeysPressed()){
                    MovementUtil.setSpeed(speed.get());
                }else{
                    mc.thePlayer.motionX = 0;
                    mc.thePlayer.motionZ = 0;
                }
                if(mc.gameSettings.keyBindJump.isKeyDown()){
                    mc.thePlayer.motionY = speed.get();
                }else if(mc.gameSettings.keyBindSneak.isKeyDown()){
                    mc.thePlayer.motionY = -speed.get();
                }else{
                    mc.thePlayer.motionY = 0;
                }
                break;
            case "Verus":
                MovementUtil.setSpeed(0.26f);
                break;
        }
    }
    @Subscriber
    public void onBlockBB(BlockBBEvent e){
        if(!e.block.getMaterial().isSolid() && mode.get().equalsIgnoreCase("Verus")){
            e.blockBB = AxisAlignedBB.fromBounds(-5, -1, -5, 5 , 1, 5).offset(e.pos.getX(), e.pos.getY(), e.pos.getZ());
        }
    }
}
