package cat;

import cat.client.ConfigManager;
import cat.client.Connection;
import cat.command.CommandManager;
import cat.events.EventManager;
import cat.module.ModuleManager;
import cat.module.modules.render.ClickGUI;
import cat.ui.GuiMain;
import cat.ui.clickgui.ClickGui;
import cat.util.ClientUtils;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;

import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class BlueZenith {
    //test commit
    public static String name = "BlueZenith";
    //no men its 1.5 lmao :rofl:
    public static String version = "1.5";
    public static EventManager eventManager;
    public static ModuleManager moduleManager;
    public static CommandManager commandManager;
    public static GuiMain guiMain;
    public static Connection connection;
    public static ExecutorService executorService = Executors.newSingleThreadExecutor();
    public static ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private static final String applicationID = "865299936043073547";
    private static DiscordRichPresence rpc;
 //h
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
        connection = new Connection();
        connection.authenticate();
        guiMain = new GuiMain();
        initRPC();
        //startup();
        ClickGUI.clickGui = new ClickGui();
        ClientUtils.getLogger().info("Creating ClickGui...");
        ClientUtils.getLogger().info("Finished Starting!");
    }

    private static void hook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if(moduleManager.getModule(ClickGUI.class).getState()) {
                moduleManager.getModule(ClickGUI.class).setState(false);
            }
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
    public static Color getMainColor(){
        return new Color(11,120,252);
    }
    public static Color getBackgroundColor(){
        return new Color(0,0,69);
    }
    public static Color getEpicColor(int secs){
        Color color = getBackgroundColor();
        Color color2 = getMainColor();
        double delay = Math.abs(System.currentTimeMillis() / 20L) / 100.0 + 6.0F * ((secs * 2) + 2.55) / 60;
        if (delay > 1.0) {
            final double n2 = delay % 1.0;
            delay = (((int) delay % 2 == 0) ? n2 : (1.0 - n2));
        }
        final double n3 = 1.0 - delay;
        return new Color((int) (color.getRed() * n3 + color2.getRed() * delay), (int) (color.getGreen() * n3 + color2.getGreen() * delay), (int) (color.getBlue() * n3 + color2.getBlue() * delay), (int) (color.getAlpha() * n3 + color2.getAlpha() * delay));
    }
}
