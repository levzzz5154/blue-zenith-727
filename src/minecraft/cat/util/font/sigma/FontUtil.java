package cat.util.font.sigma;

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
			font = Font.createFont(Font.TRUETYPE_FONT, is);
			font = font.deriveFont(Font.PLAIN, size / 2f);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Error loading font");
			font = new Font("default", Font.PLAIN, (int) (size / 2));
		}
		return font;
	}

	public static FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
	public static TFFFontRenderer fontSFLight35 = new TFFFontRenderer(getEpicFont("sf-ui-display-light", 35));
	public static TFFFontRenderer fontSFLight42 = new TFFFontRenderer(getEpicFont("sf-ui-display-light", 42));
}
