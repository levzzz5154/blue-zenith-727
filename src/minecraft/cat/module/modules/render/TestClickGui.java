package cat.module.modules.render;

import cat.module.Module;
import cat.module.ModuleCategory;
import cat.ui.clickgui.IntellijClickGui;

public class TestClickGui extends Module {
    public static IntellijClickGui click = null;
    public TestClickGui() {
        super("TestClickGui", "", ModuleCategory.RENDER);
    }
    @Override
    public void onEnable(){
        if(click == null) click = new IntellijClickGui();
        mc.displayGuiScreen(click);
    }
}
