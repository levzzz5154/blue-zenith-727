package cat.ui;

import cat.BlueZenith;
import cat.util.RenderUtil;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.IOException;

import static cat.module.modules.render.HUD.hi;

public class GuiMain extends GuiScreen {
    public void initGui(){
        int j = this.height / 3 + 48;
        this.buttonList.clear();

        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, j + 72 + 12, 98, 20, I18n.format("menu.options")));
        this.buttonList.add(new GuiButton(4, this.width / 2 + 2, j + 72 + 12, 98, 20, I18n.format("menu.quit")));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, j, I18n.format("menu.singleplayer")));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, j + 24, I18n.format("menu.multiplayer")));
    }
    ResourceLocation bg = new ResourceLocation("cat/ui/bluezenith.jpg");
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        ScaledResolution sc = new ScaledResolution(mc);
        RenderUtil.drawImage(bg, 0, 0, this.width, this.height, 1);
        GlStateManager.pushMatrix();
        FontRenderer font = mc.fontRendererObj;
        float scale = 5;
        float j = this.height / 3.5f + 48;
        GlStateManager.translate(sc.getScaledWidth() / 2f - (font.getStringWidth("BlueZenith") * scale / 2f), j - 48 - font.FONT_HEIGHT, 1);
        GlStateManager.scale(scale,scale,1);
        char[] gd = BlueZenith.name.toCharArray();
        float v = 0;
        int n = 0;
        for (char c : gd) {
            Color Z = hi(new Color(0, 60, 60), new Color(0, 140, 160), Math.abs(System.currentTimeMillis() / 10L) / 100.0 + 6.0F * (n + 2.55) / 60);
            font.drawString(String.valueOf(c), v, 0, Z.getRGB(), true);
            v += font.getStringWidth(String.valueOf(c));
            n -= 1;
        }
        GlStateManager.popMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id){
            case 0:
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;
            case 5:
                this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
                break;
            case 1:
                this.mc.displayGuiScreen(new GuiSelectWorld(this));
                break;
            case 2:
                this.mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            case 4:
                this.mc.shutdown();
                break;
        }
    }
}
