package cat.module.value.types;

import cat.module.Module;
import cat.module.value.Value;

public class FloatValue extends Value<Float> {
    public Float max;
    public Float min;

    public FloatValue(String id, String valueName, Float value, Float min, Float max, boolean visible) {
        super(id, valueName, value, visible, (p1, p2) -> 69420F);
        this.max = max;
        this.min = min;
    }

    public FloatValue(String id, String valueName, Float value, Float min, Float max, boolean visible, ValueConsumer<Float, Float> consumer) {
        super(id, valueName, value, visible, consumer);
        this.max = max;
        this.min = min;
    }

    @Override
    public Float get() {
        return this.value;
    }

    @Override
    public void set(Float newValue) {
        //lmao
        float consumerResult = valueConsumer.method(this.value, newValue);
        if(consumerResult != 69420F) {
            this.value = consumerResult;
        } else{
            this.value = onChange(this.value, newValue);
        }
    }

    @Override
    public Float onChange(Float oldValue, Float newValue) {
        return newValue;
    }
}
