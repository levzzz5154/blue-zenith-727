package cat.module.modules.render;

import cat.BlueZenith;
import cat.events.impl.Render2DEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.types.*;
import cat.util.ColorUtil;
import cat.util.MathUtil;
import cat.util.RenderUtil;
import cat.util.font.sigma.FontUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import com.google.common.eventbus.Subscribe;
import java.awt.*;
import java.util.ArrayList;

// god i hate this check
@SuppressWarnings("all")
public class HUD extends Module {
    private final ModeValue fontMode = new ModeValue("Font", "Client", true, null, "Client", "Vanilla");
    private final StringValue clientName = new StringValue("Client name", BlueZenith.name, true, null);
    private final BooleanValue notifBlur = new BooleanValue("Notification blur", true, true, null);
    private final BooleanValue shadow = new BooleanValue("Text shadow", true, true, null);
    private final BooleanValue border = new BooleanValue("Border", true, true, null);
    private final IntegerValue backgroundOpacity = new IntegerValue("Background opacity", 50, 0, 255, 1, true, null);
    private final IntegerValue margin = new IntegerValue( "Margin", 10, 0, 15, 1, true, null);
    private final ModeValue colorMode = new ModeValue("Color mode", "Pulse", true, null, "Pulse", "Rainbow", "Custom");
    private final FloatValue rainbowMulti = new FloatValue("Rainbow multiplier", 0.4f,0,1,0.1f, true, the -> colorMode.get().equals("Rainbow"));
    private final IntegerValue customR = new IntegerValue("Red", 0, 0, 255, 1, true, __ -> colorMode.get().equals("Custom"));
    private final IntegerValue customG = new IntegerValue("Blue", 60, 0, 255, 1, true, __ -> colorMode.get().equals("Custom"));
    private final IntegerValue customB = new IntegerValue("Green", 159, 0, 255, 1, true, __ -> colorMode.get().equals("Custom"));
    private final BooleanValue showPing = new BooleanValue("Ping", true, true, null);
    private final BooleanValue showCoords = new BooleanValue("Coords", true, true, null);
    private final BooleanValue showFPS = new BooleanValue("FPS", true, true, null);
    private final BooleanValue showBPS = new BooleanValue("BPS", true, true, null);
    private final ArrayList<Module> modules = new ArrayList<>();
    public HUD() {
        super("HUD", "", ModuleCategory.RENDER);
        this.setState(true);
    }
    @Subscribe
    public void onRender2D(Render2DEvent e) {
        if(mc.gameSettings.showDebugInfo) return;
        for (Module m : BlueZenith.moduleManager.getModules()) {
            if (m.getState() && !modules.contains(m)) {
                modules.add(m);
            } else if (!m.getState() && m.arrayAnim.hasReachedMin()) {
                modules.remove(m);
            }
        }

        ScaledResolution sc = e.resolution;
        // checkmate natasha
        FontRenderer font = getFont();
        modules.sort((m, m1) -> Float.compare(font.getStringWidth(m1.getTagName()), font.getStringWidth(m.getTagName())));
        String str = clientName.get() + " " + BlueZenith.version;
        char[] strArr = str.toCharArray();
        float x1 = 5;
        for (int i = 0; i < strArr.length; i++) {
            Color c = getColor(i);
            font.drawString(String.valueOf(strArr[i]), x1, 5, c.getRGB(), shadow.get());
            x1 += font.getStringWidth(String.valueOf(strArr[i]));
        }

        drawInfo(e.resolution);

        float mar = margin.get();
        float increment = font.FONT_HEIGHT + (mar / 2f);
        //if(fontMode.is("Vanilla")) {
            //renderer.doRender(e, modules, e.resolution, mar, border.get(), backgroundOpacity.get(), shadow.get());
            //return;
        //}
        float y = 0;
        for (int i = 0; i < modules.size(); i++) {
            Module m = modules.get(i);
            Module d = modules.get(i > 0 ? i - 1 : i);
            if(m.hidden) continue;
            Color c = getColor(i);

            m.arrayAnim.setReversed(!m.getState()).setMax(font.getStringWidthF(m.getTagName()));
            m.arrayAnim.update();
            float value = m.arrayAnim.getValue();
            RenderUtil.rect(sc.getScaledWidth() - value - mar, y, sc.getScaledWidth(), y + increment, new Color(0,0,0,backgroundOpacity.get()).getRGB());
            if(border.get()){
                RenderUtil.rect(sc.getScaledWidth() - value - mar - 1, y, sc.getScaledWidth() - value - mar, y + increment, c.getRGB());
                RenderUtil.rect(sc.getScaledWidth() - font.getStringWidthF(d.getTagName()) - mar - 1, y - 1, sc.getScaledWidth() - value - mar, y, c.getRGB());
                if(m == modules.get(modules.size() - 1)){
                    RenderUtil.rect(sc.getScaledWidth() - value - mar - 1, y + increment - 1, sc.getScaledWidth(), y + increment, c.getRGB());
                }
            }
            font.drawString(m.getTagName(), sc.getScaledWidth() - value - (mar / 2f), y + (increment / 2f - font.FONT_HEIGHT / 2f), c.getRGB(), shadow.get());
            y += increment;
        }
        GlStateManager.resetColor();
    }
    // kinda by fxy, i just made it less cancerous
    private void drawInfo(ScaledResolution sr){
        FontRenderer f = getFont();
        final float fy = f.FONT_HEIGHT + 2;
        float y = mc.currentScreen != null && mc.currentScreen instanceof GuiChat ? mc.fontRendererObj.FONT_HEIGHT + 4 + fy : fy;
        int z = 0;
        if(showCoords.get()){
            String displayString = "XYZ§r: "+Math.round(mc.thePlayer.posX) + ", " + Math.round(mc.thePlayer.posY) + ", " + Math.round(mc.thePlayer.posZ);
            Color colorD = getColor(z);
            f.drawString(displayString, sr.getScaledWidth() - f.getStringWidthF(displayString) - 2, sr.getScaledHeight() - y, colorD.getRGB(), shadow.get());
            y += fy;
            z++;
        }
        if(!mc.isSingleplayer() && this.showPing.get()){
            String displayString = "Ping§r: " + mc.getCurrentServerData().pingToServer + "ms";
            Color colorD = getColor(z);
            f.drawString(displayString, sr.getScaledWidth() - f.getStringWidthF(displayString) - 2, sr.getScaledHeight() - y, colorD.getRGB(), shadow.get());
            y += fy;
            z++;
        }
        if(showFPS.get()){
            String displayString = "FPS§r: " + Minecraft.getDebugFPS();
            Color colorD = getColor(z);
            f.drawString(displayString, sr.getScaledWidth() - f.getStringWidthF(displayString) - 2, sr.getScaledHeight() - y, colorD.getRGB(), shadow.get());
            y += fy;
            z++;
        }
        if(showBPS.get()){
            String displayString = "Blocks/sec§r: " + MathUtil.round(getBPS(), 2);
            Color colorD = getColor(z);
            f.drawString(displayString, sr.getScaledWidth() - f.getStringWidthF(displayString) - 2, sr.getScaledHeight() - y, colorD.getRGB(), shadow.get());
        }
    }
    private Color getColor(int i){
        switch (colorMode.get()){
            case "Rainbow":
                return ColorUtil.rainbow(i, rainbowMulti.get());
            case "Pulse":
                return ColorUtil.getEpicColor(i);
            case "Custom":
                return new Color(customR.get(),customG.get(),customB.get());
            default:
                return new Color(255,255,255);
        }
    }
    private double getBPS() {
        if (mc.thePlayer == null || mc.thePlayer.ticksExisted < 1) {
            return 0;
        }
        return mc.thePlayer.getDistance(mc.thePlayer.lastTickPosX, mc.thePlayer.lastTickPosY, mc.thePlayer.lastTickPosZ) * (Minecraft.getMinecraft().timer.ticksPerSecond * Minecraft.getMinecraft().timer.timerSpeed);
    }
    private FontRenderer getFont(){
        return fontMode.get().equals("Client") ? FontUtil.fontSFLight42 : mc.fontRendererObj;
    }
}
