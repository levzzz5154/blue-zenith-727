package cat.command.commands;

import cat.BlueZenith;
import cat.command.Command;
import cat.module.Module;
import org.lwjgl.input.Keyboard;

import java.util.Locale;

public class BindCommand extends Command {
    public BindCommand() {
        super("Bind", "Bind a module.", "b");
    }
    @Override
    public void execute(String[] args){
        if(args.length > 2){
            Module m = BlueZenith.moduleManager.getModule(args[1]);
            if(m == null){
                Module m1 = BlueZenith.moduleManager.getModule(args[2]);
                checkKey(args[1], args[2]);
                if(m1 == null) {
                    checkKey(args[2], args[1]);
                } else {
                    int key = Keyboard.getKeyIndex(args[1].toUpperCase());
                    if(key == 0) {
                        chat("Invalid key specified: " + args[1]);
                        bind(0, m1);
                        return;
                    }
                    bind(key, m1);
                }
                return;
            }
            int k = Keyboard.getKeyIndex(args[2].toUpperCase(Locale.ROOT));
            bind(k, m);
        }else{
            chat("Syntax: .bind <module> <key>");
        }
    }
    private void checkKey(String key, String modName) {
        if(Keyboard.getKeyIndex(key.toUpperCase()) == 0) {
            chat("Failed to find module " + modName);
        }
    }

    private void bind(int key, Module mod) {
        mod.keyBind = key;
        chat("Bound " + mod.getName() + " to " + Keyboard.getKeyName(key));
    }
}
