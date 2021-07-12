package cat.module;

import cat.module.modules.render.HUD;
import cat.util.MinecraftInstance;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class Module extends MinecraftInstance {
    //TODO: improve event system
    String name, tag;
    ModuleCategory category;
    public boolean enabled;
    public int keyBind = 0;

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
            onDisable();
        }else{
            onEnable();
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
}
