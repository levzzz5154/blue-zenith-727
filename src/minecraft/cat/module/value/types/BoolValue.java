package cat.module.value.types;

import cat.module.value.Value;
import cat.module.value.ValueConsumer;

import java.util.function.Predicate;

public class BoolValue extends Value<Boolean> {
    public BoolValue(String id, String valueName, Boolean value, boolean visible, Predicate<Boolean> modifier) {
        super(id, valueName, value, visible, (p1, p2) -> null, modifier);
    }

    public BoolValue(String id, String valueName, Boolean value, boolean visible, ValueConsumer<Boolean, Boolean> consumer, Predicate<Boolean> modifier) {
        super(id, valueName, value, visible, consumer, modifier);
    }

    @Override
    public Boolean get() {
        return this.value;
    }

    @Override
    public void set(Boolean newValue) {
        Object result = valueConsumer.check(this.value, newValue);
        if(result != null) {
            this.value = (boolean) result;
        } else{
            this.value = newValue;
        }
    }

    @Override
    public void next() {
        set(!value);
    }

    @Override
    public void previous() {
        set(!value);
    }
}
