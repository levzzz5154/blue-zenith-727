package cat.module.value.types;

import cat.module.value.Value;
import cat.module.value.ValueConsumer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.util.function.Predicate;

public final class IntegerValue extends Value<Integer> {
    public Integer max, min;
    public final int increment;
    public IntegerValue(String id, String valueName, int value, int min, int max, int increment, boolean visible, ValueConsumer<Integer> consumer, Predicate<Integer> modifier) {
        super(id, valueName, value, visible, consumer, modifier);
        this.max = max;
        this.min = min;
        this.increment = increment;
    }

    public IntegerValue(String id, String valueName, int value, int min, int max, int increment, boolean visible, Predicate<Integer> modifier) {
        super(id, valueName, value, visible, null, modifier);
        this.max = max;
        this.min = min;
        this.increment = increment;
    }

    @Override
    public Integer get() {
        return this.value;
    }

    @Override
    public void set(Integer newValue) {
        if(consumer != null) {
            this.value = consumer.check(this.value, newValue);
        } else this.value = newValue;
    }
    @Override
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
        set(primitive.getAsInt());
    }
}
