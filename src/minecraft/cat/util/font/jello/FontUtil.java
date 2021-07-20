package cat.util.font.jello;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.InputStream;

public class FontUtil {
	private static Font getEpicFont(String str, float size) {
		Font font;
		try {
			InputStream is = Minecraft.getMinecraft().getResourceManager()
					.getResource(new ResourceLocation("ttf/"+str+".ttf")).getInputStream();
			font = Font.createFont(0, is);
			font = font.deriveFont(Font.PLAIN, size / 2f);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Error loading font");
			font = new Font("default", Font.PLAIN, +10);
		}
		return font;
	}

	public static FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
	public static FontRenderer fontSFLight42 = MinecraftFontRenderer.createFontRenderer(getEpicFont("sf-ui-display-light",42));
	public static FontRenderer fontSFLight55 = MinecraftFontRenderer.createFontRenderer(getEpicFont("sf-ui-display-light",55));
	public static FontRenderer fontSFLight35 = MinecraftFontRenderer.createFontRenderer(getEpicFont("sf-ui-display-light",35));
	public static FontRenderer fontSFLight120 = MinecraftFontRenderer.createFontRenderer(getEpicFont("sf-ui-display-light",120));
	public static FontRenderer fontMonoLisaL35 = MinecraftFontRenderer.createFontRenderer(getEpicFont("monolisa-light",35));
	public static FontRenderer fontMonoLisaLT = MinecraftFontRenderer.createFontRenderer(getEpicFont("monolisa-light",95));
}
