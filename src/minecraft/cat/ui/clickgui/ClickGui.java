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
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.ArrayList;

import static cat.util.EntityManager.Targets.*;

public class ClickGui extends GuiScreen {
    Panel combat;
    Panel misc;
    Panel movement;
    Panel render;
    Panel[] panels;
    public ClickGui(){
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
        float x = 20;
        this.combat = new Panel(x, 20, ModuleCategory.COMBAT, combat.toArray(new Module[0]));
        x += this.combat.width + 6;
        this.misc = new Panel(x, 20, ModuleCategory.MISC, misc.toArray(new Module[0]));
        x += this.misc.width + 6;
        this.movement = new Panel(x, 20, ModuleCategory.MOVEMENT, movement.toArray(new Module[0]));
        x += this.movement.width + 6;
        this.render = new Panel(x, 20,ModuleCategory.RENDER, render.toArray(new Module[0]));
        panels = new Panel[]{this.combat, this.misc, this.movement, this.render};
    }
    Panel selectedPanel = null;
    public boolean mousePressed = false;
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        FontRenderer f = mc.fontRendererObj;
        ClickGUI click = (ClickGUI) BlueZenith.moduleManager.getModule(ClickGUI.class);
        for (Panel p : panels) {
            p.drawPanel(mouseX, mouseY);
            boolean n = i(mouseX, mouseY,p.x, p.y, p.x+p.width, p.y + f.FONT_HEIGHT + 16) && selectedPanel == null;
            if(Mouse.isButtonDown(0) && (n || selectedPanel == p)){
                if(!mousePressed){
                    p.prevX = (mouseX - p.x);
                    p.prevY = (mouseY - p.y);
                }
                selectedPanel = p;
                p.x = mouseX - p.prevX;
                p.y = mouseY - p.prevY;
            }else if(selectedPanel == p){
                selectedPanel = null;
            }
        }
        float w = 120;
        float h = 125;
        float i = f.FONT_HEIGHT + 16;
        RenderUtil.rect(this.width - w - 2, this.height - h - i, this.width, this.height - h, click.main_color);
        f.drawString("Targets", this.width - (w / 2f) + f.getStringWidth("Targets"), this.height - (i / 2f - f.FONT_HEIGHT / 2f), Color.WHITE.getRGB());
        EntityManager.Targets[] sex = new EntityManager.Targets[]{
                MOBS,
                PLAYERS,
                ANIMALS,
                INVISIBLE,
                DEAD};
        float y = this.height - h;
        for (EntityManager.Targets c : sex) {
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
    public void onGuiClosed(){
        BlueZenith.moduleManager.getModule(ClickGUI.class).setState(false);
    }
    public boolean doesGuiPauseGame(){
        return false;
    }
}