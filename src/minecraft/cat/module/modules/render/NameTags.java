package cat.module.modules.render;

import cat.module.Module;
import cat.module.ModuleCategory;

public class NameTags extends Module {
    //lmao this is pasted from my custom liquidbounce
    public NameTags() {
        super("NameTags", "", ModuleCategory.RENDER, "NameTags");
    }
    public static double round(double value) {
        int scale = (int) Math.pow(10, 1);
        return (double) Math.round(value * scale) / scale;
    }
}
