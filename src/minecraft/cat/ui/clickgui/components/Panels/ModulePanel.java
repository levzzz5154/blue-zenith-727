package cat.ui.clickgui.components.Panels;

import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.Value;
import cat.module.value.types.*;
import cat.ui.clickgui.components.Panel;
import cat.util.RenderUtil;
import cat.util.font.jello.FontUtil;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ModulePanel extends Panel {
    ModuleCategory category;
    ArrayList<Module> modules = new ArrayList<>();
    public ModulePanel(float x, float y, ModuleCategory category){
        super(x, y);
        this.category = category;
        f = FontUtil.fontSFLight35;
        mHeight = f.FONT_HEIGHT + 14;
    }
    public Panel calculateSize(){
        float pY = mHeight;
        width = f.getStringWidth(category.displayName) + 12;
        for (Module m : modules) {
            if(f.getStringWidth(m.getName()) + 12 > width){
                width = f.getStringWidth(m.getName()) + 12;
            }
            pY += mHeight;
        }
        width = Math.max(width, 120);
        height = pY;
        return this;
    }
    public void addModule(Module mod){
        this.modules.add(mod);
    }
    float textFieldCounter = 0;
    Value<?> sliderVal = null;
    private boolean wasPressed = false;
    public void drawPanel(int mouseX, int mouseY, float partialTicks, boolean handleClicks){
        Color main_color = click.main_color;
        Color backgroundColor = click.backgroundColor;

        RenderUtil.rect(x, y, x + width, y + mHeight, main_color);
        f.drawString(category.displayName, x + 4, y + mHeight / 2f - f.FONT_HEIGHT / 2f, Color.WHITE.getRGB());
        float y1 = y + mHeight;
        for (Module m : modules) {
            if(!this.showModules) continue;

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
            if(m.showSettings){
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
                        if(i(mouseX, mouseY, x, y1, x + width, y1 + h) && !wasPressed && handleClicks) {
                            if(Mouse.isButtonDown(0)) {
                                val.next();
                                toggleSound();
                            } else if(Mouse.isButtonDown(1)) {
                                val.previous();
                                toggleSound();
                            }
                        }
                        y1 += h;
                    }else if (v instanceof FloatValue) {
                        FloatValue value = (FloatValue) v;
                        final float a = x + w * (Math.max(value.min, Math.min(value.get(), value.max)) - value.min) / (value.max - value.min);
                        RenderUtil.rect(x, y1, x + width, y1 + h, settingsColor);
                        RenderUtil.rect(x, y1r, x + w, y2r, new Color(63, 65, 68));
                        RenderUtil.rect(x, y1r, a, y2r, main_color.darker().darker());
                        f.drawString(value.name + ": " + value.get(), x + 4, y1 + (h / 2f - f.FONT_HEIGHT / 2f), main_color.getRGB());

                        if (Mouse.isButtonDown(0) && handleClicks && ((i(mouseX, mouseY, x, y1r, x + w, y2r) && sliderVal == null) || sliderVal == v)) {
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

                        if (Mouse.isButtonDown(0) && handleClicks && ((i(mouseX, mouseY, x, y1r, x + w, y2r) && sliderVal == null) || sliderVal == v)) {
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
                        if (i(mouseX, mouseY, x, y1, x + width, y1 + h) && (Mouse.isButtonDown(0)) && !wasPressed && handleClicks) {
                            this.selectedTextField = val;
                            toggleSound();
                        }
                        y1 += h;
                    } else if (v instanceof BooleanValue) {
                        BooleanValue val = (BooleanValue) v;
                        RenderUtil.rect(x, y1, x + width, y1 + h, settingsColor);
                        RenderUtil.rect(x + width - 14, y1 + 2f, x + width - 3.2f, y1 + h - 2f, new Color(60, 60, 60));
                        if(val.get())
                            RenderUtil.rect(x + width - 12.5f, y1 + 3.5f, x + width - 4.5f, y1 + h - 3.5f, main_color);
                        f.drawString(val.name, x + 5, y1 + (h / 2f - f.FONT_HEIGHT / 2f), main_color.getRGB());
                        if (i(mouseX, mouseY, x, y1, x + width, y1 + h) && (Mouse.isButtonDown(0) || Mouse.isButtonDown(1)) && !wasPressed && handleClicks) {
                            val.next();
                            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
                        }
                        y1 += h;
                    }
                    else if(v instanceof ActionValue) {
                        RenderUtil.rect(x, y1, x + width, y1 + h, settingsColor);
                        f.drawString(v.name, x + width/2-f.getStringWidth(v.name)/2f, y1 + h / 2f - f.FONT_HEIGHT / 2f, main_color.getRGB());
                        if (i(mouseX, mouseY, x, y1, x + width, y1 + h) && (Mouse.isButtonDown(0) || Mouse.isButtonDown(1)) && !wasPressed && handleClicks) {
                            v.next();
                            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
                        }
                        y1 += h;
                    }
                }
            }
        }
        height = y1 - y;
        textFieldCounter += 0.1;
        textFieldCounter %= 1;
        wasPressed = Mouse.isButtonDown(0) || Mouse.isButtonDown(1);
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
}
