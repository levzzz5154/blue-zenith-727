package cat.module.modules.movement;

import cat.events.Subscriber;
import cat.events.impl.UpdateEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.types.FloatValue;
import cat.util.MovementUtil;
import org.lwjgl.input.Keyboard;

@SuppressWarnings("unused")
public class Speed extends Module {
    FloatValue speed = new FloatValue("", "Speed", 0.31f, 0f, 5f, 0.1f, true, null);
    public Speed() {
        super("Speed", "", ModuleCategory.MOVEMENT, Keyboard.KEY_ADD,"bhop");
    }
    @Subscriber
    public void onUpdate(UpdateEvent e){
        MovementUtil.setSpeed(speed.get());
        if(mc.thePlayer.onGround && MovementUtil.areMovementKeysPressed()){
            mc.thePlayer.jump();
        }
    }
}
