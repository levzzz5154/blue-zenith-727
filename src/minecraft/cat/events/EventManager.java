package cat.events;

import cat.BlueZenith;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public final class EventManager {
    private final Map<Class, CopyOnWriteArrayList<Method>> listeners = new LinkedHashMap<>();

    /**
     * only use this when shutting down the client or ur fucked
     */
    public void shutdown() {
        listeners.clear();
    }

    public void registerListener(Object listener) {
         for (Method method : listener.getClass().getMethods()) {
            if(method.isAnnotationPresent(Subscriber.class) && method.getParameterTypes().length == 1 && method.getParameterTypes()[0].getSuperclass() == Event.class) {
                Class<?> ev = method.getParameterTypes()[0];
                if(!listeners.containsKey(ev)) {
                    CopyOnWriteArrayList<Method> m = new CopyOnWriteArrayList<>();
                    m.add(method);
                    listeners.put(ev, m);
                } else {
                    listeners.get(ev).add(method);
                }
            }
         }
    }

    public void unregisterListener(Object listener) {
        listeners.values().forEach(list -> list.forEach(func -> list.removeIf(method -> method.getDeclaringClass() == listener.getClass())));
    }

    private final CopyOnWriteArrayList<Method> empty = new CopyOnWriteArrayList<>();
    public void call(Event event) {
        listeners.getOrDefault(event.getClass(), empty).forEach(a -> {
            try {
                a.invoke(BlueZenith.moduleManager.getModule(a.getDeclaringClass()), event);
            } catch(Exception ex) { }
            });
    }
}

