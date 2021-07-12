package cat.module.modules.render;

import cat.BlueZenith;
import cat.events.Subscriber;
import cat.events.impl.Render2DEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import com.sun.org.apache.xpath.internal.operations.Mod;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class HUD extends Module {
    ArrayList<Module> modules = new ArrayList<>();
    public HUD() {
        super("HUD", "", ModuleCategory.RENDER);
        this.toggle();
    }

    @Subscriber
    public void render(Render2DEvent event) {
        for (Module m : BlueZenith.moduleManager.getModules()) {
            if(m.getState() && !modules.contains(m)){
                modules.add(m);
            }else if(!m.getState()){
                modules.remove(m);
            }
        }
        ScaledResolution sc = event.resolution;
        FontRenderer font = mc.fontRendererObj;
        modules.sort((m, m1) -> Float.compare(font.getStringWidth(m1.getTagName()), font.getStringWidth(m.getTagName())));
        font.drawString("BlueZenith b"+ BlueZenith.version, 5, 5, Color.cyan.darker().getRGB(), true);
        float y = 5;
        for (Module m : modules) {
            if(m.enabled){
                font.drawString(m.getTagName(), sc.getScaledWidth() - font.getStringWidth(m.getTagName()) - 5, y, Color.BLUE.getRGB(), true);
                y += font.FONT_HEIGHT + 2;
            }
        }
    }
}
