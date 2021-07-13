package cat.module.modules.movement;

import cat.events.Subscriber;
import cat.events.impl.UpdateEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.types.FloatValue;
import cat.util.MovementUtil;

public class Speed extends Module {
    FloatValue speed = new FloatValue("", "Speed", 0f, 5f, 0.1f, true);
    public Speed() {
        super("Speed", "", ModuleCategory.MOVEMENT);
    }
    @Subscriber
    public void onUpdate(UpdateEvent e){
        if(mc.thePlayer.onGround){
            mc.thePlayer.jump();
            MovementUtil.setSpeed(speed.get());
        }
    }
}
