package cat.module.value.types;

import cat.module.value.Value;
import cat.module.value.ValueConsumer;

import java.util.function.Predicate;

public final class IntegerValue extends Value<Integer> {
    public int max, min;
    public final int increment;

    public IntegerValue(String id, String valueName, int value, int min, int max, int increment, boolean visible, ValueConsumer<Integer, Integer> consumer, Predicate<Integer> modifier) {
        super(id, valueName, value, visible, consumer, modifier);
        this.max = max;
        this.min = min;
        this.increment = increment;
    }

    public IntegerValue(String id, String valueName, int value, int min, int max, int increment, boolean visible, Predicate<Integer> modifier) {
        super(id, valueName, value, visible, (___, __) -> 69420, modifier);
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
        int old = this.value;
        int consumerResult = valueConsumer.check(old, newValue);
        if(consumerResult != 69420) {
            this.value = consumerResult;
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

}
