package cat.util.lmao;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.InputStream;

public class FontUtil {
	private static Font getJelloFont(float size, boolean bold) {
		Font font;
		try {
			InputStream is = Minecraft.getMinecraft().getResourceManager()
					.getResource(new ResourceLocation(bold ? "ttf/sf-ui-display-medium.ttf": "ttf/sf-ui-display-light.ttf")).getInputStream();
			font = Font.createFont(0, is);
			font = font.deriveFont(Font.PLAIN, size);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Error loading font");
			font = new Font("default", Font.PLAIN, +10);
		}
		return font;
	}

	public static FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
	public static FontRenderer fontSFLight50 = MinecraftFontRenderer.createFontRenderer(getJelloFont(25,false));
	public static FontRenderer fontSFLight42 = MinecraftFontRenderer.createFontRenderer(getJelloFont(20.5f,false));
	public static FontRenderer fontSFLight35 = MinecraftFontRenderer.createFontRenderer(getJelloFont(18,false));
}
