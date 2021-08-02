package cat.module;

import org.reflections.Reflections;

import java.util.ArrayList;

public final class ModuleManager {
    private final ArrayList<Module> modules = new ArrayList<>();
    public ModuleManager() {
        new Reflections("cat.module.modules").getSubTypesOf(Module.class).forEach(mod -> {
            try {
                modules.add(mod.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        //keep this at the bottom
        modules.forEach(Module::loadValues);
    }

    public ArrayList<Module> getModules(){
        return modules;
    }

    public Module getModule(String name) {
        for (Module m : modules) {
            if(m.getName().equalsIgnoreCase(name)){
                return m;
            }
            for(String alias : m.aliases) {
                if(alias.equalsIgnoreCase(name)) {
                    return m;
                }
            }
        }
        return null;
    }

    public Module getModule(Class<?> clazz) {
       return modules.stream().filter(mod -> mod.getClass() == clazz).findFirst().orElse(null);
    }

    public void handleKey(int keyCode){
        for (Module m : modules) {
            if(m.keyBind != 0 && keyCode == m.keyBind){
                m.toggle();
            }
        }
    }
}
