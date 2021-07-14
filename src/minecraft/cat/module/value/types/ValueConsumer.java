package cat.module.value.types;

import com.sun.istack.internal.Nullable;

@FunctionalInterface
public interface ValueConsumer<P1, P2> {
    @Nullable P1 check(P1 old, P2 new_);
}