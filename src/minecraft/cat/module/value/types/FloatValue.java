package cat.module.value.types;

import cat.module.Module;
import cat.module.value.Value;

public class FloatValue extends Value<Float> {
    public Float max;
    public Float min;

    public FloatValue(String id, String valueName, Float value, Float min, Float max, boolean visible) {
        super(id, valueName, value, visible);
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
