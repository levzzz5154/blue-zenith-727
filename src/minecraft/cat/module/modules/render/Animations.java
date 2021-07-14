package cat.module.modules.render;

import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.types.ModeValue;

public class Animations extends Module {
    ModeValue anim = new ModeValue("1", "Animation", "1.7", true, null, "1.7");
    public Animations() {
        super("Animations", "", ModuleCategory.RENDER, "animations", "anim");
    }
}
