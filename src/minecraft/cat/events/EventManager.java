package cat.events;

import cat.BlueZenith;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public final class EventManager {
    private final Map<Class, CopyOnWriteArrayList<Method>> listeners = new LinkedHashMap<>();

    public void registerListener(Object listener) {
        for (Method method : listener.getClass().getMethods()) {
            if (method.isAnnotationPresent(Subscriber.class) && method.getParameterTypes().length == 1 && method.getParameterTypes()[0].getSuperclass() == Event.class) {
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

    public void call(Event event) {
        listeners.forEach((targetEvent, methods) -> {
            if(targetEvent == event.getClass()){
                for (Method m : methods) {
                    m.setAccessible(true);
                    try {
                        m.invoke(BlueZenith.moduleManager.getModule(m.getDeclaringClass()), event);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}