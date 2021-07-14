package cat.ui.clickgui.components;

import cat.BlueZenith;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.modules.render.ClickGUI;
import cat.module.value.Value;
import cat.module.value.types.BoolValue;
import cat.module.value.types.FloatValue;
import cat.module.value.types.IntegerValue;
import cat.module.value.types.ModeValue;
import cat.util.MinecraftInstance;
import cat.util.RenderUtil;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class Panel extends MinecraftInstance {
    ModuleCategory category;
    Module[] modules;
    public float width;
    float mHeight;
    public float x,y;
    public float prevX,prevY;
    FontRenderer f = mc.fontRendererObj;
    ClickGUI click = (ClickGUI) BlueZenith.moduleManager.getModule(ClickGUI.class);
    public Panel(float x, float y, ModuleCategory category, Module... modules){
        this.category = category;
        this.modules = modules;
        this.x = x;
        this.y = y;

        width = f.getStringWidth(category.displayName) + 12;
        mHeight = f.FONT_HEIGHT + 16;
        for (Module m : modules) {
            if(f.getStringWidth(m.getName()) + 12 > width){
                width = f.getStringWidth(m.getName()) + 12;
            }
        }
        width = Math.max(width, 150);
    }
    Value<?> sliderVal = null;
    public void drawPanel(int mouseX, int mouseY){
        Color main_color = click.main_color;
        Color backgroundColor = click.backgroundColor;

        RenderUtil.rect(x - 2, y - 2, x + width + 2, y + mHeight, main_color);
        mc.fontRendererObj.drawString(category.displayName, x + 4, y + mHeight / 2f - f.FONT_HEIGHT / 2f, Color.WHITE.getRGB());
        float y1 = y + mHeight;
        for (Module m : modules) {
            List<Value<?>> vl = m.getValues();
            if(i(mouseX, mouseY, x, y1, x + width, y1 + mHeight) && !m.wasPressed()){
                if(Mouse.isButtonDown(0)){
                    m.toggle();
                    mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
                }
                if(Mouse.isButtonDown(1) && !vl.isEmpty()){
                    m.showSettings = !m.showSettings;
                    mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
                }
            }

            RenderUtil.rect(x, y1, x + width, y1 + mHeight, backgroundColor);
            mc.fontRendererObj.drawString(m.getName(), x + 5, y1 + (mHeight / 2f - f.FONT_HEIGHT / 2f), m.getState() ? main_color.getRGB() : main_color.darker().darker().getRGB());
            y1 += mHeight;
            if(m.showSettings){
                for (Value<?> v : vl) {
                    if(!v.isVisible()) continue;
                    Color settingsColor = backgroundColor.brighter();
                    float h = f.FONT_HEIGHT + 14;
                    if(v instanceof ModeValue) {
                        ModeValue val = (ModeValue) v;
                        RenderUtil.rect(x, y1, x + width, y1 + h, settingsColor);
                        FontRenderer font = mc.fontRendererObj;
                        font.drawString(val.name, x + 5, y1 + h / 2f - f.FONT_HEIGHT / 2f, main_color.getRGB());
                        font.drawString(val.value, x + 10 + font.getStringWidth(val.name), y1 + h / 2f - f.FONT_HEIGHT / 2f, Color.GRAY.getRGB());
                        if(i(mouseX, mouseY, x, y1, x + width, y1 + h) && !m.wasPressed()) {
                            if(Mouse.isButtonDown(0)) {
                                val.next();
                                mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
                            } else if(Mouse.isButtonDown(1)) {
                                val.previous();
                                mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
                            }
                        }
                        y1 += h;
                    }if (v instanceof FloatValue) {
                        FloatValue value = (FloatValue) v;
                        float w = width - 4;
                        final float a = x + w * (Math.max(value.min, Math.min(value.get(), value.max)) - value.min) / (value.max - value.min);
                        RenderUtil.rect(x, y1, x + width, y1 + h, settingsColor);
                        mc.fontRendererObj.drawString(value.name + ": " + value.get(), x + 4, y1 + h / 2f - 8f, Color.WHITE.getRGB());
                        RenderUtil.rect(x + 4, y1 + h / 2f + 4f, x + w, y1 + h / 2f + 6f, new Color(63, 65, 68));
                        RenderUtil.rect(x + 4, y1 + h / 2f + 4f, a, y1 + h / 2f + 6f, main_color);

                        if (Mouse.isButtonDown(0) && ((i(mouseX, mouseY, x, y1 + h / 2f + 4f, x + w, y1 + h / 2f + 6f) && sliderVal == null) || sliderVal == v)) {
                            sliderVal = v;
                            double i = MathHelper.clamp_double(((double) mouseX - (double) x) / ((double) w - 3), 0, 1);

                            BigDecimal bigDecimal = new BigDecimal(Double.toString((value.min + (value.max - value.min) * i)));
                            bigDecimal = bigDecimal.setScale(2, 4);
                            value.set(bigDecimal.floatValue());
                        } else if (!Mouse.isButtonDown(0) && sliderVal == v) {
                            sliderVal = null;
                        }

                        y1 += h;
                    } else if (v instanceof IntegerValue) {
                        IntegerValue value = (IntegerValue) v;
                        float w = width - 4;
                        final float a = x + w * (Math.max(value.min, Math.min(value.get(), value.max)) - value.min) / (value.max - value.min);
                        RenderUtil.rect(x, y1, x + width, y1 + h, settingsColor);
                        mc.fontRendererObj.drawString(value.name + ": " + value.get(), x + 4, y1 + h / 2f - 8f, main_color.getRGB());
                        RenderUtil.rect(x + 4, y1 + h / 2f + 4f, x + w, y1 + h / 2f + 6f, new Color(63, 65, 68));
                        RenderUtil.rect(x + 4, y1 + h / 2f + 4f, a, y1 + h / 2f + 6f, main_color);

                        if (Mouse.isButtonDown(0) && ((i(mouseX, mouseY, x, y1 + h / 2f + 4f, x + w, y1 + h / 2f + 6f) && sliderVal == null) || sliderVal == v)) {
                            sliderVal = v;
                            double i = MathHelper.clamp_double(((double) mouseX - (double) x) / ((double) w - 3), 0, 1);

                            BigDecimal bigDecimal = new BigDecimal(Double.toString((value.min + (value.max - value.min) * i)));
                            bigDecimal = bigDecimal.setScale(2, 4);
                            value.set(bigDecimal.intValue());
                        } else if (!Mouse.isButtonDown(0) && sliderVal == v) {
                            sliderVal = null;
                        }

                        y1 += h;
                    } else if (v instanceof BoolValue) {
                        BoolValue val = (BoolValue) v;
                        RenderUtil.rect(x, y1, x + width, y1 + h, settingsColor);
                        mc.fontRendererObj.drawString(val.name, x + 5, y1 + (h / 2f - f.FONT_HEIGHT / 2f), val.value ? main_color.getRGB() : main_color.darker().darker().getRGB());
                        if (i(mouseX, mouseY, x, y1, x + width, y1 + h) && (Mouse.isButtonDown(0) || Mouse.isButtonDown(1)) && !m.wasPressed()) {
                            val.next();
                            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
                        }
                        y1 += h;
                    }
                }
            }
            m.updatePressed();
        }
    }
    public boolean i(int mouseX, int mouseY, float x, float y, float x2, float y2){
        return mouseX >= x && mouseY >= y && mouseX <= x2 && mouseY <= y2;
    }
}
