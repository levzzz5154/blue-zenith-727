package cat.module.modules.movement;

import cat.events.impl.UpdatePlayerEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import com.google.common.eventbus.Subscribe;

public class AirJump extends Module {

    public AirJump() {
        super("AirJump", "", ModuleCategory.MOVEMENT);
    }

    @Subscribe
    public void move(UpdatePlayerEvent event) {
        if(mc.gameSettings.keyBindJump.isPressed()) {
            mc.thePlayer.jump();
        }
    }
}
