package cat.ui.clickgui.components.Panels;

import cat.client.ConfigManager;
import cat.ui.clickgui.components.Panel;
import cat.util.FileUtil;
import cat.util.RenderUtil;
import cat.util.lmao.FontUtil;
import org.apache.commons.io.FilenameUtils;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class ConfigsPanel extends Panel {
    ArrayList<File> files = new ArrayList<>();
    public ConfigsPanel(float x, float y) {
        super(x, y);
        f = FontUtil.fontSFLight35;
        mHeight = f.FONT_HEIGHT + 14;
        width = 120;
    }
    public void update(){
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
    boolean sex = false;
    public void drawPanel(int mouseX, int mouseY, float partialTicks, boolean handleClicks){
        Color main_color = click.main_color;
        Color backgroundColor = click.backgroundColor;

        RenderUtil.rect(x, y, x + width, y + mHeight, main_color);
        f.drawString("Configs", x + 4, y + mHeight / 2f - f.FONT_HEIGHT / 2f, Color.WHITE.getRGB());
        float y = this.y + mHeight;
        for (File file : files) {
            String no = FilenameUtils.removeExtension(file.getName());
            RenderUtil.rect(x, y, x + width, y + mHeight, backgroundColor);
            f.drawString(no, x + 5, y + (mHeight / 2f - f.FONT_HEIGHT / 2f), !ConfigManager.currentConfig.equals("") && ConfigManager.currentConfig.equals(no) ? main_color.getRGB() : main_color.darker().darker().getRGB());
            if(i(mouseX, mouseY, x, y, x + width, y + mHeight) && !sex && Mouse.isButtonDown(0) && handleClicks){
                ConfigManager.load(no, true, false);
                toggleSound();
            }
            y += mHeight;
        }
        sex = Mouse.isButtonDown(0);
    }
}
