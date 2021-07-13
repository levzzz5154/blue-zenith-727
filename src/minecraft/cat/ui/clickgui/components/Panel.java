package cat.ui.clickgui.components;

import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.Value;
import cat.module.value.types.BoolValue;
import cat.module.value.types.FloatValue;
import cat.module.value.types.ModeValue;
import cat.util.MinecraftInstance;
import cat.util.RenderUtil;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.math.BigDecimal;

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
        width = Math.max(width, 150);
    }
    Value<?> sliderVal = null;
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
                    if(v instanceof ModeValue) {
                        ModeValue val = (ModeValue) v;
                        RenderUtil.rect(x, y1, x + width, y1 + mHeight, Color.BLACK);
                        FontRenderer font = mc.fontRendererObj;
                        font.drawString(val.name, x + 5, y1 + f.FONT_HEIGHT / 2f, main_color.getRGB());
                        font.drawString(val.value, x + 10 + font.getStringWidth(val.name), y1 + f.FONT_HEIGHT / 2f, Color.GRAY.getRGB());
                        //mc.fontRendererObj.drawString(val.name, x + 5, y1 + f.FONT_HEIGHT / 2f, main_color.getRGB());
                        //mc.fontRendererObj.drawString(val.value, x + 5 , y1 + f.FONT_HEIGHT / 2f, main_color.getRGB());
                        if(i(mouseX, mouseY, x, y1, x + width, y1 + mHeight) && !m.wasPressed()) {
                            if(Mouse.isButtonDown(0)) {
                                val.next();
                            } else if(Mouse.isButtonDown(1)) {
                                val.previous();
                            }
                        }
                        y1 += mHeight;
                    }
                    else if(v instanceof FloatValue){
                        FloatValue value = (FloatValue) v;
                        float w = width - 4;
                        final float a = x + w * (Math.max(value.min, Math.min(value.get(), value.max)) - value.min) / (value.max - value.min);
                        RenderUtil.rect(x, y1, x + width, y1 + mHeight + 5, Color.BLACK);
                        mc.fontRendererObj.drawString(value.name + ": "+value.get(), x + 4, y1, Color.WHITE.getRGB());
                        y1 += f.FONT_HEIGHT + 3;
                        RenderUtil.rect(x + 4, y1, x + w, y1 + 2f,  new Color(63, 65, 68));
                        RenderUtil.rect(x + 4, y1, a, y1 + 2f, main_color);

                        if (Mouse.isButtonDown(0) && ((i(mouseX, mouseY, x, y1, x + w, y1 + 3) && sliderVal == null) || sliderVal == v)) {
                            sliderVal = v;
                            double i = MathHelper.clamp_double(((double) mouseX - (double) x) / ((double) w - 3), 0, 1);

                            BigDecimal bigDecimal = new BigDecimal(Double.toString((value.min + (value.max - value.min) * i)));
                            bigDecimal = bigDecimal.setScale(2, 4);
                            value.set(bigDecimal.floatValue());
                        }else if(!Mouse.isButtonDown(0) && sliderVal == v){
                            sliderVal = null;
                        }

                        y1 += 5;
                    }else if(v instanceof BoolValue){
                        BoolValue val = (BoolValue) v;
                        RenderUtil.rect(x, y1, x + width, y1 + mHeight, Color.BLACK);
                        mc.fontRendererObj.drawString(val.name, x + 5, y1 + f.FONT_HEIGHT / 2f, val.value ? main_color.getRGB() : Color.DARK_GRAY.getRGB());
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
