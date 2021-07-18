package cat.module.modules.misc;

import cat.events.Subscriber;
import cat.events.impl.PacketEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.types.BooleanValue;
import cat.util.ClientUtils;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;

public class AntiBan extends Module {
    private final BooleanValue confirm = new BooleanValue("Cancel C0F", false, true, null);
    private final BooleanValue keepalive = new BooleanValue("Cancel C00", false, true, null);

    public AntiBan() {
        super("AntiBan", "", ModuleCategory.MISC, "antiban");
    }

    @Subscriber
    public void onPacket(PacketEvent e) {
        if (confirm.get()) {
            if (e.packet instanceof C0FPacketConfirmTransaction) {
                e.cancel();
            }
        }
        if (keepalive.get()) {
            if (e.packet instanceof C00PacketKeepAlive) {
                e.cancel();
            }
        }
    }
    public void onEnable() {
        ClientUtils.fancyMessage("DO NOT USE ANTIBAN ON HYPIXEL");
        ClientUtils.fancyMessage("DO NOT USE ANTIBAN ON HYPIXEL");
    }
}


