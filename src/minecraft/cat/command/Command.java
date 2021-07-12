package cat.command;

import cat.util.ClientUtils;
import cat.util.MinecraftInstance;

public class Command extends MinecraftInstance {
    public String name, pref;
    public Command(String name, String pref){
        this.name = name;
        this.pref = pref;
    }

    public void execute(String[] args){

    }
    public void chat(String f){
        ClientUtils.displayChatMessage("§3§l[§r§bBlue Zenith§3§l] §r§9"+f);
    }
}
