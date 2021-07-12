package cat.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public final class EventManager {
    private Map<Object, ArrayList<Method>> listeners = new LinkedHashMap<>();

    public void registerListener(Object listener) {
         ArrayList<Method> methods = new ArrayList<>();
         for (Method method : listener.getClass().getMethods()) {
            if(method.isAnnotationPresent(Subscriber.class) && method.getParameterTypes().length == 1 && method.getParameterTypes()[0].getSuperclass() == Event.class) {
                methods.add(method);
            }
         }
         if(!methods.isEmpty()) {
             listeners.put(listener, methods);
         }
    }

    public void unregisterListener(Object listener) {
        listeners.remove(listener);
    }

    public void call(Event event) {
        listeners.forEach((key, value) -> value.stream().filter(m -> m.getParameterTypes()[0] == event.getClass())
                .forEach(func -> {
                    try {
                        func.setAccessible(true);
                        func.invoke(key, event);
                    } catch(IllegalAccessException | InvocationTargetException ignored) {}
                }));
        }
    }
