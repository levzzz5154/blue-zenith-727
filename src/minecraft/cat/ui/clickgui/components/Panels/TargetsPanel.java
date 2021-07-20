package cat.ui.clickgui.components.Panels;

import cat.ui.clickgui.components.Panel;
import cat.util.EntityManager;
import cat.util.RenderUtil;
import cat.util.font.jello.FontUtil;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class TargetsPanel extends Panel {
    private final ArrayList<EntityManager.Targets> targets = new ArrayList<>();
    private boolean closed = false;
    public TargetsPanel(float x, float y) {
        super(x, y);
        f = FontUtil.fontSFLight35;
        mHeight = f.FONT_HEIGHT + 14;
        targets.addAll(Arrays.asList(EntityManager.Targets.values()));
        width = 120;
    }
    boolean sex, cum = false;
    public void drawPanel(int mouseX, int mouseY, float partialTicks, boolean handleClicks){
        Color main_color = click.main_color;
        Color backgroundColor = click.backgroundColor;

        if(!sex && Mouse.isButtonDown(1) && i(mouseX, mouseY, x, y, x + width, y + mHeight)) {
            closed = !closed;
            sex = true;
        }
        sex = Mouse.isButtonDown(0) || Mouse.isButtonDown(1);
        RenderUtil.rect(x, y, x + width, y + mHeight, main_color);
        f.drawString("Targets", x + 4, y + mHeight / 2f - f.FONT_HEIGHT / 2f, Color.WHITE.getRGB());
        if(closed) return;
        float y = this.y + mHeight;
        for (EntityManager.Targets tar : targets) {
            RenderUtil.rect(x, y, x + width, y + mHeight, backgroundColor);
            f.drawString(tar.displayName, x + 5, y + (mHeight / 2f - f.FONT_HEIGHT / 2f), tar.on ? main_color.getRGB() : main_color.darker().darker().getRGB());
            if(i(mouseX, mouseY, x, y, x + width, y + mHeight) && !sex && Mouse.isButtonDown(0) && handleClicks){
                tar.on = !tar.on;
                toggleSound();
            }
            y += mHeight;
        }
        if(!Mouse.isButtonDown(1)) {
            cum = false;
        }
    }
}
