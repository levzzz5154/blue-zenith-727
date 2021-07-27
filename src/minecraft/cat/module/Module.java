package cat.module;

import cat.BlueZenith;
import cat.module.value.Value;
import fr.lavache.anime.Animate;
import fr.lavache.anime.Easing;
import net.minecraft.client.Minecraft;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Module {
    public final String[] aliases;
    protected final Minecraft mc = Minecraft.getMinecraft();
    private final List<Value<?>> values = new ArrayList<>();
    public String displayName;
    private final String name;
    private String tag;
    private boolean state;
    private final ModuleCategory category;
    public int keyBind;
    public boolean showSettings, hidden;
    public Animate clickGuiAnim = new Animate().setEase(Easing.QUAD_IN_OUT).setSpeed(300);
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
        this.displayName = name;
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
        return displayName + "§7" + (getTag().isEmpty() ? "" : " " + getTag());
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
        return values.stream().filter(val -> val.name.equalsIgnoreCase(name)).findFirst()
                .orElse(values.stream().filter(val1 -> val1.name.replace(" ", "").equalsIgnoreCase(name)).findFirst().orElse(null));
    }
}
