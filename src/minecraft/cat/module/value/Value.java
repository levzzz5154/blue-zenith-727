package cat.module.value;

import com.sun.istack.internal.Nullable;

import java.util.function.Predicate;

public abstract class Value<T>{
    public final String id;
    public final String name;
    public T value;
    private boolean visible;
    protected final ValueConsumer<T> consumer;
    protected final Predicate<T> modifier;

    public Value(String id, String valueName, T value, boolean visible, @Nullable ValueConsumer<T> consumer, @Nullable Predicate<T> modifier){
        this.id = id;
        this.name = valueName;
        this.value = value;
        this.visible = visible;
        this.consumer = consumer;
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
