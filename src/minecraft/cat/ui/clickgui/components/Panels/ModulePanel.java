package cat.ui.clickgui.components.Panels;

import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.Value;
import cat.module.value.types.*;
import cat.ui.clickgui.components.Panel;
import cat.util.MillisTimer;
import cat.util.RenderUtil;
 import cat.util.font.sigma.FontUtil;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModulePanel extends Panel {
    private final ArrayList<Module> modules = new ArrayList<>();
    private final MillisTimer timer = new MillisTimer();
    private final ModuleCategory category;
    private StringValue selectedTextField = null;
    private float textFieldCounter = 0;
    private boolean wasPressed = false;
    private Value<?> sliderVal = null;
    private Module lastMod = null;
    public ModulePanel(float x, float y, ModuleCategory category){
        super(x, y, "Modules " + category.displayName);
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
    public ArrayList<Module> getModules(){
        return modules;
    }
    public void addModule(Module mod){
        this.modules.add(mod);
        mod.clickGuiAnim.setReversed(true);
    }
    public void drawPanel(int mouseX, int mouseY, float partialTicks, boolean handleClicks){
        float w = width;
        float h = f.FONT_HEIGHT + 8;
        Color mainColor = click.main_color;
        Color backgroundColor = click.backgroundColor;
        Color settingsColor = backgroundColor.brighter();

        RenderUtil.rect(x, y, x + width, y + mHeight, new Color(mainColor.getRed(), mainColor.getGreen(), mainColor.getBlue(), click.ba.get()));
        f.drawString(category.displayName, x + 4, y + mHeight / 2f - f.FONT_HEIGHT / 2f, Color.WHITE.getRGB());
        float y1 = y + mHeight;
        for (Module m : modules) {
            if(!this.showContent) continue;

            List<Value<?>> vl = m.getValues().stream().filter(Value::isVisible).collect(Collectors.toList());
            if(i(mouseX, mouseY, x, y1, x + width, y1 + mHeight) && !wasPressed){
                if(Mouse.isButtonDown(0)){
                    m.toggle();
                    toggleSound();
                }
                if(Mouse.isButtonDown(1) && !vl.isEmpty()){
                    if(lastMod != null && lastMod != m&& click.closePrevious.get()) lastMod.showSettings = false;
                    lastMod = m;
                    m.showSettings = !m.showSettings;
                    m.clickGuiAnim.setReversed(!m.showSettings);
                    toggleSound();
                    timer.reset();
                }
            }

            RenderUtil.rect(x, y1, x + width, y1 + mHeight, backgroundColor);
            f.drawString(m.getName(), x + 5, y1 + (mHeight / 2f - f.FONT_HEIGHT / 2f), m.getState() ? mainColor.getRGB() : mainColor.darker().darker().getRGB());
            y1 += mHeight;
            if(canRender(m) && (timer.hasTimeReached(35) || !click.closePrevious.get())){
                float y2 = y1; // im sorry for too many variables :(
                if(click.animate.get())
                    m.clickGuiAnim.setMax(h * vl.size()).update();
                GL11.glEnable(GL11.GL_SCISSOR_TEST);
                RenderUtil.crop(x, y1, x + width, y1 + d(h * vl.size(), m.clickGuiAnim.getValue()) + 1);
                for (Value<?> v : vl) {
                    float y2r = y2 + h;
                    if(v instanceof FontValue){
                        FontValue val = (FontValue) v;
                        RenderUtil.rect(x, y2, x + width, y2 + h, settingsColor);
                        f.drawString(val.name, x + 5, y2 + h / 2f - f.FONT_HEIGHT / 2f, mainColor.getRGB());
                        f.drawString(val.get().getName(), x + 10 + f.getStringWidth(val.name), y2 + (h / 2f - f.FONT_HEIGHT / 2f), Color.GRAY.getRGB());
                        if(i(mouseX, mouseY, x, y2, x + width, y2 + h) && !wasPressed && handleClicks) {
                            if(Mouse.isButtonDown(0)) {
                                val.next();
                                toggleSound();
                            } else if(Mouse.isButtonDown(1)) {
                                val.previous();
                                toggleSound();
                            }
                        }
                        y2 += h;
                    } else if(v instanceof ModeValue) {
                        ModeValue val = (ModeValue) v;
                        RenderUtil.rect(x, y2, x + width, y2 + h, settingsColor);
                        f.drawString(val.name, x + 5, y2 + h / 2f - f.FONT_HEIGHT / 2f, mainColor.getRGB());
                        f.drawString(val.get(), x + 10 + f.getStringWidth(val.name), y2 + (h / 2f - f.FONT_HEIGHT / 2f), Color.GRAY.getRGB());
                        if(i(mouseX, mouseY, x, y2, x + width, y2 + h) && !wasPressed && handleClicks) {
                            if(Mouse.isButtonDown(0)) {
                                val.next();
                                toggleSound();
                            } else if(Mouse.isButtonDown(1)) {
                                val.previous();
                                toggleSound();
                            }
                        }
                        y2 += h;
                    }else if (v instanceof FloatValue) {
                        FloatValue value = (FloatValue) v;
                        final float a = x + w * (Math.max(value.min, Math.min(value.get(), value.max)) - value.min) / (value.max - value.min);
                        RenderUtil.rect(x, y2, x + width, y2 + h, settingsColor);
                        RenderUtil.rect(x, y2, x + w, y2r, new Color(63, 65, 68));
                        RenderUtil.rect(x, y2, a, y2r, mainColor.darker().darker());
                        f.drawString(value.name + ": " + value.get(), x + 4, y2 + (h / 2f - f.FONT_HEIGHT / 2f), mainColor.getRGB());

                        if (Mouse.isButtonDown(0) && handleClicks && ((i(mouseX, mouseY, x, y2, x + w, y2r) && sliderVal == null) || sliderVal == v)) {
                            sliderVal = v;
                            double i = MathHelper.clamp_double(((double) mouseX - (double) x) / ((double) w - 3), 0, 1);

                            BigDecimal bigDecimal = new BigDecimal(Double.toString((value.min + (value.max - value.min) * i)));
                            bigDecimal = bigDecimal.setScale(2, 4);
                            value.set(bigDecimal.floatValue());
                        } else if (!Mouse.isButtonDown(0) && sliderVal == v) {
                            sliderVal = null;
                        }

                        y2 += h;
                    } else if (v instanceof IntegerValue) {
                        IntegerValue value = (IntegerValue) v;
                        final float a = x + w * (Math.max(value.min, Math.min(value.get(), value.max)) - value.min) / (value.max - value.min);
                        RenderUtil.rect(x, y2, x + width, y2 + h, settingsColor);
                        RenderUtil.rect(x, y2, x + w, y2r, new Color(63, 65, 68));
                        RenderUtil.rect(x, y2, a, y2r, mainColor.darker().darker());
                        f.drawString(value.name + ": " + value.get(), x + 4, y2 + h / 2f - f.FONT_HEIGHT / 2f, mainColor.getRGB());

                        if (Mouse.isButtonDown(0) && handleClicks && ((i(mouseX, mouseY, x, y2, x + w, y2r) && sliderVal == null) || sliderVal == v)) {
                            sliderVal = v;
                            double i = MathHelper.clamp_double(((double) mouseX - (double) x) / ((double) w - 3), 0, 1);

                            BigDecimal bigDecimal = new BigDecimal(Double.toString((value.min + (value.max - value.min) * i)));
                            bigDecimal = bigDecimal.setScale(2, 4);
                            value.set(bigDecimal.intValue());
                        } else if (!Mouse.isButtonDown(0) && sliderVal == v) {
                            sliderVal = null;
                        }

                        y2 += h;
                    } else if (v instanceof StringValue) {
                        StringValue val = (StringValue) v;
                        RenderUtil.rect(x, y2, x + width, y2 + h, settingsColor);
                        f.drawString(val.name + ": "+val.get() + (selectedTextField != null && selectedTextField == val && textFieldCounter > partialTicks % 0.5 ? "_" : ""), x + 5, y2 + (h / 2f - f.FONT_HEIGHT / 2f), Color.WHITE.getRGB());
                        if (i(mouseX, mouseY, x, y2, x + width, y2 + h) && (Mouse.isButtonDown(0)) && !wasPressed && handleClicks) {
                            this.selectedTextField = val;
                            toggleSound();
                        }
                        y2 += h;
                    } else if (v instanceof BooleanValue) {
                        BooleanValue val = (BooleanValue) v;
                        RenderUtil.rect(x, y2, x + width, y2 + h, settingsColor);
                        RenderUtil.rect(x + width - 14, y2 + 2f, x + width - 3.2f, y2 + h - 2f, new Color(60, 60, 60));
                        if(val.get())
                            RenderUtil.rect(x + width - 12.5f, y2 + 3.5f, x + width - 4.5f, y2 + h - 3.5f, mainColor);
                        f.drawString(val.name, x + 5, y2 + (h / 2f - f.FONT_HEIGHT / 2f), mainColor.getRGB());
                        if (i(mouseX, mouseY, x, y2, x + width, y2 + h) && (Mouse.isButtonDown(0) || Mouse.isButtonDown(1)) && !wasPressed && handleClicks) {
                            val.next();
                            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
                        }
                        y2 += h;
                    }else if(v instanceof ActionValue) {
                        RenderUtil.rect(x, y2, x + width, y2 + h, settingsColor);
                        f.drawString(v.name, x + width/2-f.getStringWidth(v.name)/2f, y2 + h / 2f - f.FONT_HEIGHT / 2f, mainColor.getRGB());
                        if (i(mouseX, mouseY, x, y2, x + width, y2 + h) && (Mouse.isButtonDown(0) || Mouse.isButtonDown(1)) && !wasPressed && handleClicks) {
                            v.next();
                            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
                        }
                        y2 += h;
                    }
                }
                // i can't find any way of fixing this
                GL11.glDisable(GL11.GL_SCISSOR_TEST);
                y1 += d(h * vl.size(), m.clickGuiAnim.getValue());
            }
        }
        height = y1 - y;
        textFieldCounter += 0.1;
        textFieldCounter %= 1;
        wasPressed = Mouse.isButtonDown(0) || Mouse.isButtonDown(1);
    }
    private float d(float h, float value){
        return click.animate.get() ? value : h;
    }
    private boolean canRender(Module m){
        return click.animate.get() ? m.clickGuiAnim.isReversed() ? m.clickGuiAnim.getValue() != m.clickGuiAnim.getMin() : m.clickGuiAnim.getValue() <= m.clickGuiAnim.getMax() : m.showSettings;
    }
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
