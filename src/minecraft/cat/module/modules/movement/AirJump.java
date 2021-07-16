package cat.module.modules.movement;

import cat.events.Subscriber;
import cat.events.impl.UpdatePlayerEvent;
import cat.module.Module;
import cat.module.ModuleCategory;

public class AirJump extends Module {

    public AirJump() {
        super("AirJump", "", ModuleCategory.MOVEMENT);
    }

    @Subscriber
    public void move(UpdatePlayerEvent event) {
        if(mc.gameSettings.keyBindJump.isPressed()) {
            mc.thePlayer.jump();
        }
    }
}
