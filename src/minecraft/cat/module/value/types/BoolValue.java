package cat.module.value.types;

import cat.module.value.Value;

public class BoolValue extends Value<Boolean> {
    public BoolValue(String id, String valueName, Boolean value, boolean visible) {
        super(id, valueName, value, visible, (p1, p2) -> null);
    }

    public BoolValue(String id, String valueName, Boolean value, boolean visible, ValueConsumer<Boolean, Boolean> consumer) {
        super(id, valueName, value, visible, consumer);
    }

    @Override
    public Boolean get() {
        return this.value;
    }

    @Override
    public void set(Boolean newValue) {
        if(valueConsumer.method(this.value, newValue) != null) {
            this.value = valueConsumer.method(this.value, newValue);
        } else{
            this.value = onChange(this.value, newValue);
        }
    }

    @Override
    public Boolean onChange(Boolean oldValue, Boolean newValue) {
        return newValue;
    }
}
