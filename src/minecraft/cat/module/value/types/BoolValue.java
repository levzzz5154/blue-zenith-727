package cat.module.value.types;

import cat.module.value.Value;
import cat.module.value.ValueConsumer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.util.function.Predicate;

public final class BoolValue extends Value<Boolean> {
    public BoolValue(String valueName, Boolean value, boolean visible, Predicate<Boolean> modifier) {
        super(valueName, value, visible, null, modifier);
    }

    public BoolValue(String valueName, Boolean value, boolean visible, ValueConsumer<Boolean> consumer, Predicate<Boolean> modifier) {
        super(valueName, value, visible, consumer, modifier);
    }

    @Override
    public Boolean get() {
        return this.value;
    }

    @Override
    public void set(Boolean newValue) {
        if(consumer != null) {
            this.value = consumer.check(this.value, newValue);
        } else this.value = newValue;
    }

    public void next() {
        set(!value);
    }

    @Override
    public void previous() {
        set(!value);
    }

    @Override
    public JsonElement getPrimitive() {
        return new JsonPrimitive(this.value);
    }

    @Override
    public void fromPrimitive(JsonPrimitive primitive) {
         set(primitive.getAsBoolean());
    }
}
