package cat.module.value.types;

import cat.module.Module;
import cat.module.value.Value;

public class BoolValue extends Value<Boolean> {
    public BoolValue(Module parentModule, String id, String valueName, Boolean value, boolean visible) {
        super(parentModule, id, valueName, value, visible);
    }

    @Override
    public Boolean get() {
        return this.value;
    }

    @Override
    public void set(Boolean newValue) {
        this.value = newValue;
    }
}
