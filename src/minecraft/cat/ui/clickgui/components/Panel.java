package cat.ui.clickgui.components;

import cat.BlueZenith;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.modules.render.ClickGUI;
import cat.module.value.Value;
import cat.module.value.types.*;
import cat.util.MinecraftInstance;
import cat.util.RenderUtil;
import cat.util.lmao.FontUtil;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Panel extends MinecraftInstance {
    ModuleCategory category;
    ArrayList<Module> modules;
    public float width;
    float mHeight;
    public float x,y;
    public float prevX,prevY;
    public boolean hidden = false;
    private final FontRenderer f;
    ClickGUI click = (ClickGUI) BlueZenith.moduleManager.getModule(ClickGUI.class);
    public Panel(float x, float y, ModuleCategory category){
        f = FontUtil.fontSFLight35;
        this.category = category;
        this.modules = new ArrayList<>();
        this.x = x;
        this.y = y;
        mHeight = f.FONT_HEIGHT + 14;
    }
    public Panel calculateWidth(){
        width = f.getStringWidth(category.displayName) + 12;
        for (Module m : modules) {
            if(f.getStringWidth(m.getName()) + 12 > width){
                width = f.getStringWidth(m.getName()) + 12;
            }
        }
        width = Math.max(width, 120);
        return this;
    }
    public void addModule(Module mod){
        this.modules.add(mod);
    }
    float textFieldCounter = 0;
    Value<?> sliderVal = null;
    private boolean wasPressed = false;
    public void drawPanel(int mouseX, int mouseY, float partialTicks){
        Color main_color = click.main_color;
        Color backgroundColor = click.backgroundColor;

        RenderUtil.rect(x, y, x + width, y + mHeight, main_color);
        f.drawString(category.displayName, x + 4, y + mHeight / 2f - f.FONT_HEIGHT / 2f, Color.WHITE.getRGB(), true);
        float y1 = y + mHeight;
        for (Module m : modules) {
            if(hidden) return;
            List<Value<?>> vl = m.getValues();
            if(i(mouseX, mouseY, x, y1, x + width, y1 + mHeight) && !wasPressed){
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
            f.drawString(m.getName(), x + 5, y1 + (mHeight / 2f - f.FONT_HEIGHT / 2f), m.getState() ? main_color.getRGB() : main_color.darker().darker().getRGB());
            y1 += mHeight;
            if(m.showSettings) {
                for (Value<?> v : vl) {
                    //lmao i forgot this
                    if(!v.isVisible()) continue;
                    float w = width;
                    Color settingsColor = backgroundColor.brighter();
                    float h = f.FONT_HEIGHT + 8;
                    float y1r = y1;
                    float y2r = y1 + h;
                    if(v instanceof ModeValue) {
                        ModeValue val = (ModeValue) v;
                        RenderUtil.rect(x, y1, x + width, y1 + h, settingsColor);
                        f.drawString(val.name, x + 5, y1 + h / 2f - f.FONT_HEIGHT / 2f, main_color.getRGB());
                        f.drawString(val.get(), x + 10 + f.getStringWidth(val.name), y1 + (h / 2f - f.FONT_HEIGHT / 2f), Color.GRAY.getRGB());
                        if(i(mouseX, mouseY, x, y1, x + width, y1 + h) && !wasPressed) {
                            if(Mouse.isButtonDown(0)) {
                                val.next();
                                mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
                            } else if(Mouse.isButtonDown(1)) {
                                val.previous();
                                mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
                            }
                        }
                        y1 += h;
                    } else if (v instanceof FloatValue) {
                        FloatValue value = (FloatValue) v;
                        final float a = x + w * (Math.max(value.min, Math.min(value.get(), value.max)) - value.min) / (value.max - value.min);
                        RenderUtil.rect(x, y1, x + width, y1 + h, settingsColor);
                        RenderUtil.rect(x, y1r, x + w, y2r, new Color(63, 65, 68));
                        RenderUtil.rect(x, y1r, a, y2r, main_color.darker().darker());
                        f.drawString(value.name + ": " + value.get(), x + 4, y1 + (h / 2f - f.FONT_HEIGHT / 2f), main_color.getRGB());

                        if (Mouse.isButtonDown(0) && ((i(mouseX, mouseY, x, y1r, x + w, y2r) && sliderVal == null) || sliderVal == v)) {
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
                        final float a = x + w * (Math.max(value.min, Math.min(value.get(), value.max)) - value.min) / (value.max - value.min);
                        RenderUtil.rect(x, y1, x + width, y1 + h, settingsColor);
                        RenderUtil.rect(x, y1r, x + w, y2r, new Color(63, 65, 68));
                        RenderUtil.rect(x, y1r, a, y2r, main_color.darker().darker());
                        f.drawString(value.name + ": " + value.get(), x + 4, y1 + h / 2f - f.FONT_HEIGHT / 2f, main_color.getRGB());

                        if (Mouse.isButtonDown(0) && ((i(mouseX, mouseY, x, y1r, x + w, y2r) && sliderVal == null) || sliderVal == v)) {
                            sliderVal = v;
                            double i = MathHelper.clamp_double(((double) mouseX - (double) x) / ((double) w - 3), 0, 1);

                            BigDecimal bigDecimal = new BigDecimal(Double.toString((value.min + (value.max - value.min) * i)));
                            bigDecimal = bigDecimal.setScale(2, 4);
                            value.set(bigDecimal.intValue());
                        } else if (!Mouse.isButtonDown(0) && sliderVal == v) {
                            sliderVal = null;
                        }

                        y1 += h;
                    } else if (v instanceof StringValue) {
                        StringValue val = (StringValue) v;
                        RenderUtil.rect(x, y1, x + width, y1 + h, settingsColor);
                        f.drawString(val.name + ": "+val.get() + (selectedTextField != null && selectedTextField == val && textFieldCounter > partialTicks % 0.5 ? "_" : ""), x + 5, y1 + (h / 2f - f.FONT_HEIGHT / 2f), Color.WHITE.getRGB());
                        if (i(mouseX, mouseY, x, y1, x + width, y1 + h) && (Mouse.isButtonDown(0)) && !wasPressed) {
                            this.selectedTextField = val;
                            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
                        }
                        y1 += h;
                    } else if (v instanceof BooleanValue) {
                        BooleanValue val = (BooleanValue) v;
                        RenderUtil.rect(x, y1, x + width, y1 + h, settingsColor);
                        RenderUtil.rect(x + width - 14, y1 + 2f, x + width - 3.2f, y1 + h - 2f, new Color(60, 60, 60));
                        if(val.get())
                            RenderUtil.rect(x + width - 12.5f, y1 + 3.5f, x + width - 4.5f, y1 + h - 3.5f, main_color);
                        f.drawString(val.name, x + 5, y1 + (h / 2f - f.FONT_HEIGHT / 2f), main_color.getRGB());
                        if (i(mouseX, mouseY, x, y1, x + width, y1 + h) && (Mouse.isButtonDown(0) || Mouse.isButtonDown(1)) && !wasPressed) {
                            val.next();
                            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
                        }
                        y1 += h;
                    }
                }
            }
        }
        textFieldCounter += 0.1;
        textFieldCounter %= 1;
        wasPressed = Mouse.isButtonDown(0) || Mouse.isButtonDown(1);
    }
    public float sigma(float v, float m, float mx){
        return v % m % mx;
    }
    StringValue selectedTextField = null;
    public void keyTyped(char charTyped, int keyCode){
        Keyboard.enableRepeatEvents(true);
        if(selectedTextField == null || selectedTextField.get() == null){
            if (keyCode == 1 && selectedTextField == null) {
                mc.displayGuiScreen(null);

                if (mc.currentScreen == null) {
                    mc.setIngameFocus();
                }
            }
            return;
        }
        String fieldText = selectedTextField.get();
        if(keyCode == 14) {
            selectedTextField.set(fieldText.substring(0, fieldText.length() > 0 ? fieldText.length() - 1 : 0));
        }else if(keyCode == 28 || keyCode == Keyboard.KEY_ESCAPE){
            selectedTextField = null;
        }else if(!Character.isISOControl(charTyped)){
            selectedTextField.set(fieldText + charTyped);
        }
    }
    public boolean i(int mouseX, int mouseY, float x, float y, float x2, float y2){
        return mouseX >= x && mouseY >= y && mouseX <= x2 && mouseY <= y2;
    }
}
