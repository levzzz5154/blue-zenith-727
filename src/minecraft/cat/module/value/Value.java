package cat.module.value;

import cat.module.Module;

public abstract class Value<T>{
    public String id;
    public String name;
    public T value;
    boolean visible;

    public Value(String id, String valueName, T value, boolean visible){
        this.id = id;
        this.name = valueName;
        this.value = value;
        this.visible = visible;
    }

    public abstract T get();
    public abstract void set(T newValue);
}
