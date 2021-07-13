package cat.events;

import cat.BlueZenith;
import cat.module.ModuleManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public final class EventManager {
    private final Map<Class, CopyOnWriteArrayList<Method>> listeners = new LinkedHashMap<>();

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
        listeners.values().stream().forEach(list -> list.stream().forEach(func -> list.removeIf(method -> method.getDeclaringClass() == listener.getClass())));
    }

    public void call(Event event) {
        listeners.get(event.getClass()).forEach(method -> {
            Object obj = BlueZenith.moduleManager.getModule(method.getDeclaringClass());
            try {
                method.invoke(obj, event);
            } catch (IllegalAccessException | InvocationTargetException ignored) {ignored.printStackTrace();}
        });
        }
      }

