package cat.util.font.sigma;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.InputStream;

public class FontUtil {
	private static Font getEpicFont(String str, float size) {
		Font font = new Font("default", Font.PLAIN, (int) (size / 2));
		try {
			InputStream is = FontUtil.class.getResourceAsStream("/assets/minecraft/ttf/"+str+".ttf");
			if(is == null){
				throw new Exception("Font path does not exist. "+str);
			}
			font = Font.createFont(Font.TRUETYPE_FONT, is);
			font = font.deriveFont(Font.PLAIN, size / 2f);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Error loading font");
		}
		return font;
	}

	public static FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
	public static TFFFontRenderer fontSFLight35 = new TFFFontRenderer(getEpicFont("sf-ui-display-light", 35));
	public static TFFFontRenderer fontSFLight42 = new TFFFontRenderer(getEpicFont("sf-ui-display-light", 42));
	public static TFFFontRenderer fontComicSans42 = new TFFFontRenderer(getEpicFont("comic-sans-ms", 42));
	public static TFFFontRenderer I_icon = new TFFFontRenderer(getEpicFont("icons", 80));
}
