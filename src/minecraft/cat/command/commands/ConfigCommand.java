package cat.command.commands;

import cat.command.Command;
import cat.client.ConfigManager;

public final class ConfigCommand extends Command {

    public ConfigCommand() {
        super("Config", "Manage your configs.","Usage: .config <load/save> <name> (binds) (norender)", "cfg");
    }

    @Override
    public void execute(String[] args) {
        switch(args.length) {
            case 3:
              switch(args[1].toLowerCase()) {
                  case "load":
                      ConfigManager.load(args[2], false, false);
                      break;
                  case "save":
                      ConfigManager.save(args[2]);
                      break;
              }
            break;
            case 4:
                switch(args[1].toLowerCase()) {
                    case "load":
                        ConfigManager.load(args[2], args[3].equalsIgnoreCase("binds"), args[3].equalsIgnoreCase("norender"));
                        break;
                    case "save":
                        ConfigManager.save(args[2]);
                        break;
                }
            break;
            case 5:
                switch(args[1].toLowerCase()) {
                    case "load":
                        ConfigManager.load(args[2], (args[3].equalsIgnoreCase("binds") || args[4].equalsIgnoreCase("binds")), (args[3].equalsIgnoreCase("norender") || args[4].equalsIgnoreCase("norender")));
                        break;
                    case "save":
                        ConfigManager.save(args[2]);
                        break;
                }
            break;
            default:
                chat("Usage: .config <load/save> <name> (binds) (norender)");
            break;
        }
    }
}
