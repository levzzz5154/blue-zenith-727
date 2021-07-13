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
    public void onRender2D(Render2DEvent e) {
        for (Module m : BlueZenith.moduleManager.getModules()) {
            if (m.getState() && !modules.contains(m)) {
                modules.add(m);
            } else if (!m.getState()) {
                modules.remove(m);
            }
        }
        ScaledResolution sc = e.resolution;
        FontRenderer font = mc.fontRendererObj;
        modules.sort((m, m1) -> Float.compare(font.getStringWidth(m1.getTagName()), font.getStringWidth(m.getTagName())));
        font.drawString("BlueZenith b" + BlueZenith.version, 5, 5, Color.cyan.darker().getRGB(), true);
        float y = 5;
        int cf = 0;
        for (Module m : modules) {
            Color c = hi(new Color(50, 170, 150), new Color(50, 140, 160), Math.abs(System.currentTimeMillis() / 10L) / 100.0 + 6.0F * (cf + 2.55) / 60);
            font.drawString(m.getTagName(), sc.getScaledWidth() - font.getStringWidth(m.getTagName()) - 5, y, c.getRGB(), true);
            y += font.FONT_HEIGHT + 2;
            cf -= 1;
        }
    }
    public static Color hi(final Color color, final Color color2, double delay) {
        if (delay > 1.0) {
            final double n2 = delay % 1.0;
            delay = (((int) delay % 2 == 0) ? n2 : (1.0 - n2));
        }
        final double n3 = 1.0 - delay;
        return new Color((int) (color.getRed() * n3 + color2.getRed() * delay), (int) (color.getGreen() * n3 + color2.getGreen() * delay), (int) (color.getBlue() * n3 + color2.getBlue() * delay), (int) (color.getAlpha() * n3 + color2.getAlpha() * delay));
    }
}
