package cat.module.modules.render;

import cat.BlueZenith;
import cat.events.Subscriber;
import cat.events.impl.Render2DEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.ArrayList;

public class HUD extends Module {
    ArrayList<Module> modules = new ArrayList<>();
    public HUD() {
        super("HUD", "", ModuleCategory.RENDER);
        this.setState(true);
    }

    @Subscriber
    public void onRender2D(Render2DEvent e){
        for (Module m : BlueZenith.moduleManager.getModules()) {
            if(m.getState() && !modules.contains(m)){
                modules.add(m);
            }else if(!m.getState()){
                modules.remove(m);
            }
        }
        ScaledResolution sc = e.resolution;
        FontRenderer font = mc.fontRendererObj;
        modules.sort((m, m1) -> Float.compare(font.getStringWidth(m1.getTagName()), font.getStringWidth(m.getTagName())));
        font.drawString("BlueZenith b"+ BlueZenith.version, 5, 5, Color.cyan.darker().getRGB(), true);
        float y = 5;
        for (Module m : modules) {
            if(m.getState()){
                font.drawString(m.getTagName(), sc.getScaledWidth() - font.getStringWidth(m.getTagName()) - 5, y, Color.BLUE.getRGB(), true);
                y += font.FONT_HEIGHT + 2;
            }
        }
    }
}
