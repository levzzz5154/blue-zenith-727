package cat.util;

import net.minecraft.network.Packet;

public final class PacketUtil extends MinecraftInstance {

    public static void send(Packet packet) {
        mc.getNetHandler().getNetworkManager().sendPacket(packet);
    }
    public static void sendSilent(Packet packet) {
        mc.getNetHandler().getNetworkManager().sendPacketNoEvent(packet);
    }
}
