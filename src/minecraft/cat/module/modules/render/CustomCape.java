package cat.module.modules.render;

import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.types.BoolValue;
import net.minecraft.util.ResourceLocation;

@SuppressWarnings("unused")
public class CustomCape extends Module {
    ResourceLocation d = null;
    BoolValue selectCape = new BoolValue("1", "SelectCape", false, true, null){
    };
    public CustomCape() {
        super("CustomCape", "", ModuleCategory.RENDER, "customcape", "cape");
    }
    public ResourceLocation getCapeLocation(){
        return d == null ? new ResourceLocation("cat/cape.png") : d;
    }
}
