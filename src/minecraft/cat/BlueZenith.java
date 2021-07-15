package cat;

import cat.command.CommandManager;
import cat.events.EventManager;
import cat.module.ModuleManager;
import cat.ui.GuiMain;
import cat.util.ClientUtils;
import cat.util.config.ConfigManager;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;

public class BlueZenith {
    public static String name = "BlueZenith";
    //da real version //the real version is 7.27 smh
    public static String version = "1.1";
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
        ConfigManager.load("default", false, false);
        ConfigManager.loadBinds();
        ClientUtils.getLogger().info("Loaded the default config and binds.");
        guiMain = new GuiMain();
        initRPC();
        //startup();
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
