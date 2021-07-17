package cat.module.modules.render;

import cat.BlueZenith;
import cat.events.Subscriber;
import cat.events.impl.Render2DEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.types.BoolValue;
import cat.module.value.types.IntegerValue;
import cat.util.lmao.FontUtil;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;
import java.util.ArrayList;

public class HUD extends Module {
    BoolValue shadow = new BoolValue("FontShadow", true, true, null);
    BoolValue icame = new BoolValue("Border", true, true, null);
    IntegerValue backgroundOpacity = new IntegerValue("BackgroundOpacity", 50, 0, 255, 1, true, null);
    IntegerValue margin = new IntegerValue( "Margin", 10, 0, 15, 1, true, null);
    ArrayList<Module> modules = new ArrayList<>();
    public HUD() {
        super("HUD", "", ModuleCategory.RENDER);
        this.setState(true);
    }

    @Subscriber
    public void onRender2D(Render2DEvent e) {
        if(mc.gameSettings.showDebugInfo) return;
        for (Module m : BlueZenith.moduleManager.getModules()) {
            if (m.getState() && !modules.contains(m)) {
                modules.add(m);
            } else if (!m.getState()) {
                modules.remove(m);
            }
        }
        Color colorDark = new Color(0,40,40);
        Color color = new Color(0, 140, 160);
        ScaledResolution sc = e.resolution;
        FontRenderer font = FontUtil.fontSFLight42;
        modules.sort((m, m1) -> Float.compare(font.getStringWidth(m1.getTagName()), font.getStringWidth(m.getTagName())));
        String str = BlueZenith.name+" b"+BlueZenith.version;
        char[] strArr = str.toCharArray();
        float x1 = 5;
        for (int i = 0; i < strArr.length; i++) {
            Color c = hi(colorDark, color, Math.abs(System.currentTimeMillis() / 10L) / 100.0 + 6.0F * (i + 2.55) / 60);
            font.drawString(String.valueOf(strArr[i]), x1, 5, c.getRGB(), shadow.get());
            x1 += font.getStringWidth(String.valueOf(strArr[i]));
        }
        float mar = margin.get();
        float increment = font.FONT_HEIGHT + (mar / 2f);
        float y = 0;
        for (int i = 0; i < modules.size(); i++) {
            Module m = modules.get(i);
            Module burgir = modules.get(i > 0 ? i - 1 : i);
            Color c = hi(colorDark, color, Math.abs(System.currentTimeMillis() / 10L) / 100.0 + 6.0F * ((i * 2) + 2.55) / 60);
            Gui.drawRect(sc.getScaledWidth() - font.getStringWidth(m.getTagName()) - mar, y, sc.getScaledWidth(), y + increment, new Color(0,0,0,backgroundOpacity.get()).getRGB());
            if(icame.get()){
                Gui.drawRect(sc.getScaledWidth() - font.getStringWidth(m.getTagName()) - mar - 1, y, sc.getScaledWidth() - font.getStringWidth(m.getTagName()) - mar, y + increment, c.getRGB());
                Gui.drawRect(sc.getScaledWidth() - font.getStringWidth(burgir.getTagName()) - mar - 1, y - 1, sc.getScaledWidth() - font.getStringWidth(m.getTagName()) - mar, y, c.getRGB());
                if(m == modules.get(modules.size() - 1)){
                    Gui.drawRect(sc.getScaledWidth() - font.getStringWidth(m.getTagName()) - mar - 1, y + increment - 1, sc.getScaledWidth(), y + increment, c.getRGB());
                }
            }
            font.drawString(m.getTagName(), sc.getScaledWidth() - font.getStringWidth(m.getTagName()) - (mar / 2f), y + (increment / 2f - font.FONT_HEIGHT / 2f), c.getRGB(), shadow.get());
            y += increment;
        }
        GlStateManager.resetColor();
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
