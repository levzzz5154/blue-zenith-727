package cat.command;

import cat.command.commands.BindCommand;
import cat.command.commands.ToggleCommand;

import java.util.ArrayList;

public class CommandManager {
    public ArrayList<Command> commands = new ArrayList<>();
    public CommandManager(){
        commands.add(new ToggleCommand());
        commands.add(new BindCommand());
    }
}
