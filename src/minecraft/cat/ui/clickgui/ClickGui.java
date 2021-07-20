package cat.ui.clickgui;

import cat.BlueZenith;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.modules.render.ClickGUI;
import cat.ui.clickgui.components.Panels.ConfigsPanel;
import cat.ui.clickgui.components.Panels.ModulePanel;
import cat.ui.clickgui.components.Panel;
import cat.ui.clickgui.components.Panels.TargetsPanel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;

public class ClickGui extends GuiScreen {
    ArrayList<Panel> panels = new ArrayList<>();
    ConfigsPanel configsPanel;
    public ClickGui(){
        float x = 20;
        float y = 20;
        float maxHeight = 0;
        for (ModuleCategory v : ModuleCategory.values()) {
            ModulePanel panel = new ModulePanel(x, y, v);
            for (Module m : BlueZenith.moduleManager.getModules()) {
                if(m.getCategory() == v){
                    panel.addModule(m);
                }
            }
            panels.add(panel.calculateSize());
            if(panel.height > maxHeight){
                maxHeight = panel.height;
            }
            if(x + panel.width + 6 > 504){
                x = 20;
                y += maxHeight + 6;
            }else{
                x += panel.width + 6;
            }
        }
        panels.add(new TargetsPanel(x, y));
        panels.add(configsPanel = new ConfigsPanel(x, y + maxHeight));
    }
    public void initGui(){
        configsPanel.update();
    }
    Panel selectedPanel = null;
    public boolean mousePressed = false;
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        for (Panel p : panels) {
            p.drawPanel(mouseX, mouseY, partialTicks, selectedPanel == null);
            boolean d = i(mouseX, mouseY, p.x, p.y, p.x + p.width, p.y + p.mHeight);
            if(Mouse.isButtonDown(0) && ((d && selectedPanel == null) || selectedPanel == p)){
                if(!mousePressed){
                    selectedPanel = p;
                    p.prevX = (mouseX - p.x);
                    p.prevY = (mouseY - p.y);
                }
                p.x = mouseX - p.prevX;
                p.y = mouseY - p.prevY;
            }else if(selectedPanel == p){
                selectedPanel = null;
            }
            if(d){
                if(!mousePressed && Mouse.isButtonDown(1)){
                    p.showContent = !p.showContent;
                    p.toggleSound();
                }
            }

            GlStateManager.resetColor();
        }
        mousePressed = Mouse.isButtonDown(0) || Mouse.isButtonDown(1);
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
    public ArrayList<Panel> getPanels(){
        return panels;
    }
    public Panel getPanel(String identifier) {
        return panels.stream().filter(p -> p.id.equalsIgnoreCase(identifier)).findFirst().orElse(null);
    }
    public void onGuiClosed(){
        BlueZenith.moduleManager.getModule(ClickGUI.class).setState(false);
    }
    public boolean doesGuiPauseGame(){
        return false;
    }
}