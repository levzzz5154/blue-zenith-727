package cat.module.modules.render;

import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.types.BooleanValue;

public class AntiBlind extends Module {
    public BooleanValue noPumpkin = new BooleanValue("NoPumpkinOverlay", false, true, null);
    public BooleanValue noBlindnessFog = new BooleanValue("NoBlindnessFog", false, true, null);
    public BooleanValue noLavaFog = new BooleanValue("NoLavaFog", false, true, null);
    public BooleanValue noWaterFog = new BooleanValue("NoWaterFog", false, true, null);
    public AntiBlind() {
        super("AntiBlind", "", ModuleCategory.RENDER);
    }
}
