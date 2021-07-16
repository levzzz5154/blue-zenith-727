package cat;

import cat.command.CommandManager;
import cat.events.EventManager;
import cat.module.ModuleManager;
import cat.module.modules.render.ClickGUI;
import cat.ui.GuiMain;
import cat.ui.clickgui.ClickGui;
import cat.util.ClientUtils;
import cat.util.config.ConfigManager;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.minecraft.client.Minecraft;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.MessageDigest;

public class BlueZenith {
    //test commit
    public static String name = "BlueZenith";
    //no men its 1.5 lmao :rofl:
    public static String version = "1.5";
    public static EventManager eventManager;
    public static ModuleManager moduleManager;
    public static CommandManager commandManager;
    public static GuiMain guiMain;
    private static final String applicationID = "865299936043073547";
    private static DiscordRichPresence rpc;

    public static void start(){
        ClientUtils.getLogger().info("Starting BlueZenith b"+version);
        eventManager = new EventManager();
        ClientUtils.getLogger().info("Started event manager.");
        moduleManager = new ModuleManager();
        ClientUtils.getLogger().info("Loaded "+moduleManager.getModules().size()+" modules.");
        commandManager = new CommandManager();
        ClientUtils.getLogger().info("Loaded "+commandManager.commands.size()+" commands.");
        hook();
        ClientUtils.getLogger().info("Added a shutdown hook.");
        // why load binds separately?
        ConfigManager.load("default", true, false);
        //ConfigManager.loadBinds();
        ClientUtils.getLogger().info("Loaded the default config and binds.");
        guiMain = new GuiMain();
        initRPC();
        //startup();
        ClickGUI.clickGui = new ClickGui();
        ClientUtils.getLogger().info("Creating ClickGui...");
        ClientUtils.getLogger().info("Finished Starting!");
    }

    private static void hook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            eventManager.shutdown();
            ConfigManager.save("default");
            ConfigManager.saveBinds();
            DiscordRPC.discordShutdown();
        }));
    }

    private static void initRPC() {
        rpc = new DiscordRichPresence.Builder("Main Menu").setDetails("Hello").setBigImage("main", "hi there").build();
        DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler((user) -> {
            ClientUtils.getLogger().info("Initialized Discord RPC!");
        }).build();
        DiscordRPC.discordInitialize(applicationID, handlers, true);
        DiscordRPC.discordUpdatePresence(rpc);
        updateRPC("In the main menu", "");
        new Thread(() -> {
            while (true) {
                DiscordRPC.discordRunCallbacks();
                DiscordRPC.discordUpdatePresence(rpc);
            }
        });
    }

    public static void updateRPC(String line1, String line2) {
        rpc = new DiscordRichPresence.Builder(line2).setDetails(line1).setBigImage("main", "hi there").build();
        DiscordRPC.discordUpdatePresence(rpc);
    }
}
