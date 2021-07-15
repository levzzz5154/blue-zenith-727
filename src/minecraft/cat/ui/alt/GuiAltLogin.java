package cat.ui.alt;

import cat.util.RenderUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;

public final class GuiAltLogin extends GuiScreen {
    private final GuiScreen previousScreen;
    private AltLoginThread thread;
    private GuiTextField combined;
    private GuiTextField bruh;

    public GuiAltLogin(GuiScreen previousScreen) {
        this.previousScreen = previousScreen;
    }

    protected void actionPerformed(GuiButton button) {
        switch(button.id) {
            case 0:
                if (!this.combined.getText().isEmpty() && this.combined.getText().contains(":")) {
                    String u = this.combined.getText().split(":")[0];
                    String p = this.combined.getText().split(":")[1];
                    this.thread = new AltLoginThread(u.replaceAll(" ", ""), p.replaceAll(" ", ""));
                    this.thread.start();
                }
                if(this.combined.getText().isEmpty()) {
                    if(!bruh.getText().isEmpty()) {
                        String user = this.bruh.getText();
                        this.thread = new AltLoginThread(user);
                        this.thread.start();
                    }
                }
                break;
            case 1:
                this.mc.displayGuiScreen(previousScreen);
        }

    }

    public void drawScreen(int x, int y, float z) {
        //       this.drawDefaultBackground();
        RenderUtil.rect(0, 0, this.width, this.height, new Color(43, 43, 43, 255));
        this.combined.drawTextBox();
        this.drawCenteredString(this.mc.fontRendererObj, "Direct login for now", this.width / 2, 20, -1);
        this.drawCenteredString(this.mc.fontRendererObj, this.thread == null ? EnumChatFormatting.WHITE + "Idling" : this.thread.getStatus(), this.width / 2 - 5, 33, -1);
        if (this.combined.getText().isEmpty()) {
            this.drawString(this.mc.fontRendererObj, "Email : Password", this.width / 2 - 96, 56, -5592406);
        }
        this.bruh.drawTextBox();
        if(this.bruh.getText().isEmpty()) {
            this.drawString(this.fontRendererObj, "Username", this.width / 2 - 96, 86, -5592406);
       //     FontUtil.jelloFontAddAlt3.drawString("Username", this.width / 2 - 96, 86, -5592406);
        }
        mc.fontRendererObj.drawString("Current Name: "+mc.session.getUsername(), 10, 10, Color.WHITE.getRGB());
        super.drawScreen(x, y, z);
    }

    public void initGui() {
        int var3 = this.height / 4 + 24;
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, var3 + 20, "Login"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, var3 + 20 + 24, "Back"));
        this.combined = new GuiTextField(var3, this.mc.fontRendererObj, this.width / 2 - 100, 50, 200, 20);
        this.bruh = new GuiTextField(var3, this.mc.fontRendererObj, this.width /2 - 100, 80, 200, 20);
        this.combined.setMaxStringLength(200);
        this.bruh.setMaxStringLength(16);
        Keyboard.enableRepeatEvents(true);
    }

    protected void keyTyped(char character, int key) {
        try {
            super.keyTyped(character, key);
        } catch (IOException var4) {
            var4.printStackTrace();
        }

        if (character == '\r') {
            this.actionPerformed((GuiButton)this.buttonList.get(0));
        }

        this.combined.textboxKeyTyped(character, key);
        this.bruh.textboxKeyTyped(character, key);
    }

    protected void mouseClicked(int x, int y, int button) {
        try {
            super.mouseClicked(x, y, button);
        } catch (IOException var5) {
            var5.printStackTrace();
        }
        this.bruh.mouseClicked(x,y,button);
        this.combined.mouseClicked(x, y, button);
    }

    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    public void updateScreen() {
        this.combined.updateCursorCounter();
    }
}