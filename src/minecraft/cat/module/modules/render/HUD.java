package cat.module.modules.render;

import cat.BlueZenith;
import cat.events.Subscriber;
import cat.events.impl.Render2DEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.types.BooleanValue;
import cat.module.value.types.FloatValue;
import cat.module.value.types.IntegerValue;
import cat.module.value.types.ModeValue;
import cat.ui.arraylist.IArraylistRenderer;
import cat.ui.arraylist.impl.VanillaFont;
import cat.util.ColorUtil;
import cat.util.RenderUtil;
import cat.util.font.sigma.FontUtil;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;
import java.util.ArrayList;

import static cat.util.ColorUtil.rainbow;

public class HUD extends Module {
    ModeValue fontMode = new ModeValue("Font", "Client", true, null, "Client", "Vanilla");
    BooleanValue shadow = new BooleanValue("FontShadow", true, true, null);
    BooleanValue border = new BooleanValue("Border", true, true, null);
    IntegerValue backgroundOpacity = new IntegerValue("BackgroundOpacity", 50, 0, 255, 1, true, null);
    IntegerValue margin = new IntegerValue( "Margin", 10, 0, 15, 1, true, null);
    ModeValue colorMode = new ModeValue("ColorMode", "Pulse", true, null, "Pulse", "Rainbow");
    FloatValue rainbowMulti = new FloatValue("RainbowMulti", 0.4f,0,1,0.1f, true, the -> colorMode.get().equals("Rainbow"));
    ArrayList<Module> modules = new ArrayList<>();
    private final IArraylistRenderer renderer = new VanillaFont();
    @SuppressWarnings("unused")
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

        ScaledResolution sc = e.resolution;
        // checkmate natasha
        FontRenderer font = fontMode.get().equals("Client") ? FontUtil.fontSFLight42 : mc.fontRendererObj;
        modules.sort((m, m1) -> Float.compare(font.getStringWidth(m1.getTagName()), font.getStringWidth(m.getTagName())));
        String str = BlueZenith.name+" b"+BlueZenith.version;
        char[] strArr = str.toCharArray();
        float x1 = 5;
        for (int i = 0; i < strArr.length; i++) {
            Color c = ColorUtil.getEpicColor(i);
            font.drawString(String.valueOf(strArr[i]), x1, 5, c.getRGB(), shadow.get());
            x1 += font.getStringWidth(String.valueOf(strArr[i]));
        }

        float mar = margin.get();
        float increment = font.FONT_HEIGHT + (mar / 2f);
        //if(fontMode.is("Vanilla")) {
            //renderer.doRender(e, modules, e.resolution, mar, border.get(), backgroundOpacity.get(), shadow.get());
            //return;
        //}
        float y = 0;
        for (int i = 0; i < modules.size(); i++) {
            Module m = modules.get(i);
            Module d = modules.get(i > 0 ? i - 1 : i);
            if(m.hidden) continue;
            Color c = colorMode.get().equals("Rainbow") ? rainbow(i, rainbowMulti.get()) : colorMode.get().equals("Pulse") ? ColorUtil.getEpicColor(i) : new Color(255,255, 255);
            RenderUtil.rect(sc.getScaledWidth() - font.getStringWidthF(m.getTagName()) - mar, y, sc.getScaledWidth(), y + increment, new Color(0,0,0,backgroundOpacity.get()).getRGB());
            if(border.get()){
                RenderUtil.rect(sc.getScaledWidth() - font.getStringWidthF(m.getTagName()) - mar - 1, y, sc.getScaledWidth() - font.getStringWidthF(m.getTagName()) - mar, y + increment, c.getRGB());
                RenderUtil.rect(sc.getScaledWidth() - font.getStringWidthF(d.getTagName()) - mar - 1, y - 1, sc.getScaledWidth() - font.getStringWidthF(m.getTagName()) - mar, y, c.getRGB());
                if(m == modules.get(modules.size() - 1)){
                    RenderUtil.rect(sc.getScaledWidth() - font.getStringWidthF(m.getTagName()) - mar - 1, y + increment - 1, sc.getScaledWidth(), y + increment, c.getRGB());
                }
            }
            font.drawString(m.getTagName(), sc.getScaledWidth() - font.getStringWidthF(m.getTagName()) - (mar / 2f), y + (increment / 2f - font.FONT_HEIGHT / 2f), c.getRGB(), shadow.get());
            y += increment;
        }
        GlStateManager.resetColor();
    }
}
