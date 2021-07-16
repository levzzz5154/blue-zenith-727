package cat;

import cat.client.ConfigManager;
import cat.client.Connection;
import cat.command.CommandManager;
import cat.events.EventManager;
import cat.module.ModuleManager;
import cat.ui.GuiMain;
import cat.util.ClientUtils;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.minecraft.client.gui.GuiScreen;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BlueZenith {
    public static String name = "BlueZenith";
    //da real version //the real version is 7.27 smh
    public static String version = "1.1";
    public static EventManager eventManager;
    public static ModuleManager moduleManager;
    public static CommandManager commandManager;
    public static GuiScreen guiMain;
    private static final String applicationID = "865299936043073547";
    public static ExecutorService executorService;
    private static DiscordRichPresence rpc;
    public static Connection connection;

    public static void start(){
        executorService = Executors.newSingleThreadExecutor();
        ClientUtils.getLogger().info("Starting BlueZenith b"+version);
        eventManager = new EventManager();
        ClientUtils.getLogger().info("Started event manager.");
        moduleManager = new ModuleManager();
        ClientUtils.getLogger().info("Loaded "+moduleManager.getModules().size()+" modules.");
        commandManager = new CommandManager();
        ClientUtils.getLogger().info("Loaded "+commandManager.commands.size()+" commands.");
        hook();
        ClientUtils.getLogger().info("Added a shutdown hook.");
        ConfigManager.load("default", false, false);
        ConfigManager.loadBinds();
        ClientUtils.getLogger().info("Loaded the default config and binds.");
        initRPC();
        ClientUtils.getLogger().info("Initialized Discord RPC!");
        connection = new Connection();
        connection.authenticate();
        ClientUtils.getLogger().info("Attempted connecting to the server.");
        guiMain = new GuiMain();
        ClientUtils.getLogger().info("Finished Starting!");

    }

    private static void hook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            eventManager.shutdown();
            ConfigManager.save("default");
            ConfigManager.saveBinds();
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
