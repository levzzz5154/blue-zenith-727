package cat.ui.clickgui.components;

import cat.BlueZenith;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.modules.render.ClickGUI;
import cat.module.value.Value;
import cat.module.value.types.BoolValue;
import cat.module.value.types.FloatValue;
import cat.module.value.types.IntegerValue;
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
    ClickGUI click = (ClickGUI) BlueZenith.moduleManager.getModule(ClickGUI.class);

    public Panel(ModuleCategory category, Module... modules) {
        this.category = category;
        this.modules = modules;

        width = f.getStringWidth(category.displayName) + 12;
        mHeight = f.FONT_HEIGHT + 16;
        for (Module m : modules) {
            if (f.getStringWidth(m.getName()) + 12 > width) {
                width = f.getStringWidth(m.getName()) + 12;
            }
        }
        width = Math.max(width, 150);
    }

    Value<?> sliderVal = null;

    public float drawPanel(float x, float y, int mouseX, int mouseY) {
        Color main_color = click.main_color;
        Color backgroundColor = click.backgroundColor;
        RenderUtil.rect(x - 2, y - 2, x + width + 2, y + mHeight, main_color);
        mc.fontRendererObj.drawString(category.displayName, x + 4, y + mHeight / 2f - f.FONT_HEIGHT / 2f, Color.WHITE.getRGB());
        float y1 = y + mHeight;
        for (Module m : modules) {
            if (i(mouseX, mouseY, x, y1, x + width, y1 + mHeight) && !m.wasPressed()) {
                if (Mouse.isButtonDown(0)) {
                    m.toggle();
                }
                if (Mouse.isButtonDown(1)) {
                    m.showSettings = !m.showSettings;
                }
            }

            RenderUtil.rect(x, y1, x + width, y1 + mHeight, backgroundColor);
            mc.fontRendererObj.drawString(m.getName(), x + 5, y1 + (mHeight / 2f - f.FONT_HEIGHT / 2f), m.getState() ? Color.WHITE.getRGB() : Color.DARK_GRAY.getRGB());
            y1 += mHeight;
            if (m.showSettings) {
                Color settingsColor = backgroundColor.brighter();
                for (Value<?> v : m.getValues()) {
                    if (v instanceof FloatValue) {
                        FloatValue value = (FloatValue) v;
                        float w = width - 4;
                        float n = f.FONT_HEIGHT + 14;
                        final float a = x + w * (Math.max(value.min, Math.min(value.get(), value.max)) - value.min) / (value.max - value.min);
                        RenderUtil.rect(x, y1, x + width, y1 + n, settingsColor);
                        mc.fontRendererObj.drawString(value.name + ": " + value.get(), x + 4, y1 + n / 2f - 8f, Color.WHITE.getRGB());
                        RenderUtil.rect(x + 4, y1 + n / 2f + 4f, x + w, y1 + n / 2f + 6f, new Color(63, 65, 68));
                        RenderUtil.rect(x + 4, y1 + n / 2f + 4f, a, y1 + n / 2f + 6f, main_color);

                        if (Mouse.isButtonDown(0) && ((i(mouseX, mouseY, x, y1 + n / 2f + 4f, x + w, y1 + n / 2f + 6f) && sliderVal == null) || sliderVal == v)) {
                            sliderVal = v;
                            double i = MathHelper.clamp_double(((double) mouseX - (double) x) / ((double) w - 3), 0, 1);

                            BigDecimal bigDecimal = new BigDecimal(Double.toString((value.min + (value.max - value.min) * i)));
                            bigDecimal = bigDecimal.setScale(2, 4);
                            value.set(bigDecimal.floatValue());
                        } else if (!Mouse.isButtonDown(0) && sliderVal == v) {
                            sliderVal = null;
                        }

                        y1 += n;
                    } else if (v instanceof IntegerValue) {
                        IntegerValue value = (IntegerValue) v;
                        float w = width - 4;
                        float n = f.FONT_HEIGHT + 14;
                        final float a = x + w * (Math.max(value.min, Math.min(value.get(), value.max)) - value.min) / (value.max - value.min);
                        RenderUtil.rect(x, y1, x + width, y1 + n, settingsColor);
                        mc.fontRendererObj.drawString(value.name + ": " + value.get(), x + 4, y1 + n / 2f - 8f, Color.WHITE.getRGB());
                        RenderUtil.rect(x + 4, y1 + n / 2f + 4f, x + w, y1 + n / 2f + 6f, new Color(63, 65, 68));
                        RenderUtil.rect(x + 4, y1 + n / 2f + 4f, a, y1 + n / 2f + 6f, main_color);

                        if (Mouse.isButtonDown(0) && ((i(mouseX, mouseY, x, y1 + n / 2f + 4f, x + w, y1 + n / 2f + 6f) && sliderVal == null) || sliderVal == v)) {
                            sliderVal = v;
                            double i = MathHelper.clamp_double(((double) mouseX - (double) x) / ((double) w - 3), 0, 1);

                            BigDecimal bigDecimal = new BigDecimal(Double.toString((value.min + (value.max - value.min) * i)));
                            bigDecimal = bigDecimal.setScale(2, 4);
                            value.set(bigDecimal.intValue());
                        } else if (!Mouse.isButtonDown(0) && sliderVal == v) {
                            sliderVal = null;
                        }

                        y1 += n;
                    } else if (v instanceof BoolValue) {
                        BoolValue val = (BoolValue) v;
                        RenderUtil.rect(x, y1, x + width, y1 + mHeight - 2, settingsColor);
                        mc.fontRendererObj.drawString(val.name, x + 5, y1 + ((mHeight - 2) / 2f - f.FONT_HEIGHT / 2f), val.value ? main_color.getRGB() : Color.DARK_GRAY.getRGB());
                        if (i(mouseX, mouseY, x, y1, x + width, y1 + mHeight) && Mouse.isButtonDown(0) && !m.wasPressed()) {
                            val.value = !val.value;
                        }
                        y1 += mHeight - 2;
                    }
                }
            }
            m.updatePressed();
        }
        return width;
    }

    public boolean i(int mouseX, int mouseY, float x, float y, float x2, float y2) {
        return mouseX >= x && mouseY >= y && mouseX <= x2 && mouseY <= y2;
    }
}
