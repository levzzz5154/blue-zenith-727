package cat.module.modules.render;

import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.types.IntegerValue;
import cat.ui.clickgui.ClickGui;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class ClickGUI extends Module {
    public static ClickGui clickGui = new ClickGui();
    public Color main_color = Color.WHITE;
    public Color backgroundColor = Color.BLACK;
    public IntegerValue r = new IntegerValue("1", "MainR", 152, 0, 255, true){
        @Override
        public Integer onChange(Integer oldValue, Integer newValue) {
            updateMainColor();
            return super.onChange(oldValue, newValue);
        }
    };
    public IntegerValue g = new IntegerValue("1", "MainG", 195, 0, 255, true){
        @Override
        public Integer onChange(Integer oldValue, Integer newValue) {
            updateMainColor();
            return super.onChange(oldValue, newValue);
        }
    };
    public IntegerValue b = new IntegerValue("1", "MainB", 121, 0, 255, true){
        @Override
        public Integer onChange(Integer oldValue, Integer newValue) {
            updateMainColor();
            return super.onChange(oldValue, newValue);
        }
    };
    public IntegerValue rg = new IntegerValue("1", "BackgroundR", 40, 0, 255, true){
        @Override
        public Integer onChange(Integer oldValue, Integer newValue) {
            updateBackgroundColor();
            return super.onChange(oldValue, newValue);
        }
    };
    public IntegerValue gg = new IntegerValue("1", "BackgroundG", 44, 0, 255, true){
        @Override
        public Integer onChange(Integer oldValue, Integer newValue) {
            updateBackgroundColor();
            return super.onChange(oldValue, newValue);
        }
    };
    public IntegerValue bg = new IntegerValue("1", "BackgroundB", 52, 0, 255, true){
        @Override
        public Integer onChange(Integer oldValue, Integer newValue) {
            updateBackgroundColor();
            return super.onChange(oldValue, newValue);
        }
    };
    {
        updateMainColor();
        updateBackgroundColor();
    }
    public void updateMainColor(){
        main_color = new Color(r.get(), g.get(), b.get());
    }
    public void updateBackgroundColor(){
        backgroundColor = new Color(rg.get(), gg.get(), bg.get());
    }
    public ClickGUI() {
        super("ClickGUI", "", ModuleCategory.RENDER, Keyboard.KEY_RSHIFT);
    }
    @Override
    public void onEnable(){
        mc.displayGuiScreen(clickGui);
    }
}
