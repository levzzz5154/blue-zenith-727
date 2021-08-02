package cat;

import cat.client.ConfigManager;
import cat.client.Connection;
import cat.client.HWID;
import cat.command.CommandManager;
import cat.events.EventManager;
import cat.module.ModuleManager;
import cat.module.modules.render.ClickGUI;
import cat.ui.GuiMain;
import cat.ui.clickgui.ClickGui;
import cat.util.ClientUtils;
import com.google.common.eventbus.EventBus;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import org.lwjgl.opengl.Display;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BlueZenith {
    //ddoxed developer
    private static final String[] devs = {"3c120deab28db9574f345f62f4fbca55acf2fb53930a0c7758610e4975997", "2129e0c24fc31b3c1963c7ff6732b233ca58c4e135dfe43b52e5abd5df0339e"};
    public static String currentServerIP;
    public static String name = "Blue Zenith";
    public static String version = "b2.0";
    public static boolean useExperimentalEventBus = true;
    public static EventManager eventManager;
    public static EventBus eventBus;
    public static ModuleManager moduleManager;
    public static CommandManager commandManager;
    public static GuiMain guiMain;
    public static Connection connection;
    public static ExecutorService executorService = Executors.newSingleThreadExecutor();
    public static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    private static final String applicationID = "871024843674497045";
    private static DiscordRichPresence rpc;
    private static long startTime;
    private static boolean isDeveloper = false;
    private static final boolean startRPC = true;

    private BlueZenith() {}

    public static void start() {
        startTime = System.currentTimeMillis();
        HWID.getHWID();
        isDeveloper = Arrays.stream(devs).anyMatch(hwid -> hwid.equalsIgnoreCase(HWID.hwid));
        ClientUtils.getLogger().info("Starting BlueZenith " + version);
        if(useExperimentalEventBus)
        eventBus = new EventBus();
        eventManager = new EventManager();
        ClientUtils.getLogger().info("Started event manager.");
        moduleManager = new ModuleManager();
        ClientUtils.getLogger().info("Loaded " + moduleManager.getModules().size() + " modules.");
        commandManager = new CommandManager();
        ClientUtils.getLogger().info("Loaded " + commandManager.commands.size() + " commands.");
        hook();
        ClientUtils.getLogger().info("Added a shutdown hook.");
        ConfigManager.load("default", false, false);
        ConfigManager.loadBinds();
        ClientUtils.getLogger().info("Loaded the default config and binds.");
        if(startRPC)
        initRPC();
        guiMain = new GuiMain();
        ClickGUI.clickGui = new ClickGui();
        ConfigManager.loadClickGUIPanels();
        ClientUtils.getLogger().info("Created ClickGUI.");
        Display.setTitle(name + " | 1.8.9 | " + version + (isDeveloper ? " | Developer" : "Beta"));
        ClientUtils.getLogger().info("Started in " + (System.currentTimeMillis() - startTime) + " ms.");
    }

    private static void hook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            moduleManager.getModule(ClickGUI.class).setState(false);
            eventManager.shutdown();
            ConfigManager.save("default");
            ConfigManager.saveBinds();
            ConfigManager.saveClickGUIPanels();
            DiscordRPC.discordShutdown();
        }));
    }

    private static void initRPC() {
        rpc = new DiscordRichPresence.Builder("Starting up...")
                .setBigImage("main", "Build " + version)
                .setStartTimestamps(System.currentTimeMillis())
                .setEndTimestamp(System.currentTimeMillis() - startTime)
                .build();

        DiscordEventHandlers handlers = new DiscordEventHandlers.Builder()
                .setReadyEventHandler(user ->
                   ClientUtils.getLogger().info("Started Discord RPC! Account: " + user.username + "#" + user.discriminator))
                .build();

        DiscordRPC.discordInitialize(applicationID, handlers, true);

        DiscordRPC.discordUpdatePresence(rpc);

        scheduledExecutorService.scheduleAtFixedRate(() -> { //update presence every second
            DiscordRPC.discordRunCallbacks();
            DiscordRPC.discordUpdatePresence(rpc);
        }, 0, 1, TimeUnit.SECONDS);
    }

    public static void updateRPC(String line1, String line2) {
        //developer badge :flushed:
        if(isDeveloper) {
            rpc = new DiscordRichPresence.Builder(line2)
                    .setBigImage("main", version)
                    .setSmallImage("developer_badge", "Maintainer")
                    .setDetails(line1)
                    .setStartTimestamps(System.currentTimeMillis())
                    .build();
        } else {
            rpc = new DiscordRichPresence.Builder(line2)
                    .setBigImage("main", version)
                    .setDetails(line1)
                    .setStartTimestamps(System.currentTimeMillis())
                    .build();
        }
        DiscordRPC.discordUpdatePresence(rpc);
    }

    public static void register(Object listener) {
        if(useExperimentalEventBus)
            eventBus.register(listener);
        else eventManager.registerListener(listener);
    }

    public static void unregister(Object listener) {
        if(useExperimentalEventBus)
            eventBus.unregister(listener);
        else eventManager.unregisterListener(listener);
    }
}
