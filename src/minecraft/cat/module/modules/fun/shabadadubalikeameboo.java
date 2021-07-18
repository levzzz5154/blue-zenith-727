package cat.module.modules.fun;

import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.Value;
import cat.module.value.types.FloatValue;
import cat.module.value.types.IntegerValue;
import cat.module.value.types.ModeValue;
import cat.module.value.types.StringValue;
import cat.util.ClientUtils;

public class shabadadubalikeameboo extends Module {
    FloatValue test1 = new FloatValue("test", 1, 0, 3,  1, true, null);
    IntegerValue test2 = new IntegerValue("test2", 1, 0, 3,  1, true, null);
    ModeValue test3 = new ModeValue("test3", "test", true, null, "test", "test1", "test2");
    // this breaks the config system :(
    StringValue test4 = new StringValue("test4", "sexy", true, null, null);
    StringValue test5 = new StringValue("test4", "hamburger", true, null, null);
    public shabadadubalikeameboo(){
        super("test_module", "", ModuleCategory.FUN);
    }
    @Override
    public void onEnable(){
        for (Value<?> v : this.getValues()) {
            ClientUtils.fancyMessage(v.name + ": "+v.get());
        }
    }
}
