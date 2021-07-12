package cat.command.commands;

import cat.BlueZenith;
import cat.command.Command;
import cat.module.Module;
import cat.module.value.Value;
import cat.module.value.types.BoolValue;
import cat.module.value.types.FloatValue;

import java.util.regex.Pattern;

public class ValueCommand extends Command {
    public ValueCommand() {
        super("Value", "v");
    }

    @Override
    public void execute(String[] args) {
        if(args.length > 2){
            Module m = BlueZenith.moduleManager.getModule(args[1]);
            if(m == null){
                chat("Invalid module.");
                return;
            }
            Value<?> value = m.getValue(args[2]);
            if(value == null){
                chat("Invalid value.");
                return;
            }
            if(value instanceof BoolValue){
                BoolValue b = (BoolValue) value;
                b.value = !b.value;
                chat("Set "+b.name+" to "+b.value);
            }else if(args.length > 3){
                if(value instanceof FloatValue){
                    if(!Pattern.matches("[a-zA-Z]+", args[3])){
                        FloatValue b = (FloatValue) value;
                        float c = Float.parseFloat(args[3]);
                        b.value = c;
                        chat("Set "+b.name+" to "+c);
                    }else{
                        chat("Cannot convert "+args[3]+" to number.");
                    }
                }
            }
        }else{
            chat("Syntax: .v <module> <value> <value (?)>");
        }
    }
}
