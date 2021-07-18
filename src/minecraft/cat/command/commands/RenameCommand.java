package cat.command.commands;

import cat.BlueZenith;
import cat.command.Command;
import cat.module.Module;

@SuppressWarnings("unused")
public class RenameCommand extends Command {

    public RenameCommand() {
        super("Rename", "Rename a module in the arraylist.",".rename <module> <new name>", "rn");
    }

    @Override
    public void execute(String[] args) {
        if(args.length == 1) {
            chat("Usage: <module> <new name>");
            return;
        }
        if(args.length == 2) {
            if(args[1].equalsIgnoreCase("reset")) {
                BlueZenith.moduleManager.getModules().forEach(a -> a.displayName = a.getName());
                chat("Reset all custom names!");
            }
        } else {
            Module mod = BlueZenith.moduleManager.getModule(args[1]);
            if (mod == null) {
                chat("Couldn't find that module!");
                return;
            }
            String newName = args[2];
            if (newName.equalsIgnoreCase("reset")) {
                mod.displayName = mod.getName();
                chat("Reset name for " + mod.getName() + "!");
            } else {
                mod.displayName = args[2].replaceAll("_", " ").replaceAll("&", "§");
                chat("Renamed " + mod.getName() + " to " + mod.displayName);
            }
        }
    }
}
