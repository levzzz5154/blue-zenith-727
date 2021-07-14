package cat.module.value.types;

import cat.module.value.Value;
import cat.module.value.ValueConsumer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

public final class ModeValue extends Value<String> {

    private ArrayList<String> range;

    public ModeValue(String id, String valueName, String defaultValue, boolean visible, ValueConsumer<String, String> update, Predicate<String> visibility, String... range) {
        super(id, valueName, defaultValue, visible, update, visibility);
        this.range = new ArrayList<>(Arrays.asList(range));
    }

    public ModeValue(String id, String valueName, String defaultValue, boolean visible, Predicate<String> visibility, String... range) {
        super(id, valueName, defaultValue, visible, (p, p1) -> null, visibility);
        this.range = new ArrayList<>(Arrays.asList(range));
    }

    @Override
    public String get() {
        return this.value;
    }

    @Override
    public void set(String newValue) {
        String consumerResult = valueConsumer.check(this.value, newValue);
        if(consumerResult != null) {
            this.value = consumerResult;
        } else this.value = newValue;
    }

    @Override
    public void next() {
        int index = range.indexOf(this.value);
        if((index + 1) >= range.size()) {
            index = 0;
        } else index += 1;
        this.set(range.get(index));
    }

    @Override
    public void previous() {
        int index = range.indexOf(this.value);
        if(index - 1 < 0) {
            index = range.size() - 1;
        } else index -= 1;
        this.set(range.get(index));
    }
}
