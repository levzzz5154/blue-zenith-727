package cat.module.modules.movement;

import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.types.FloatValue;

public class NoSlowDown extends Module {
    public FloatValue itemMulti = new FloatValue("4", "Reduce by", 1f, 0f, 1f, 0.1f, true, null);
    public NoSlowDown() {
        super("NoSlowDown", "", ModuleCategory.MOVEMENT, "noslow");
    }
}
