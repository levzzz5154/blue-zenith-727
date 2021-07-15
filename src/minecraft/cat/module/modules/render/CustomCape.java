package cat.module.modules.render;

import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.types.BoolValue;
import cat.util.ClientUtils;
import javafx.stage.FileChooser;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@SuppressWarnings("unused")
public class CustomCape extends Module {
    ResourceLocation d = null;
    BoolValue selectCape = new BoolValue("1", "SelectCape", false, true, (oldValue, newValue) -> {
        if(newValue){
            File temp = ClientUtils.openFileChooser(null, new FileChooser.ExtensionFilter("Image", "*.png", "*.jpg", "*.jpeg"));
            if(temp == null || !temp.exists()){
                ClientUtils.fancyMessage("The selected path doesn't exist.");
                d = null;
                return false;
            }
            d = new ResourceLocation("wawawawawawawawawawawawawawawawawawawawa");
            try {
                mc.getTextureManager().deleteTexture(d);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Files.readAllBytes(temp.toPath()));
                BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
                byteArrayInputStream.close();
                mc.getTextureManager().loadTexture(d, new DynamicTexture(bufferedImage));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        return false;
    }, null);
    public CustomCape() {
        super("CustomCape", "", ModuleCategory.RENDER, "customcape", "cape");
    }
    public ResourceLocation getCapeLocation(){
        return d == null ? new ResourceLocation("cat/cape.png") : d;
    }
}