package cat.module;

import cat.module.modules.combat.Aura;
import cat.module.modules.combat.Velocity;
import cat.module.modules.misc.InventoryMove;
import cat.module.modules.movement.*;
import cat.module.modules.render.*;

import java.util.ArrayList;

public class ModuleManager {
    private final ArrayList<Module> modules = new ArrayList<>();
    public ModuleManager(){
        modules.add(new HUD().setState(true));
        modules.add(new Flight());
        modules.add(new FullBright());
        modules.add(new Aura());
        modules.add(new InventoryMove());
        modules.add(new Velocity());
        modules.add(new ClickGUI());
        modules.add(new Speed());
        modules.add(new NoSlowDown());
    }
    public ArrayList<Module> getModules(){
        return modules;
    }
    public Module getModule(String name){
        for (Module m : modules) {
            if(m.getName().equalsIgnoreCase(name)){
                return m;
            }
        }
        return null;
    }
    public Module getModule(Class<?> clazz){
        for (Module m : modules) {
            if(m.getClass() == clazz){
                return m;
            }
        }
        return null;
    }
    public void handleKey(int keyCode){
        for (Module m : modules) {
            if(m.keyBind != 0 && keyCode == m.keyBind){
                m.toggle();
            }
        }
    }
}
