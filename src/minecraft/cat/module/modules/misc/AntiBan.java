package cat.module.modules.misc;

import cat.events.Subscriber;
import cat.events.impl.PacketEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.types.BoolValue;
import cat.util.ClientUtils;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;

public class AntiBan extends Module {
    private final BoolValue confirm = new BoolValue("C0FPacketConfirmTransaction", true, true, null);
    private final BoolValue keepalive = new BoolValue("C00PacketKeepAlive", false, true, null);

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


