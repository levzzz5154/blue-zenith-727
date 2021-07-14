package cat.module;

import cat.BlueZenith;
import cat.module.value.Value;
import cat.util.MinecraftInstance;
import org.lwjgl.input.Mouse;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Module extends MinecraftInstance {
    final String name;
    String tag;
    ModuleCategory category;
    private boolean state;
    public int keyBind;
    public boolean showSettings;
    private boolean wasPressed;
    public final String[] aliases;
    private final List<Value<?>> values = new ArrayList<>();
    public Module(String name, String tag, ModuleCategory cat, String... aliases){
        this(name, tag, cat, 0, aliases);
    }
    public void loadValues() {
        for(Field i : getClass().getDeclaredFields()) {
            i.setAccessible(true);
            Object o = null;
            try {
                o = i.get(this);
            } catch(IllegalAccessException ignored) {}
            if(o instanceof Value) {
                values.add((Value<?>) o);
            }
        }
    }
    public List<Value<?>> getValues(){
        return this.values;
    }
    public Module(String name, String tag, ModuleCategory cat, int keyBind, String... aliases){
        state = false;
        this.name = name;
        this.tag = tag;
        this.category = cat;
        this.keyBind = keyBind;
        this.aliases = aliases;
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

    protected void setTag(String newTag) {
        this.tag = newTag;
    }
    public void onDisable(){}
    public void onEnable() {}

    public String getTagName(){
        return getName() + "§7" + (getTag().isEmpty() ? "" : " " + getTag());
    }

    public final String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    public final ModuleCategory getCategory() {
        return category;
    }
    public Module setState(boolean state){
        if(state != this.state) {
            this.toggle();
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
    public boolean wasPressed(){
        return wasPressed;
    }
    public void updatePressed(){
        wasPressed = Mouse.isButtonDown(0) || Mouse.isButtonDown(1);
    }
}
