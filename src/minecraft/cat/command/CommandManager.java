package cat.command;

import cat.BlueZenith;
import cat.module.ModuleCommand;
import cat.events.impl.SentMessageEvent;
import cat.module.Module;
import cat.util.ClientUtils;
import org.reflections.Reflections;

import java.util.ArrayList;

public class CommandManager {
    public String commandPrefix = ".";
    public ArrayList<Command> commands = new ArrayList<>();

    public CommandManager() {
        new Reflections("cat.command.commands").getSubTypesOf(Command.class).forEach(cmd -> {
            try {
                commands.add(cmd.getDeclaredConstructor().newInstance());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        //idk how to make it better sorry :(
        for (Module m : BlueZenith.moduleManager.getModules()) {
            commands.add(new ModuleCommand(m, m.getName().toLowerCase()));
        }
    }

    public void dispatch(SentMessageEvent event) {
        if (event.message.startsWith(commandPrefix)) {
            event.cancel();
            String[] args = event.message.substring(commandPrefix.length()).split(" ");
            for (Command command : commands) {
                if (command.name.equalsIgnoreCase(args[0])) {
                    command.execute(args);
                    return;
                }
                for (String alias : command.pref) {
                    if (alias.equalsIgnoreCase(args[0])) {
                        command.execute(args);
                        return;
                    }
                }

            }
            ClientUtils.fancyMessage("Couldn't find that command.");
        }
    }
}
