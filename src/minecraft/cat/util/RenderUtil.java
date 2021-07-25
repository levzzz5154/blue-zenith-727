package cat.util;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtil extends MinecraftInstance {
    public static int delta = 0;
    public static void drawImage(ResourceLocation image, float x, float y, float width, float height, float alpha) {
        GlStateManager.pushMatrix();
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glDepthMask(false);
        OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);
        glColor4f(1.0F, 1.0F, 1.0F, alpha);
        mc.getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);
        glDepthMask(true);
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
        GlStateManager.popMatrix();
    }
    public static float animate(float target, float current, float speed) {
        boolean larger = (target > current);
        speed = range(speed * 10 / delta, 0, 1);
        float dif = Math.max(target, current) - Math.min(target, current);
        float factor = dif * speed;
        if (factor < 0f)
            factor = 0f;
        if (larger) {
            current += factor;
        } else {
            current -= factor;
        }
        return current;
    }
    public static float range(float v, float min, float max){
        return Math.max(Math.min(v, max), min);
    }
    public static void rect(final float x, final float y, final float x2, final float y2, final int color) {
        Gui.drawRect(x,y,x2,y2, color);
        GlStateManager.resetColor();
    }
    public static void rect(final float x, final float y, final float x2, final float y2, final Color color) {
        Gui.drawRect(x,y,x2,y2,color.getRGB());
        GlStateManager.resetColor();
    }
    public static void rect(final double x, final double y, final double x2, final double y2, final Color color) {
        Gui.drawRect(x,y,x2,y2,color.getRGB());
        GlStateManager.resetColor();
    }
    public static void crop(final float x, final float y, final float x2, final float y2) {
        final ScaledResolution scaledResolution = new ScaledResolution(mc);
        final int factor = scaledResolution.getScaleFactor();
        glScissor((int) (x * factor), (int) ((scaledResolution.getScaledHeight() - y2) * factor), (int) ((x2 - x) * factor), (int) ((y2 - y) * factor));
    }
    private static int lastScale;
    private static int lastScaleWidth;
    private static int lastScaleHeight;
    private static Framebuffer buffer;
    private static final ResourceLocation shader = new ResourceLocation("cat/blur.json");
    private static ShaderGroup blurShader;
    public static void initFboAndShader() {
        try {
            blurShader = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), shader);
            blurShader.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
            buffer = blurShader.mainFramebuffer;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Framebuffer blur(float x, float y, float x2, float y2, ScaledResolution sc) {
        int factor = sc.getScaleFactor();
        int factor2 = sc.getScaledWidth();
        int factor3 = sc.getScaledHeight();
        if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3 || buffer == null
                || blurShader == null) {
            initFboAndShader();
        }
        lastScale = factor;
        lastScaleWidth = factor2;
        lastScaleHeight = factor3;
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        crop(x, y, x2, y2);
        buffer.framebufferHeight = mc.displayHeight;
        buffer.framebufferWidth = mc.displayWidth;
        GlStateManager.resetColor();
        blurShader.loadShaderGroup(mc.timer.renderPartialTicks);
        buffer.bindFramebuffer(true);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        return buffer;
    }
    public static void glColor(final Color color) {
        final float red = color.getRed() / 255F;
        final float green = color.getGreen() / 255F;
        final float blue = color.getBlue() / 255F;
        final float alpha = color.getAlpha() / 255F;

        GlStateManager.color(red, green, blue, alpha);
    }
    private static void glColor(final int hex) {
        final float alpha = (hex >> 24 & 0xFF) / 255F;
        final float red = (hex >> 16 & 0xFF) / 255F;
        final float green = (hex >> 8 & 0xFF) / 255F;
        final float blue = (hex & 0xFF) / 255F;

        GlStateManager.color(red, green, blue, alpha);
    }
}
