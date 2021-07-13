package cat.module.value;

import cat.module.value.types.ValueConsumer;

public abstract class Value<T>{
    public String id;
    public String name;
    public T value;
    public boolean visible;
    protected final ValueConsumer<T, T> valueConsumer;

    public Value(String id, String valueName, T value, boolean visible, ValueConsumer<T, T> consumer){
        this.id = id;
        this.name = valueName;
        this.value = value;
        this.visible = visible;
        this.valueConsumer = consumer;
    }

    public abstract T get();
    public abstract void set(T newValue);
    public abstract T onChange(T oldValue, T newValue);
}
