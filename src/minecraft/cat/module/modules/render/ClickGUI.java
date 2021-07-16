package cat.module.modules.render;

import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.types.IntegerValue;
import cat.ui.clickgui.ClickGui;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class ClickGUI extends Module {
    public static ClickGui clickGui = null;
    public Color main_color = Color.WHITE;
    public Color backgroundColor = Color.BLACK;
    public IntegerValue r = new IntegerValue("MainR", 152, 0, 255, 1, true, (p1, p2) -> {updateMainColor(); return p2;}, null);
    public IntegerValue g = new IntegerValue("MainG", 195, 0, 255, 1, true, (p1, p2) -> {updateMainColor(); return p2;}, null);
    public IntegerValue b = new IntegerValue("MainB", 121, 0, 255, 1, true, (p1, p2) -> {updateMainColor(); return p2;}, null);
    public IntegerValue rg = new IntegerValue("BackgroundR", 40, 0, 255, 1,true, (p1, p2) -> {updateBackgroundColor(); return p2;}, null);
    public IntegerValue gg = new IntegerValue("BackgroundG", 44, 0, 255, 1, true, (p1, p2) -> {updateBackgroundColor(); return p2;}, null);
    public IntegerValue bg = new IntegerValue("BackgroundB", 52, 0, 255, 1, true, (p1, p2) -> {updateBackgroundColor(); return p2;}, null);
    {
        updateMainColor();
        updateBackgroundColor();
    }
    private void updateMainColor(){
        main_color = new Color(r.get(), g.get(), b.get());
    }
    private void updateBackgroundColor(){
        backgroundColor = new Color(rg.get(), gg.get(), bg.get());
    }
    public ClickGUI() {
        super("ClickGUI", "", ModuleCategory.RENDER, Keyboard.KEY_RSHIFT);
    }
    @Override
    public void onEnable(){
        if(clickGui == null){
            clickGui = new ClickGui();
        }
        mc.displayGuiScreen(clickGui);
    }
}
