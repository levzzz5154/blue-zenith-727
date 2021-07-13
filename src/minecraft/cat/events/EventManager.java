package cat.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public final class EventManager {
    private final Map<Object, ArrayList<Method>> listeners = new LinkedHashMap<>();

    public void registerListener(Object listener) {
        ArrayList<Method> methods = new ArrayList<>();
        for (Method method : listener.getClass().getMethods()) {
            if (method.isAnnotationPresent(Subscriber.class) && method.getParameterTypes().length == 1 && method.getParameterTypes()[0].getSuperclass() == Event.class) {
                methods.add(method);
            }
        }
        if (!methods.isEmpty()) {
            listeners.put(listener, methods);
        }
    }

    public void unregisterListener(Object listener) {
        for (Map.Entry<Object, ArrayList<Method>> s : listeners.entrySet()) {
            if(s.getKey().getClass() == listener.getClass()){
                listeners.remove(s.getKey());
            }
        }
    }

    public void call(Event event) {
        for (Map.Entry<Object, ArrayList<Method>> s : listeners.entrySet()) {
            for (Method m : s.getValue()) {
                if(m.getParameterTypes()[0] == event.getClass()){
                    try {
                        m.invoke(s.getKey(), event);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}