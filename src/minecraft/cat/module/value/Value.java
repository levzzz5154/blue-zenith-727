package cat.module.value;

import cat.module.Module;
import cat.module.value.types.ValueConsumer;
import com.sun.istack.internal.Nullable;

import java.util.function.Predicate;

public abstract class Value<T>{
    public String id;
    public String name;
    public T value;
    private boolean visible;
    protected final ValueConsumer<T, T> valueConsumer;
    public final Predicate<T> modifier;

    public Value(String id, String valueName, T value, boolean visible, ValueConsumer<T, T> consumer, @Nullable Predicate<T> modifier){
        this.id = id;
        this.name = valueName;
        this.value = value;
        this.visible = visible;
        this.valueConsumer = consumer;
        this.modifier = modifier;
    }

    public abstract T get();
    public abstract void set(T newValue);
    public abstract void next();
    public abstract void previous();

    public boolean isVisible() {
        if(modifier == null) return visible;
        visible = modifier.test(value);
        return visible;
    }
}
