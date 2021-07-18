package cat.ui.clickgui;

import cat.BlueZenith;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.modules.render.ClickGUI;
import cat.ui.clickgui.components.Panel;
import cat.util.EntityManager;
import cat.util.RenderUtil;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class ClickGui extends GuiScreen {
    ArrayList<Panel> panels = new ArrayList<>();
    public ClickGui(){
        float x = 20;
        for (ModuleCategory v : ModuleCategory.values()) {
            Panel panel = new Panel(x, 20, v);
            for (Module m : BlueZenith.moduleManager.getModules()) {
                if(m.getCategory() == v){
                    panel.addModule(m);
                }
            }
            panels.add(panel.calculateWidth());
            x += panel.width + 6;
        }
    }
    Panel selectedPanel = null;
    public boolean mousePressed = false;
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        FontRenderer f = mc.fontRendererObj;
        ClickGUI click = (ClickGUI) BlueZenith.moduleManager.getModule(ClickGUI.class);
        for (Panel p : panels) {
            p.drawPanel(mouseX, mouseY, partialTicks);
            boolean n = i(mouseX, mouseY,p.x, p.y, p.x+p.width, p.y + f.FONT_HEIGHT + 16) && selectedPanel == null;
            if(Mouse.isButtonDown(0) && (n || selectedPanel == p)) {
                if (!mousePressed) {
                    p.prevX = (mouseX - p.x);
                    p.prevY = (mouseY - p.y);
                }
                selectedPanel = p;
                p.x = mouseX - p.prevX;
                p.y = mouseY - p.prevY;
            }
            else if(selectedPanel == p){
                selectedPanel = null;
            }
            GlStateManager.resetColor();
        }
        float w = 120;
        float h = 125;
        float i = f.FONT_HEIGHT + 16;
        RenderUtil.rect(this.width - w - 2, this.height - h - i, this.width, this.height - h, click.main_color);
        f.drawString("Targets", this.width - w - 2 + f.getStringWidth("Targets"), this.height - h - i + 7, Color.WHITE.getRGB());
        float y = this.height - h;
        for (EntityManager.Targets c : EntityManager.Targets.values()) {
            RenderUtil.rect(this.width - w, y, this.width - 2, y + i, click.backgroundColor);
            f.drawString(c.displayName, this.width - w + 6, (y + i / 2f - f.FONT_HEIGHT / 2f), c.on ? Color.WHITE.getRGB() : Color.WHITE.darker().darker().getRGB());
            if(i(mouseX, mouseY, this.width - w, y, this.width - 2, y + i) && !mousePressed && Mouse.isButtonDown(0)){
                c.on = !c.on;
                mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
            }
            y += i;
        }
        mousePressed = Mouse.isButtonDown(0);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    public boolean i(int mouseX, int mouseY, float x, float y, float x2, float y2){
        return mouseX >= x && mouseY >= y && mouseX <= x2 && mouseY <= y2;
    }
    protected void keyTyped(char typedChar, int keyCode){
        for (Panel p : panels) {
            p.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if(mouseButton != 1) return;
        panels.forEach(p -> {
            if(i(mouseX, mouseY,p.x, p.y, p.x+p.width, p.y + mc.fontRendererObj.FONT_HEIGHT + 16)) {
                p.hidden = !p.hidden;
            }
        });

    }

    public void onGuiClosed(){
        BlueZenith.moduleManager.getModule(ClickGUI.class).setState(false);
    }
    public boolean doesGuiPauseGame(){
        return false;
    }
}