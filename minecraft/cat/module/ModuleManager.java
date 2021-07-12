package cat.module;

import cat.BlueZenith;
import cat.module.modules.combat.Aura;
import cat.module.modules.combat.TestMod;
import cat.module.modules.misc.InventoryMove;
import cat.module.modules.movement.*;
import cat.module.modules.render.*;

import java.util.ArrayList;

public class ModuleManager {
    public ArrayList<Module> modules = new ArrayList<>();
    public ModuleManager(){
        modules.add(new HUD().setEnabled(true));
        modules.add(new Flight());
        modules.add(new FullBright());
        modules.add(new Aura());
        modules.add(new InventoryMove());
        modules.add(new TestMod());
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
