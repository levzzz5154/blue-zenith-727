package cat.module.value.types;

import cat.module.value.Value;
import cat.module.value.ValueConsumer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.util.function.Predicate;

public final class FloatValue extends Value<Float> {
    public Float max;
    public Float min;
    public final float increment;

    public FloatValue(String id, String valueName, float value, float min, float max, float increment, boolean visible, Predicate<Float> modifier) {
        super(id, valueName, value, visible, null, modifier);
        this.max = max;
        this.min = min;
        this.increment = increment;
    }

    public FloatValue(String id, String valueName, Float value, Float min, Float max, float increment, boolean visible, ValueConsumer<Float> consumer, Predicate<Float> modifier) {
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
        if(consumer != null) {
            this.value = consumer.check(this.value, newValue);
        } else this.value = newValue;
    }
    public void next() {
        set(Math.min(value + increment, max));
    }

    @Override
    public void previous() {
        set(Math.max(value - increment, min));
    }

    @Override
    public JsonElement getPrimitive() {
        return new JsonPrimitive(this.value);
    }

    @Override
    public void fromPrimitive(JsonPrimitive primitive) {
         set(primitive.getAsFloat());
    }
}
