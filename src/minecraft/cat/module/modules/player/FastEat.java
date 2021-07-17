package cat.module.modules.player;

import cat.events.Subscriber;
import cat.events.impl.UpdateEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.types.FloatValue;
import cat.module.value.types.ModeValue;
import net.minecraft.item.ItemFood;
import net.minecraft.network.play.client.C03PacketPlayer;

public class FastEat extends Module {
    private final ModeValue mode = new ModeValue("Mode", "Packet", true, null, "Packet", "Timer");
    private final FloatValue timer = new FloatValue("Timer", 2f, 1.1f, 10f, 0.5f, true, __ -> mode.get().equals("Timer"));
    private final FloatValue packet = new FloatValue("Packet", 5f, 1f, 20f, 0.5f, true, __ -> mode.get().equals("Packet"));

    public FastEat() {
        super("FastEat", "", ModuleCategory.PLAYER, "fasteat");
    }

    @Subscriber
    public void onUpdate(UpdateEvent e) {
        switch (mode.get()) {
            case "Packet":
                if (mc.thePlayer.isEating() && (mc.thePlayer.getItemInUse().getItem()instanceof ItemFood)) {
                    for (int i = 0; i < packet.get(); i++) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
                    }
                }
            break;
            case "Timer":
                if (mc.thePlayer.isEating() && (mc.thePlayer.getItemInUse().getItem()instanceof ItemFood))  {
                    mc.timer.timerSpeed = timer.get();
                } else {
                    mc.timer.timerSpeed = 1;
                }
            break;
        }
    }
}



