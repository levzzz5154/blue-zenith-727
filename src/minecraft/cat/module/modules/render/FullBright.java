package cat.module.modules.render;

import cat.events.Subscriber;
import cat.events.impl.Render2DEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.input.Keyboard;

public class FullBright extends Module {
    public FullBright() {
        super("FullBright", "", ModuleCategory.RENDER, Keyboard.KEY_V);
    }
    float prevBrightness = 0;
    public void onEnable(){
        prevBrightness = mc.gameSettings.gammaSetting;
        mc.gameSettings.gammaSetting = 10000;
    }
    public void onDisable(){
        mc.gameSettings.gammaSetting = prevBrightness;
    }
}
