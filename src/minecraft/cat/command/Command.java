package cat.command;

import cat.util.ClientUtils;
import cat.util.MinecraftInstance;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;

public class Command extends MinecraftInstance {
    public String name;
    public String[] pref;
    public Command(String name, String... pref){
        this.name = name;
        this.pref = pref;
    }
    public void changedSound(){
        mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("mob.cat.purreow")));
    }
    public void execute(String[] args){

    }
    public void chat(String f){
        ClientUtils.displayChatMessage("§3§l[§r§bBlue Zenith§3§l] §r§9"+f);
    }
}
