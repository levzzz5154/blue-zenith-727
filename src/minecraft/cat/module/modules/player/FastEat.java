package cat.module.modules.player;

import cat.events.Subscriber;
import cat.events.impl.UpdateEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.types.FloatValue;
import cat.module.value.types.ModeValue;
import net.minecraft.network.play.client.C03PacketPlayer;

public class FastEat extends Module {
    private final ModeValue mode = new ModeValue("eat_mode", "Mode", "Packet", true, null, "Packet", "Timer");
    private final FloatValue timer = new FloatValue("1", "Timer", 2f, 1.1f, 10f, 0.5f, true, __ -> mode.get().equals("Timer"));
    private final FloatValue packet = new FloatValue("1", "Packet", 5f, 1f, 20f, 0.5f, true, __ -> mode.get().equals("Packet"));

    public FastEat() {
        super("FastEat", "", ModuleCategory.PLAYER, "fasteat");
    }

    @Subscriber
    public void onUpdate(UpdateEvent e) {
        switch (mode.get()) {
            case "Packet":
                if (mc.thePlayer.isEating()) {
                    for (int i = 0; i < packet.get(); i++) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
                    }
                }
            case "Timer":
                if (mc.thePlayer.isEating()) {
                    mc.timer.timerSpeed = timer.get();
                }else{
                    mc.timer.timerSpeed = 1;
                }
        }
    }
}