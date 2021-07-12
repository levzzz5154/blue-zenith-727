package cat;

import cat.command.CommandManager;
import cat.events.EventManager;
import cat.module.ModuleManager;
import cat.ui.GuiMain;
import cat.util.ClientUtils;

public class BlueZenith {
    public static String name = "BlueZenith";
    public static String version = "7.27";
    public static EventManager eventManager;
    public static ModuleManager moduleManager;
    public static CommandManager commandManager;
    public static GuiMain guiMain;
    public static void start(){
        ClientUtils.getLogger().info("Starting BlueZenith b"+version);
        eventManager = new EventManager();
        moduleManager = new ModuleManager();
        commandManager = new CommandManager();
        guiMain = new GuiMain();
        ClientUtils.getLogger().info("Finished Starting!");
    }
}
