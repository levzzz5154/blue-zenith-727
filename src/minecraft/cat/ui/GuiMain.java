package cat.ui;

import cat.BlueZenith;
import cat.ui.alt.GuiAltLogin;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.IOException;

public class GuiMain extends GuiScreen {
    public void initGui(){
        int j = this.height / 3 + 48;
        this.buttonList.clear();

        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, j + 72 + 12, 98, 20, I18n.format("menu.options")));
        this.buttonList.add(new GuiButton(4, this.width / 2 + 2, j + 72 + 12, 98, 20, I18n.format("menu.quit")));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, j, I18n.format("menu.singleplayer")));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, j + 24, I18n.format("menu.multiplayer")));
        this.buttonList.add(new GuiButton(14, this.width / 2 - 100, j + 24 * 2, "Alt Manager"));
    }
    ResourceLocation bg = new ResourceLocation("cat/ui/bluezenith.jpg");
    String hamburger = "BlueZenith";
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        FontRenderer f = mc.fontRendererObj;
        ScaledResolution sc = new ScaledResolution(mc);
        //RenderUtil.drawImage(bg, 0, 0, this.width, this.height, 1);
        drawGradientRect(0, 0, this.width, this.height, new Color(0, 0, 69).getRGB(),BlueZenith.getMainColor().getRGB());
        drawGradientRect(0,0, this.width, this.height, new Color(0, 0, 69).getRGB(), BlueZenith.getEpicColor(10).getRGB());
        //drawGradientRect(0,0, this.width, this.height, new Color(0,0,0, 80).getRGB(), new Color(0,0,255,80).getRGB());
        GlStateManager.pushMatrix();
        float j = this.height / 3.5f + 48;
        char[] gd = hamburger.toCharArray();
        float v = 0;
        int n = 0;
        Color Z = BlueZenith.getEpicColor(n);
        float scale = 4.5f;
        //GlStateManager.translate(sc.getScaledWidth() / 2f - (f.getStringWidth(hamburger) * scale / 2f), j - 48 - f.FONT_HEIGHT, 1);
        GlStateManager.scale(scale, scale, 1);
        f.drawString(hamburger, sc.getScaledWidth() / 11f - (f.getStringWidth(hamburger) / 11f) + v, j - 150 - f.FONT_HEIGHT, Z.getRGB());
        for (char c : gd) {
            v += f.getStringWidth(String.valueOf(c));
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
            case 14:
                this.mc.displayGuiScreen(new GuiAltLogin(this));
                break;
        }
    }
}
