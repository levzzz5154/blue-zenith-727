package cat.ui.clickgui.components.Panels;

import cat.client.ConfigManager;
import cat.module.value.types.StringValue;
import cat.ui.clickgui.components.Panel;
import cat.util.ClientUtils;
import cat.util.FileUtil;
import cat.util.RenderUtil;
import cat.util.font.jello.FontUtil;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class ConfigsPanel extends Panel {
    private final StringValue val = new StringValue("Name", "", true, (a1, a2) -> {
       if(a2.length() > 25) return a1;
       else return a2;
    }, null);

    private StringValue selectedTextField = null;
    private float textFieldCounter = 0f;
    private String selectedConfig;
    private boolean closed = false;
    ArrayList<File> files = new ArrayList<>();
    public ConfigsPanel(float x, float y) {
        super(x, y);
        f = FontUtil.fontSFLight35;
        mHeight = f.FONT_HEIGHT + 14;
        width = 120;
    }
    public void update(){
        textFieldCounter = 0f;
        files.clear();
        File directory = new File(FileUtil.configFolder);
        if(directory.exists() && directory.isDirectory()){
            for (File f : Objects.requireNonNull(directory.listFiles())) {
                if(f != null && f.exists() && f.isFile() && FilenameUtils.getExtension(f.getName()).equalsIgnoreCase("json")){
                    this.files.add(f);
                }
            }
        }
    }
    boolean sex, hot = false;
    public void drawPanel(int mouseX, int mouseY, float partialTicks, boolean handleClicks) {
        if(!Mouse.isButtonDown(0)) sex = false;
        if(!Mouse.isButtonDown(1)) hot = false;
        textFieldCounter += 0.1f;
        Color main_color = click.main_color;
        Color backgroundColor = click.backgroundColor;

        RenderUtil.rect(x, y, x + width, y + mHeight, main_color);
        if(Mouse.isButtonDown(1) && !hot && i(mouseX, mouseY, x, y, x+width, y+mHeight)) {
            closed = !closed;
            hot = true;
        }
        f.drawString("Configs", x + 4, y + mHeight / 2f - f.FONT_HEIGHT / 2f, Color.WHITE.getRGB());
        if(closed) return;
        float y = this.y + mHeight;
        for (File file : files) {
            String no = FilenameUtils.removeExtension(file.getName());
            RenderUtil.rect(x, y, x + width, y + mHeight, backgroundColor);
            f.drawString(no, x + width/2-f.getStringWidth(no)/2f, y + (mHeight / 2f - f.FONT_HEIGHT / 2f), selectedConfig != null && selectedConfig.equals(no) ? main_color.getRGB() : main_color.darker().darker().getRGB());
            if(i(mouseX, mouseY, x, y, x + width, y + mHeight) && !sex && Mouse.isButtonDown(0) && handleClicks){
                sex = true;
                selectedConfig = no;
                //ConfigManager.load(no, Keyboard.isKeyDown(Keyboard.KEY_LSHIFT), (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && Keyboard.isKeyDown(Keyboard.KEY_X)) || Keyboard.isKeyDown(Keyboard.KEY_X));
                toggleSound();
            }
            y += mHeight;
        }
        RenderUtil.rect(x, y, x + width, y + 20, backgroundColor);
        boolean blank = StringUtils.isBlank(val.get());
        boolean selected = this.selectedTextField == val;
        f.drawString(blank ? val.name + (selected ? "_" : "..") : val.get() + (selectedTextField != null && selectedTextField == val && textFieldCounter > partialTicks % 0.5 ? "_" : ""), blank ? x + width/2-f.getStringWidth(val.name)/2f : x + width/2 - f.getStringWidth(val.get())/2f, y + (20 / 2f - f.FONT_HEIGHT / 2f), blank ? Color.GRAY.getRGB() : Color.WHITE.getRGB());
        if (Mouse.isButtonDown(0) && i(mouseX, mouseY, x, y, x + width, y + 19) && handleClicks && !sex) {
            sex = true;
            this.selectedTextField = val;
            toggleSound();
        } else if(Mouse.isButtonDown(0) && !i(mouseX, mouseY, x, y, x + width, y + 10) && handleClicks && !sex) {
            this.selectedTextField = null;
        }
        y += 10;

        RenderUtil.rect(x,  y + 10, x + width, y + 30, backgroundColor);
        f.drawString("Save", x + width/2 - f.getStringWidth("Save")/2f, y + 10 + (mHeight / 2f - f.FONT_HEIGHT / 2f), main_color.getRGB());
        if(Mouse.isButtonDown(0) && i(mouseX, mouseY, x, y  + 5, x + width, y + 30) && handleClicks && !sex) {
            update();
            sex = true;
            if(StringUtils.isBlank(val.get())) {
                if(selectedConfig != null) {
                    ConfigManager.save(selectedConfig);
                    update();
                    val.set("");
                    selectedConfig = null;
                } else
                ClientUtils.fancyMessage("Specify a config name to proceed.");
            } else {
                ConfigManager.save(val.get());
                update();
                val.set("");
            }
        }
        y += 10;
        RenderUtil.rect(x,  y + 20, x + width, y + 40, backgroundColor);
        f.drawString("Delete", x + width/2 - f.getStringWidth("Delete")/2f, y + 20 + (mHeight / 2f - f.FONT_HEIGHT / 2f), main_color.getRGB());
        if(Mouse.isButtonDown(0) && i(mouseX, mouseY, x, y  + 20, x + width, y + 40) && handleClicks && !sex) {
            sex = true;
            if(selectedConfig != null) {
                boolean a = new File(FileUtil.configFolder + File.separator + selectedConfig + ".json").delete();
                ClientUtils.fancyMessage("Deleted config " + selectedConfig);
                selectedConfig = null;
                update();
            } else ClientUtils.fancyMessage("Select a config to delete!");
            val.set("");
        }
        y += 10;
        RenderUtil.rect(x,  y + 30, x + width, y + 50, backgroundColor);
        f.drawString("Load", x + width/2 - f.getStringWidth("Load")/2f, y + 30 + (mHeight / 2f - f.FONT_HEIGHT / 2f), main_color.getRGB());
        if(Mouse.isButtonDown(0) && i(mouseX, mouseY, x, y  + 30, x + width, y + 50) && handleClicks && !sex) {
            sex = true;
            if(selectedConfig != null) {
                ConfigManager.load(selectedConfig, Keyboard.isKeyDown(Keyboard.KEY_LSHIFT), (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && Keyboard.isKeyDown(Keyboard.KEY_X)) || Keyboard.isKeyDown(Keyboard.KEY_X));
                selectedConfig = null;
            } else ClientUtils.fancyMessage("Select a config to load!");
            val.set("");
        }
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
