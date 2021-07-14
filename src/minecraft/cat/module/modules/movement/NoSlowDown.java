package cat.module.modules.movement;

import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.types.FloatValue;

public class NoSlowDown extends Module {
    public FloatValue itemMulti = new FloatValue("4", "ItemUseMultiplier", 1f, 0f,1f,true);
    public NoSlowDown() {
        super("NoSlowDown", "", ModuleCategory.MOVEMENT, "noslow");
    }
}
