package cat.command.commands;

import cat.BlueZenith;
import cat.client.ConfigManager;
import cat.command.Command;
import cat.util.FileUtil;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public final class ConfigCommand extends Command {

    public ConfigCommand() {
        super("Config", "Manage your configs.","Usage: .config <load/save> <name> (binds) (norender)", "cfg");
    }

    private ScheduledFuture<?> task;
    @Override
    @SuppressWarnings("all")
    public void execute(String[] args) {
        switch(args.length) {
            case 2:
                switch(args[1].toLowerCase()) {
                    case "list":
                        final File configs = new File(FileUtil.configFolder);
                        final String[] list = configs.list();
                        if(list == null || list.length == 0) {
                            chat("Couldn't find any configs!");
                            return;
                        }
                        chat("All configs:");
                        chat("------------");
                        int amount = 0;
                        for(String filename : list) {
                            amount++;
                            chat(amount + ": " + filename.replace(".json", ""));
                        }
                        chat("------------");
                    break;

                    case "clear":
                        final File cfgs = new File(FileUtil.configFolder);
                        try {
                            task = BlueZenith.scheduledExecutorService.schedule(new Runnable() {
                                public void run() {
                                    final File cfgs = new File(FileUtil.configFolder);
                                    Arrays.stream(cfgs.listFiles()).forEach(file -> file.delete());
                                    chat("Cleared all configs!");
                                    task = null;
                                }
                            }, 10, TimeUnit.SECONDS);
                           chat("Your configs will be deleted in 10 seconds");
                           chat("If you do not want them to be deleted, execute .cfg cancel.");
                        } catch(Exception e) {
                            chat("Failed to clear configs!");
                        }
                    break;

                    case "cancel":
                        if(task == null) {
                            chat("No pending deletion task found.");
                            return;
                        }
                        task.cancel(true);
                        task = null;
                        chat("Cancelled configs deletion!");
                    break;
                }
            break;
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
