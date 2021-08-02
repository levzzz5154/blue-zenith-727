package cat.module.value.types;

import cat.module.value.Value;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ListValue extends Value<List<String>> {

    private final HashMap<String, Boolean> choices = new HashMap<>();

    public ListValue(String name, boolean visible, String... options) {
        super(name, Arrays.asList(options), visible, null, null);
        Arrays.stream(options).forEach(option -> choices.put(option, false));
    }

    public ListValue(String name, boolean visible, Predicate<List<String>> modifier, String... options) {
        super(name, Arrays.asList(options), visible, null, modifier);
        Arrays.stream(options).forEach(option -> choices.put(option, false));
    }


    @Override
    public List<String> get() {
        return value;
    }

    @Override
    public void set(List<String> newValue) {
         //unused
    }

    @Override
    public void next() {
        //unused
    }

    @Override
    public void previous() {
        //unused
    }


    public List<String> getOptions() {
        return choices.keySet().stream().collect(Collectors.toList());
    }

    public List<String> getSelectedOptions() {
        return choices.entrySet().stream().filter(Map.Entry::getValue).map(Map.Entry::getKey).collect(Collectors.toList());
    }

    public void toggleOption(String key) {
        if(!choices.containsKey(key)) throw new NullPointerException("Couldn't find option " + key + " for value " + name);
        choices.put(key, !choices.get(key));
    }

    public boolean getOptionState(String key) {
        if(!choices.containsKey(key)) throw new NullPointerException("Couldn't find option " + key + " for value " + name);
        return choices.get(key);
    }

    public void setOptionState(String key, boolean state) {
        if(!choices.containsKey(key)) throw new NullPointerException("Couldn't find option " + key + " for value " + name);
        choices.put(key, state);
    }

    @Override
    public JsonElement getPrimitive() {

        return null;
    }

    @Override
    public void fromPrimitive(JsonPrimitive primitive) {

    }

    public void fromObject(JsonObject object) {
        object.entrySet().forEach(entry -> {
                 setOptionState(entry.getKey(), entry.getValue().getAsBoolean());
        });
    }
}
