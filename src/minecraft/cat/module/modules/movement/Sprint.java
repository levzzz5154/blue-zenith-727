package cat.module.modules.movement;

import cat.events.Subscriber;
import cat.events.impl.UpdateEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.types.BoolValue;
import cat.module.value.types.ModeValue;
import cat.util.MovementUtil;

@SuppressWarnings("unused")
public class Sprint extends Module {
    public Sprint() {
        super("Sprint", "", ModuleCategory.MOVEMENT, "Sprint");
    }

    @Subscriber
    public void onUpdate(UpdateEvent e) {
        if (MovementUtil.areMovementKeysPressed()) {
            if (!mc.thePlayer.isCollidedHorizontally) {
                if (!mc.thePlayer.isUsingItem()) {
                    if (!mc.thePlayer.isSneaking()) {
                        mc.thePlayer.setSprinting(true);
                    }
                }
            }
        }
    }
}





