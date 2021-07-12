package cat.module.value.types;

import cat.module.Module;
import cat.module.value.Value;

public class FloatValue extends Value<Float> {
    Float max;
    Float min;

    public FloatValue(Module parentModule, String id, String valueName, Float value, Float max, Float min, boolean visible) {
        super(parentModule, id, valueName, value, visible);
        this.max = max;
        this.min = min;
    }

    @Override
    public Float get() {
        return this.value;
    }

    @Override
    public void set(Float newValue) {
        if(newValue <= max && newValue >= min){
            this.value = newValue;
        }
    }
}
