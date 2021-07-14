package cat.module;

import cat.BlueZenith;
import cat.command.Command;
import cat.module.Module;
import cat.module.value.Value;
import cat.module.value.types.BoolValue;
import cat.module.value.types.FloatValue;
import cat.module.value.types.IntegerValue;
import cat.module.value.types.ModeValue;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;

import java.util.regex.Pattern;

public class ModuleCommand extends Command {
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
                BoolValue v = (BoolValue) value;
                v.next();
                chat("Set "+v.name+" to "+v.get());
                changedSound();
            }else if(args.length > 2){
                if(value instanceof ModeValue) {
                    ModeValue v = (ModeValue) value;
                    String result = v.find(args[2]);
                    if(result != null) {
                        v.set(result);
                    } else {
                        chat("Illegal argument: " + args[2] + " is not in the " + value.name + " possible values range.");
                    }
                }
                if(!Pattern.matches("[a-zA-Z]+", args[2])){
                    if(value instanceof FloatValue){
                        FloatValue v = (FloatValue) value;
                        v.set(Float.parseFloat(args[2]));
                        chat("Set "+v.name+" to "+v.get());
                        changedSound();
                    }else if(value instanceof IntegerValue){
                        IntegerValue v = (IntegerValue) value;
                        v.set(Integer.parseInt(args[2]));
                        chat("Set "+v.name+" to "+v.get());
                        changedSound();
                    }
                }else{
                    chat("Syntax: $m <setting> <value>".replace("$m", name.toLowerCase()));
                }
            }else{
                chat("Syntax: $m <setting> <value>".replace("$m", name.toLowerCase()));
            }
        }else{
            chat("Syntax: $m <setting> <value>".replace("$m", name.toLowerCase()));
        }
    }
}
