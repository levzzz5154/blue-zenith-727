package cat.util;

import com.google.gson.JsonObject;
import net.minecraft.util.IChatComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientUtils extends MinecraftInstance {
    private static final Logger logger = LogManager.getLogger("BlueZenith");

    public static Logger getLogger() {
        return logger;
    }

    public static void displayChatMessage(final String message) {
        if (mc.thePlayer == null) {
            getLogger().info("(MCChat)" + message);
            return;
        }

        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("text", message);

        mc.thePlayer.addChatMessage(IChatComponent.Serializer.jsonToComponent(jsonObject.toString()));
    }
}
