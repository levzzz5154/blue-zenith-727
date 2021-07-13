package cat.module.value.types;

import cat.module.value.Value;

public class IntegerValue extends Value<Integer> {
    public Integer max, min;
    public IntegerValue(String id, String valueName, Integer value, Integer min, Integer max, boolean visible) {
        super(id, valueName, value, visible);
        this.max = max;
        this.min = min;
    }

    @Override
    public Integer get() {
        return this.value;
    }

    @Override
    public void set(Integer newValue) {
        onChange(this.value, newValue);
        this.value = newValue;
    }
    public Integer onChange(Integer oldValue, Integer newValue){
        return newValue;
    }
}
