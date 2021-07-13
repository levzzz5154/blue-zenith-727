package cat.ui.clickgui.components;

import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.Value;
import cat.module.value.types.BoolValue;
import cat.module.value.types.FloatValue;
import cat.util.MinecraftInstance;
import cat.util.RenderUtil;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.input.Mouse;

import java.awt.*;

public class Panel extends MinecraftInstance {
    ModuleCategory category;
    Module[] modules;
    float width;
    float mHeight;
    FontRenderer f = mc.fontRendererObj;
    Color main_color = Color.GREEN;
    public Panel(ModuleCategory category, Module... modules){
        this.category = category;
        this.modules = modules;

        width = f.getStringWidth(category.displayName) + 12;
        mHeight = f.FONT_HEIGHT + 12;
        for (Module m : modules) {
            if(f.getStringWidth(m.getName()) + 12 > width){
                width = f.getStringWidth(m.getName()) + 12;
            }
        }
        width = Math.max(width, 100);
    }
    public float drawPanel(float x, float y, int mouseX, int mouseY){
        RenderUtil.rect(x - 2, y - 2, x + width + 2, y + mHeight, main_color);
        mc.fontRendererObj.drawString(category.displayName, x + 4, y + f.FONT_HEIGHT / 2f, Color.WHITE.getRGB());
        float y1 = y + mHeight;
        for (Module m : modules) {
            if(i(mouseX, mouseY, x, y1, x + width, y1 + mHeight) && !m.wasPressed()){
                if(Mouse.isButtonDown(0)){
                    m.toggle();
                }
                if(Mouse.isButtonDown(1)){
                    m.showSettings = !m.showSettings;
                }
            }

            RenderUtil.rect(x, y1, x + width, y1 + mHeight, Color.BLACK);
            mc.fontRendererObj.drawString(m.getName(), x + 5, y1 + (f.FONT_HEIGHT / 2f), m.getState() ? Color.WHITE.getRGB() : Color.DARK_GRAY.getRGB());
            y1 += mHeight;
            if(m.showSettings){
                for (Value<?> v : m.getValues()) {
                    if(v instanceof FloatValue){
                        RenderUtil.rect(x, y1, x + width, y1 + mHeight + 5, Color.BLACK);
                        mc.fontRendererObj.drawString("*insert slider*", x, y1, Color.WHITE.getRGB());
                        y1 += mHeight + 5;
                    }else if(v instanceof BoolValue){
                        BoolValue val = (BoolValue) v;
                        RenderUtil.rect(x, y1, x + width, y1 + mHeight, Color.BLACK);
                        mc.fontRendererObj.drawString(val.name, x + 5, y1 + f.FONT_HEIGHT / 2f, val.value ? main_color.getRGB() : Color.WHITE.getRGB());
                        if(i(mouseX, mouseY, x, y1, x + width, y1 + mHeight) && Mouse.isButtonDown(0) && !m.wasPressed()){
                            val.value = !val.value;
                        }
                        y1 += mHeight;
                    }
                }
            }
            m.updatePressed();
        }
        return width;
    }
    public boolean i(int mouseX, int mouseY, float x, float y, float x2, float y2){
        return mouseX >= x && mouseY >= y && mouseX <= x2 && mouseY <= y2;
    }
}
