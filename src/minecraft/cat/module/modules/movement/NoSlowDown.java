package cat.module.modules.movement;

import cat.events.Subscriber;
import cat.events.impl.SlowdownEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.types.FloatValue;

@SuppressWarnings("unused")
public class NoSlowDown extends Module {
    public FloatValue itemMulti = new FloatValue("Reduce by", 1f, 0f, 1f, 0.1f, true, null);
    public NoSlowDown() {
        super("NoSlowDown", "", ModuleCategory.MOVEMENT, "noslow");
    }

    @Subscriber
    public void onSlowdown(SlowdownEvent event) {
        if(itemMulti.get() == 0F) event.cancel();
        event.reducer = itemMulti.get();
    }


}
