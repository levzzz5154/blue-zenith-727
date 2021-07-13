package cat.ui.clickgui;

import cat.BlueZenith;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.modules.render.ClickGUI;
import cat.ui.clickgui.components.Panel;
import net.minecraft.client.gui.GuiScreen;

import java.util.ArrayList;

public class ClickGui extends GuiScreen {
    Panel combat;
    Panel misc;
    Panel movement;
    Panel render;
    Panel[] panels;
    public void initGui(){
        ArrayList<Module> combat = new ArrayList<>();
        ArrayList<Module> misc = new ArrayList<>();
        ArrayList<Module> movement = new ArrayList<>();
        ArrayList<Module> render = new ArrayList<>();
        for (Module m : BlueZenith.moduleManager.getModules()) {
            switch (m.getCategory()){
                case COMBAT:
                    combat.add(m);
                    break;
                case MISC:
                    misc.add(m);
                    break;
                case RENDER:
                    render.add(m);
                    break;
                case MOVEMENT:
                    movement.add(m);
                    break;
            }
        }
        this.combat = new Panel(ModuleCategory.COMBAT, combat.toArray(new Module[0]));
        this.misc = new Panel(ModuleCategory.MISC, misc.toArray(new Module[0]));
        this.movement = new Panel(ModuleCategory.MOVEMENT, movement.toArray(new Module[0]));
        this.render = new Panel(ModuleCategory.RENDER, render.toArray(new Module[0]));
        panels = new Panel[]{this.combat, this.misc, this.movement, this.render};
    }
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        float x = 20;
        for (Panel p : panels) {
            float width = p.drawPanel(x,20,mouseX, mouseY);
            x += width + 6;
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    public void onGuiClosed(){
        BlueZenith.moduleManager.getModule(ClickGUI.class).setState(false);
    }
    public boolean doesGuiPauseGame(){
        return false;
    }
}