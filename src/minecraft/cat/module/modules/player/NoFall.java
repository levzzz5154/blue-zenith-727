package cat.module.modules.player;

import cat.events.Subscriber;
import cat.events.impl.UpdatePlayerEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.types.BoolValue;
import cat.module.value.types.ModeValue;
import cat.util.PacketUtil;
import net.minecraft.network.play.client.C03PacketPlayer;

public class NoFall extends Module {
    private final ModeValue mode = new ModeValue("Mode", "Edit", true, null, "Edit", "Packet", "Verus");
    private final ModeValue editMode = new ModeValue("Edit mode", "NoGround", true, ___ -> mode.is("edit"), "NoGround", "SpoofGround");
    private final BoolValue verusMode = new BoolValue("Silent", true, false, ___ -> mode.is("verus"));

    public NoFall(){
        super("NoFall", "", ModuleCategory.PLAYER, "NoFall");
    }
    @Override
    public String getTag(){
        return this.mode.get();
    }

    @Subscriber
    public void onMotion(UpdatePlayerEvent event) {
        if(mode.is("Edit") && editMode.is("NoGround")) event.onGround = false;
        else if(mc.thePlayer.fallDistance >= 2.9) switch (mode.get()) {
            case "Edit":
                event.onGround = true;
            break;

            case "Packet":
                PacketUtil.send(new C03PacketPlayer(true));
            break;

            case "Verus":
                event.y = (int) event.y;
                event.onGround = true;
                if(!verusMode.get()) { mc.thePlayer.motionY = 0; }
                mc.thePlayer.fallDistance = 0;
            break;
        }
    }
    @Subscriber
    public void onPacket(UpdatePlayerEvent e){
        if (mode.get().equals("Edit")) {
            if(mc.thePlayer.fallDistance >= 3){
                e.onGround = true;
            }
        }
    }
}
