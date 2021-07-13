package cat.module.value.types;

import cat.module.value.Value;

import java.util.ArrayList;
import java.util.Arrays;

public final class ModeValue extends Value<String> {

    private ArrayList<String> range;
    public ModeValue(String id, String valueName, String defaultValue, boolean visible, String... range) {
        super(id, valueName, defaultValue, visible);
        this.range = new ArrayList<>(Arrays.asList(range));
    }
    @Override
    public String get() {
        return null;
    }

    @Override
    public void set(String newValue) {

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
        this.value = range.get(index);
    }

    public void previous() {
        int index = range.indexOf(this.value);
        if(index - 1 < 0) {
            index = range.size() - 1;
        } else index -= 1;
        this.value = range.get(index);
    }
}
