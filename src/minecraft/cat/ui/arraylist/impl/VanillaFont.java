package cat.ui.arraylist.impl;

import cat.events.impl.Render2DEvent;
import cat.module.Module;
import cat.ui.arraylist.IArraylistRenderer;
import cat.util.ColorUtil;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.ArrayList;

import static cat.util.MinecraftInstance.mc;

public class VanillaFont implements IArraylistRenderer {

    @Override
    public void doRender(Render2DEvent event, ArrayList<Module> modules, ScaledResolution resolution, float margin, boolean shadow, int opacity, boolean textShadow) {
        FontRenderer font = mc.fontRendererObj;
        modules.sort((m, m1) -> Float.compare(font.getStringWidth(m1.getTagName()), font.getStringWidth(m.getTagName())));
        float increment = font.FONT_HEIGHT + (margin / 2f);

        float y = 0;
        for (int i = 0; i < modules.size(); i++) {
            Module m = modules.get(i);
            if(m.hidden) continue;
            Module burgir = modules.get(i > 0 ? i - 1 : i);
            Color c = ColorUtil.getEpicColor(i);
            Gui.drawRect(resolution.getScaledWidth() - font.getStringWidth(m.getTagName()) - margin + 2, y, resolution.getScaledWidth(), y + increment, new Color(0,0,0,opacity).getRGB());
            if(shadow){
                Gui.drawRect(resolution.getScaledWidth() - font.getStringWidth(m.getTagName()) - margin + 0.5f, y, resolution.getScaledWidth() - font.getStringWidth(m.getTagName()) - margin + 2, y + increment, c.getRGB());
                Gui.drawRect(resolution.getScaledWidth() - font.getStringWidth(burgir.getTagName()) - margin + 0.5f, y - 1, resolution.getScaledWidth() - font.getStringWidth(m.getTagName()) - margin + 2, y, c.getRGB());
                if(m == modules.get(modules.size() - 1)){
                    Gui.drawRect(resolution.getScaledWidth() - font.getStringWidth(m.getTagName()) - margin + 0.5f, y + increment - 1, resolution.getScaledWidth() + 2, y + increment, c.getRGB());
                }
            }
            font.drawString(m.getTagName(), resolution.getScaledWidth() - font.getStringWidth(m.getTagName()) - (margin / 2f) + 2, y + (increment / 2f - font.FONT_HEIGHT / 2f), c.getRGB(), textShadow);
            y += increment;
        }
    }
}
