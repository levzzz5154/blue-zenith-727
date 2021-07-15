package cat.command.commands;

import cat.BlueZenith;
import cat.command.Command;
import cat.module.Module;

public class ToggleCommand extends Command {
    public ToggleCommand() {
        super("Toggle", "Toggle a module.",".t module","t", "enable");
    }
    @Override
    public void execute(String[] args){
        if(args.length > 1){
            Module m = BlueZenith.moduleManager.getModule(args[1]);
            if(m == null){
                chat("Invalid module.");
                return;
            }
            m.toggle();
            chat(m.getState() ? "Enabled module "+m.getName()+"." : "Disabled module "+m.getName()+".");
        }else{
            chat("Syntax: .t <module>");
        }
    }
}
