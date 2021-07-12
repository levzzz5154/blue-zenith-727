package cat.module;

import cat.module.modules.combat.Aura;
import cat.module.modules.misc.InventoryMove;
import cat.module.modules.movement.*;
import cat.module.modules.render.*;
import com.sun.org.apache.xpath.internal.operations.Mod;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private ArrayList<Module> modules = new ArrayList<>();
    public ModuleManager(){
        modules.add(new HUD().setEnabled(true));
        modules.add(new Flight());
        modules.add(new FullBright());
        modules.add(new Aura());
        modules.add(new InventoryMove());
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
    public ArrayList<Module> getModules() {
        return modules;
    }
    public void handleKey(int keyCode){
        for (Module m : modules) {
            if(m.keyBind != 0 && keyCode == m.keyBind){
                m.toggle();
            }
        }
    }
}
