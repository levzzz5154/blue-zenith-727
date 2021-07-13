package cat.module.modules.render;

import cat.module.Module;
import cat.module.ModuleCategory;
import cat.ui.clickgui.ClickGui;
import org.lwjgl.input.Keyboard;

public class ClickGUI extends Module {
    public static ClickGui clickGui = new ClickGui();
    public ClickGUI() {
        super("ClickGUI", "", ModuleCategory.RENDER, Keyboard.KEY_RSHIFT);
    }
    @Override
    public void onEnable(){
        mc.displayGuiScreen(clickGui);
    }
}
