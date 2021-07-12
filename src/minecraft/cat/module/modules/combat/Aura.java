package cat.module.modules.combat;

import cat.module.Module;
import cat.module.ModuleCategory;
import cat.util.EntityManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import org.lwjgl.input.Keyboard;

public class Aura extends Module {
    public Aura() {
        super("Aura", "", ModuleCategory.COMBAT, Keyboard.KEY_R);
    }
    EntityLivingBase target = null;
    public void onUpdate(){
        for (Entity ent : mc.theWorld.loadedEntityList) {
            if(ent != null && mc.thePlayer.getDistanceToEntity(ent) <= 3 && EntityManager.isTarget(ent)){
                target = (EntityLivingBase) ent;
            }
        }
        if(target == null){
            return;
        }

        mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
        mc.thePlayer.swingItem();

        target = null;
    }
}
