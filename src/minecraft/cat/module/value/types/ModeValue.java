package cat.module.value.types;

import cat.module.value.Value;

import java.util.ArrayList;
import java.util.Arrays;

public final class ModeValue extends Value<String> {

    private ArrayList<String> range;
    public ModeValue(String id, String valueName, String defaultValue, boolean visible, ValueConsumer<String, String> update, String... range) {
        super(id, valueName, defaultValue, visible, update);
        this.range = new ArrayList<>(Arrays.asList(range));
    }

    public ModeValue(String id, String valueName, String defaultValue, boolean visible, String... range) {
        super(id, valueName, defaultValue, visible, (p, p1) -> null);
        this.range = new ArrayList<>(Arrays.asList(range));
    }
    @Override
    public String get() {
        return this.value;
    }

    @Override
    public void set(String newValue) {
       String consumerResult = valueConsumer.method(this.value, newValue);
       if(consumerResult != null) {
           this.value = consumerResult;
       } else this.value = newValue;
    }


    @Override
    public String onChange(String oldValue, String newValue) {
        return newValue;
    }

    public void next() {
        int index = range.indexOf(this.value);
        if((index + 1) >= range.size()) {
            index = 0;
        } else index += 1;
        this.set(range.get(index));
    }

    public void previous() {
        int index = range.indexOf(this.value);
        if(index - 1 < 0) {
            index = range.size() - 1;
        } else index -= 1;
        this.set(range.get(index));
    }
}
