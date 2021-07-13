package cat.module;

import cat.BlueZenith;
import cat.module.value.Value;
import cat.util.MinecraftInstance;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

import java.lang.reflect.Field;
import java.util.*;

public class Module extends MinecraftInstance {
    String name, tag;
    ModuleCategory category;
    private boolean state;
    public int keyBind;

    public Module(String name, String tag, ModuleCategory cat){
        this(name, tag, cat, 0);
    }
    private List<Value<?>> getValues(){
        ArrayList<Value<?>> d = new ArrayList<>();
        for (Field i : getClass().getDeclaredFields()) {
            i.setAccessible(true);
            Object o = null;
            try {
                o = i.get(this);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if(o instanceof Value){
                d.add((Value<?>) o);
            }
        }
        return d;
    }
    public Module(String name, String tag, ModuleCategory cat, int keyBind){
        state = false;
        this.name = name;
        this.tag = tag;
        this.category = cat;
        this.keyBind = keyBind;
    }

    public void toggle(){
        if(state){
            BlueZenith.eventManager.unregisterListener(this);
            onDisable();
        }else{
            BlueZenith.eventManager.registerListener(this);
            onEnable();
        }
        state = !state;
    }
    public void onDisable(){}
    public void onEnable() {}

    public String getTagName(){
        return name + "§7" + (tag.isEmpty() ? "" : " " + tag);
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    public ModuleCategory getCategory() {
        return category;
    }
    public Module setState(boolean state){
        if(!state && this.state){
            BlueZenith.eventManager.unregisterListener(this);
            onDisable();
        }else if(state && !this.state){
            BlueZenith.eventManager.registerListener(this);
            onEnable();
        }
        this.state = state;
        return this;
    }
    public boolean getState(){
        return state;
    }
    public Value<?> getValue(String name){
        for (Value<?> v : getValues()) {
            if(v.name.equalsIgnoreCase(name)){
                return v;
            }
        }
        return null;
    }
}
