package cat.module;

import cat.command.Command;
import cat.module.value.Value;
import cat.module.value.types.BoolValue;
import cat.module.value.types.FloatValue;
import cat.module.value.types.IntegerValue;
import cat.module.value.types.ModeValue;

public final class ModuleCommand extends Command {
    Module parent;
    public ModuleCommand(Module parent, String... pref) {
        super(parent.getName(), pref);
        this.parent = parent;
    }
    @Override
    public void execute(String[] args){
        if(args.length > 1){
            Value<?> value = parent.getValue(args[1]);
            if(value == null){
                chat("Invalid value!");
                return;
            }
            if (value instanceof BoolValue) {
                value.next();
                changedSound();
                chat("Set " + value.name + " to " + value.get());
            }
            else if(args.length > 2) {
                if(value instanceof ModeValue) {
                    ModeValue v = (ModeValue) value;
                    String result = v.find(args[2]);
                    if(result != null) {
                        v.set(result);
                    } else {
                        chat("Illegal argument: " + args[2] + " is not in the " + value.name + " possible values range.");
                    }
                }
                else if(value instanceof FloatValue){
                    ((FloatValue) value).set(Float.parseFloat(args[2]));
                    changedSound();
                }else if(value instanceof IntegerValue) {
                    ((IntegerValue)value).set(Integer.parseInt(args[2]));
                    changedSound();
                }
                chat("Set " + parent.name + " " + value.name + " to " + value.get());
            }else{
                chat("Syntax: $m <setting> <value>".replace("$m", name.toLowerCase()));
            }
        }else{
            chat("Syntax: $m <setting> <value>".replace("$m", name.toLowerCase()));
        }
    }
}
