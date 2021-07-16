package cat.module.modules.player;

import cat.events.Subscriber;
import cat.events.impl.UpdateEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import net.minecraft.network.play.client.C03PacketPlayer;

public class FastEat extends Module {
    public FastEat() {
        super("FastEat", "", ModuleCategory.PLAYER, "fasteat");
    }

    @Subscriber
    public void onUpdate(UpdateEvent e) {
        if (mc.thePlayer.isEating()) {
            for (int i = 0; i < 30; i++) {
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
            }
        }
    }
}