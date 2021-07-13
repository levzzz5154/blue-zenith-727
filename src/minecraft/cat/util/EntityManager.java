package cat.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;

public class EntityManager extends MinecraftInstance{
    public static boolean mobs = true;
    public static boolean players = true;
    public static boolean animals = true;
    public static boolean invisible = true;
    public static boolean dead = false;
    public static boolean isTarget(Entity ent){
        return ent != mc.thePlayer && ((invisible && ent.isInvisible()) || (dead && ent.isDead) || ((mobs && isMob(ent)) || (players && ent instanceof EntityPlayer) || (animals && isAnimal(ent))));
    }
    public static boolean isAnimal(Entity ent){
        return ent instanceof EntitySheep || ent instanceof EntityCow || ent instanceof EntityPig || ent instanceof EntityChicken || ent instanceof EntityRabbit;
    }
    public static boolean isMob(Entity ent){
        return ent instanceof EntityZombie || ent instanceof EntitySkeleton || ent instanceof EntityVillager || ent instanceof EntitySlime || ent instanceof EntityCreeper || ent instanceof EntityEnderman || ent instanceof EntityEndermite || ent instanceof EntitySpider;
    }
}
