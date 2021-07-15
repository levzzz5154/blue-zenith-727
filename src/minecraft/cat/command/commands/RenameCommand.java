package cat.command.commands;

import cat.BlueZenith;
import cat.command.Command;
import cat.module.Module;

@SuppressWarnings("unused")
public class RenameCommand extends Command {

    public RenameCommand() {
        super("Rename", "Rename a module in the arraylist.","rn");
    }

    @Override
    public void execute(String[] args) {
        if(args.length == 1) {
            chat("Usage: <module> <new name>");
            return;
        }
        Module mod = BlueZenith.moduleManager.getModule(args[1]);
        if(mod == null) {
            chat("Couldn't find that module!");
            return;
        }
        if(args.length >= 3) {
            String newName = args[2];
            if(newName.equalsIgnoreCase("reset")) {
                mod.displayName = mod.getName();
                chat("Reset name for " + mod.getName() + "!");
            } else {
              mod.displayName = args[2].replaceAll("_", " ");
              chat("Renamed " + mod.getName() + " to " + mod.displayName);
            }
        }
    }
}
