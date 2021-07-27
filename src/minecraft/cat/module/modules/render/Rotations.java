package cat.module.modules.render;

import cat.module.Module;
import cat.module.ModuleCategory;

public class Rotations extends Module {
    public Rotations() {
        super("Rotations", "", ModuleCategory.RENDER, "rotations", "rot");
    }
    public float yaw = 0;
    public float pitch = 0;
    public float prevYaw = 0;
    public float prevPitch = 0;
}
