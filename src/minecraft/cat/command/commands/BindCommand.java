package cat.command.commands;

import cat.BlueZenith;
import cat.command.Command;
import cat.module.Module;
import org.lwjgl.input.Keyboard;

import java.util.Locale;
import java.util.regex.Pattern;

public class BindCommand extends Command {
    public BindCommand() {
        super("Bind", "bind");
    }
    @Override
    public void execute(String[] args){
        if(args.length > 2){
            Module m = BlueZenith.moduleManager.getModule(args[1]);
            if(m == null){
                chat("Invalid module.");
                return;
            }
            int k = Keyboard.getKeyIndex(args[2].toUpperCase(Locale.ROOT));
            if(k != 0){
                m.keyBind = k;
                chat("Bound "+m.getName()+" to "+args[2].toUpperCase(Locale.ROOT));
            }else{
                m.keyBind = Keyboard.KEY_NONE;
                chat("Bound "+m.getName()+" to NONE");
            }
        }else{
            chat("Syntax: .bind <module> <key>");
        }
    }
}
