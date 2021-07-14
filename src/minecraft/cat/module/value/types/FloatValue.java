package cat.module.value.types;

import cat.module.value.Value;
import cat.module.value.ValueConsumer;

import java.util.function.Predicate;

public class FloatValue extends Value<Float> {
    public Float max;
    public Float min;
    public final float increment;

    public FloatValue(String id, String valueName, float value, float min, float max, float increment, boolean visible, Predicate<Float> modifier) {
        super(id, valueName, value, visible, (p1, p2) -> 69420F, modifier);
        this.max = max;
        this.min = min;
        this.increment = increment;
    }

    public FloatValue(String id, String valueName, Float value, Float min, Float max, float increment, boolean visible, ValueConsumer<Float, Float> consumer, Predicate<Float> modifier) {
        super(id, valueName, value, visible, consumer, modifier);
        this.max = max;
        this.min = min;
        this.increment = increment;
    }

    @Override
    public Float get() {
        return this.value;
    }

    @Override
    public void set(Float newValue) {
        //lmao
        float consumerResult = valueConsumer.check(this.value, newValue);
        if(consumerResult != 69420F) {
            this.value = consumerResult;
        } else{
            this.value = newValue;
        }
    }
    public void next() {
        set(Math.min(value + increment, max));
    }

    @Override
    public void previous() {
        set(Math.max(value - increment, min));
    }
}
