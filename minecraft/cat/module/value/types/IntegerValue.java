package cat.module.value.types;

import cat.module.Module;
import cat.module.value.Value;

public class IntegerValue extends Value<Integer> {
    Integer max, min;
    public IntegerValue(Module parentModule, String id, String valueName, Integer value, Integer max, Integer min, boolean visible) {
        super(parentModule, id, valueName, value, visible);
        this.max = max;
        this.min = min;
    }

    @Override
    public Integer get() {
        return this.value;
    }

    @Override
    public void set(Integer newValue) {
        this.value = newValue;
    }
}
