package cat.module.modules.player;

import cat.events.Subscriber;
import cat.events.impl.UpdateEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.types.IntegerValue;
import cat.util.ClientUtils;
import cat.util.PacketUtil;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Regen extends Module {
    IntegerValue speed = new IntegerValue("Speed", 10, 0, 100, 0, true, null);
    public Regen() {
        super("Regen", "", ModuleCategory.PLAYER);
    }
    @Subscriber
    public void onUpdate(@SuppressWarnings("unused") UpdateEvent e){
        if(mc.thePlayer.getFoodStats().getFoodLevel() > 19){
            if(mc.thePlayer.getHealth() < mc.thePlayer.getMaxHealth() || mc.thePlayer.hurtTime >= 7){
                for (int i = 0; i < speed.get(); i++) {
                    PacketUtil.send(new C03PacketPlayer(true));
                }
            }
        }
    }
}
