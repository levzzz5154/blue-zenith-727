package cat.module;

import cat.BlueZenith;
import cat.module.value.Value;
import cat.util.MinecraftInstance;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

import java.util.LinkedList;
import java.util.List;

public class Module extends MinecraftInstance {
    //TODO: improve event system
    String name, tag;
    ModuleCategory category;
    public boolean enabled;
    public int keyBind = 0;
    protected List<Value<?>> values = new LinkedList<>();

    public Module(String name, String tag, ModuleCategory cat){
        enabled = false;
        this.name = name;
        this.tag = tag;
        this.category = cat;
    }
    public Module(String name, String tag, ModuleCategory cat, int keyBind){
        enabled = false;
        this.name = name;
        this.tag = tag;
        this.category = cat;
        this.keyBind = keyBind;
    }

    public void onRender2D(){

    }
    public void onUpdate(){

    }
    public AxisAlignedBB onBlockBB(BlockPos pos, Block block, AxisAlignedBB blockBB){
        return blockBB;
    }
    public void toggle(){
        if(enabled){
            BlueZenith.eventManager.unregisterListener(this);
            onDisable();
        }else{
            onEnable();
            BlueZenith.eventManager.registerListener(this);
        }
        enabled = !enabled;
    }
    public void onDisable(){

    }
    public void onEnable(){

    }

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
    public Module setEnabled(boolean enabled){
        this.enabled = enabled;
        return this;
    }
    public boolean getState(){
        return enabled;
    }
    public Value<?> getValue(String name){
        for (Value<?> v : values) {
            if(v.name.equalsIgnoreCase(name)){
                return v;
            }
        }
        return null;
    }
}
