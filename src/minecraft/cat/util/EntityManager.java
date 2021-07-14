package cat.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;

public class EntityManager extends MinecraftInstance{
    public enum Targets{
        MOBS("Mobs", true),
        PLAYERS("Players", true),
        ANIMALS("Animals", true),
        INVISIBLE("Invisible", true),
        DEAD("Dead", false)
        ;
        public String displayName;
        public boolean on;
        Targets(String displayName, boolean on){
            this.displayName = displayName;
            this.on = on;
        }
    }
    public static boolean isTarget(Entity ent){
        return ent != mc.thePlayer && ((Targets.INVISIBLE.on && ent.isInvisible()) || (Targets.DEAD.on && !ent.isEntityAlive()) || ((Targets.MOBS.on && isMob(ent)) || (Targets.PLAYERS.on && ent instanceof EntityPlayer) || (Targets.ANIMALS.on && isAnimal(ent))));
    }
    public static boolean isAnimal(Entity ent){
        return ent instanceof EntitySheep || ent instanceof EntityCow || ent instanceof EntityPig
                || ent instanceof EntityChicken || ent instanceof EntityRabbit || ent instanceof EntityHorse;
    }
    public static boolean isMob(Entity ent){
        return ent instanceof EntityZombie || ent instanceof EntitySkeleton
                || ent instanceof EntityVillager || ent instanceof EntitySlime
                || ent instanceof EntityCreeper || ent instanceof EntityEnderman
                || ent instanceof EntityEndermite || ent instanceof EntitySpider
                || ent instanceof EntityWitch || ent instanceof EntityWither || ent instanceof EntityBlaze;
    }
}
