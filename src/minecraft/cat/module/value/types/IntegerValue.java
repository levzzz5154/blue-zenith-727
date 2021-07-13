package cat.module.value.types;

import cat.module.value.Value;

public class IntegerValue extends Value<Integer> {
    public Integer max, min;
    public IntegerValue(String id, String valueName, Integer value, Integer min, Integer max, boolean visible, ValueConsumer<Integer, Integer> consumer) {
        super(id, valueName, value, visible, consumer);
        this.max = max;
        this.min = min;
    }

    public IntegerValue(String id, String valueName, Integer value, Integer min, Integer max, boolean visible) {
        super(id, valueName, value, visible, (___, __) -> 69420);
        this.max = max;
        this.min = min;
    }

    @Override
    public Integer get() {
        return this.value;
    }

    @Override
    public void set(Integer newValue) {
        int old = this.value;
        int consumerResult = valueConsumer.method(old, newValue);
        onChange(this.value, newValue);
        this.value = newValue;
        if(consumerResult != 69420) this.value = consumerResult;
    }
    public Integer onChange(Integer oldValue, Integer newValue){
        return newValue;
    }

}
