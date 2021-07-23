package cat.module.modules.render;

import cat.BlueZenith;
import cat.events.Subscriber;
import cat.events.impl.Render2DEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.types.BooleanValue;
import cat.module.value.types.FloatValue;
import cat.module.value.types.IntegerValue;
import cat.module.value.types.ModeValue;
import cat.ui.arraylist.IArraylistRenderer;
import cat.ui.arraylist.impl.VanillaFont;
import cat.util.ColorUtil;
import cat.util.MathUtil;
import cat.util.RenderUtil;
import cat.util.font.sigma.FontUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.optifine.util.MathUtils;

import java.awt.*;
import java.util.ArrayList;

import static cat.util.ColorUtil.rainbow;
// god i hate this check
@SuppressWarnings("SpellCheckingInspection")
public class HUD extends Module {
    ModeValue fontMode = new ModeValue("Font", "Client", true, null, "Client", "Vanilla");
    BooleanValue shadow = new BooleanValue("FontShadow", true, true, null);
    BooleanValue border = new BooleanValue("Border", true, true, null);
    IntegerValue backgroundOpacity = new IntegerValue("BackgroundOpacity", 50, 0, 255, 1, true, null);
    IntegerValue margin = new IntegerValue( "Margin", 10, 0, 15, 1, true, null);
    ModeValue colorMode = new ModeValue("ColorMode", "Pulse", true, null, "Pulse", "Rainbow", "Custom");
    FloatValue rainbowMulti = new FloatValue("RainbowMulti", 0.4f,0,1,0.1f, true, the -> colorMode.get().equals("Rainbow"));
    IntegerValue customR = new IntegerValue("CustomR", 0, 0, 255, 1, true, __ -> colorMode.get().equals("Custom"));
    IntegerValue customG = new IntegerValue("CustomB", 60, 0, 255, 1, true, __ -> colorMode.get().equals("Custom"));
    IntegerValue customB = new IntegerValue("CustomG", 159, 0, 255, 1, true, __ -> colorMode.get().equals("Custom"));
    BooleanValue showPing = new BooleanValue("Ping", true, true, null);
    BooleanValue showCoords = new BooleanValue("Coords", true, true, null);
    BooleanValue showFPS = new BooleanValue("FPS", true, true, null);
    BooleanValue showBPS = new BooleanValue("BPS", true, true, null);
    ArrayList<Module> modules = new ArrayList<>();
    private final IArraylistRenderer renderer = new VanillaFont();
    @SuppressWarnings("unused")
    public HUD() {
        super("HUD", "", ModuleCategory.RENDER);
        this.setState(true);
    }
    @Subscriber
    public void onRender2D(Render2DEvent e) {
        if(mc.gameSettings.showDebugInfo) return;
        for (Module m : BlueZenith.moduleManager.getModules()) {
            if (m.getState() && !modules.contains(m)) {
                modules.add(m);
            } else if (!m.getState()) {
                modules.remove(m);
            }
        }

        ScaledResolution sc = e.resolution;
        // checkmate natasha
        FontRenderer font = getFont();
        modules.sort((m, m1) -> Float.compare(font.getStringWidth(m1.getTagName()), font.getStringWidth(m.getTagName())));
        String str = BlueZenith.name+" b"+BlueZenith.version;
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
            RenderUtil.rect(sc.getScaledWidth() - font.getStringWidthF(m.getTagName()) - mar, y, sc.getScaledWidth(), y + increment, new Color(0,0,0,backgroundOpacity.get()).getRGB());
            if(border.get()){
                RenderUtil.rect(sc.getScaledWidth() - font.getStringWidthF(m.getTagName()) - mar - 1, y, sc.getScaledWidth() - font.getStringWidthF(m.getTagName()) - mar, y + increment, c.getRGB());
                RenderUtil.rect(sc.getScaledWidth() - font.getStringWidthF(d.getTagName()) - mar - 1, y - 1, sc.getScaledWidth() - font.getStringWidthF(m.getTagName()) - mar, y, c.getRGB());
                if(m == modules.get(modules.size() - 1)){
                    RenderUtil.rect(sc.getScaledWidth() - font.getStringWidthF(m.getTagName()) - mar - 1, y + increment - 1, sc.getScaledWidth(), y + increment, c.getRGB());
                }
            }
            font.drawString(m.getTagName(), sc.getScaledWidth() - font.getStringWidthF(m.getTagName()) - (mar / 2f), y + (increment / 2f - font.FONT_HEIGHT / 2f), c.getRGB(), shadow.get());
            y += increment;
        }
        GlStateManager.resetColor();
    }
    private void drawInfo(ScaledResolution sr){
        FontRenderer f = getFont();
        final float fy = f.FONT_HEIGHT + 2;
        float y = mc.currentScreen != null && mc.currentScreen instanceof GuiChat ? mc.fontRendererObj.FONT_HEIGHT + 2 + fy : fy;
        int z = 0;
        if(showCoords.get()){
            Color colorD = getColor(z);
            // i hate minecraft coloring
            /*float x = 2;
            String[] stA = new String[]{"X§r: "+Math.round(mc.thePlayer.posX), "Y§r: "+Math.round(mc.thePlayer.posY), "Z§r: "+Math.round(mc.thePlayer.posZ)};
            for (int i = stA.length - 1; i >= 0; i--) {
                String str = stA[i];
                f.drawString(str, x, sr.getScaledHeight() - y, colorD.getRGB(), shadow.get());
                x += f.getStringWidth(str + " ");
            }*/
            f.drawString("XYZ§r: "+Math.round(mc.thePlayer.posX) + ", " + Math.round(mc.thePlayer.posY) + ", " + Math.round(mc.thePlayer.posZ), 2, sr.getScaledHeight() - y, colorD.getRGB(), shadow.get());
            y += fy;
            z++;
        }
        if(!mc.isSingleplayer() && this.showPing.get()){
            Color colorD = getColor(z);
            f.drawString("Ping§r:" + mc.getCurrentServerData().pingToServer + "ms", 2, sr.getScaledHeight() - y, colorD.getRGB(), shadow.get());
            y += fy;
            z++;
        }
        if(showFPS.get()){
            Color colorD = getColor(z);
            f.drawString("FPS§r: " + Minecraft.getDebugFPS(), 2, sr.getScaledHeight() - y, colorD.getRGB(), shadow.get());
            y += fy;
            z++;
        }
        if(showBPS.get()){
            Color colorD = getColor(z);
            f.drawString("Blocks/sec§r:" + MathUtil.round(getBPS(), 2), 2, sr.getScaledHeight() - y, colorD.getRGB(), shadow.get());
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
