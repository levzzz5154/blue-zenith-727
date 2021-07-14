package cat;

import cat.command.CommandManager;
import cat.events.EventManager;
import cat.module.ModuleManager;
import cat.ui.GuiMain;
import cat.util.ClientUtils;

public class BlueZenith {
    public static String name = "BlueZenith";
    //da real version
    public static String version = "1.1";
    public static EventManager eventManager;
    public static ModuleManager moduleManager;
    public static CommandManager commandManager;
    public static GuiMain guiMain;
    public static void start(){
        ClientUtils.getLogger().info("Starting BlueZenith b"+version);
        eventManager = new EventManager();
        ClientUtils.getLogger().info("Started event manager.");
        moduleManager = new ModuleManager();
        ClientUtils.getLogger().info("Loaded "+moduleManager.getModules().size()+" modules.");
        commandManager = new CommandManager();
        ClientUtils.getLogger().info("Loaded "+commandManager.commands.size()+" commands.");
        guiMain = new GuiMain();
        ClientUtils.getLogger().info("Finished Starting!");
    }
}
