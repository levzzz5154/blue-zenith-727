package cat.module.modules.player;

import cat.events.Subscriber;
import cat.events.impl.BlockBBEvent;
import cat.events.impl.UpdatePlayerEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.types.ModeValue;
import net.minecraft.util.AxisAlignedBB;

public class NoFall extends Module {
    private final ModeValue mode = new ModeValue("Mode", "Edit", true, null, "Edit", "Verus");
    public NoFall(){
        super("NoFall", "", ModuleCategory.PLAYER, "NoFall");
    }
    @Override
    public String getTag(){
        return this.mode.get();
    }
    @Subscriber
    public void onBlockBB(BlockBBEvent e){
        if(mode.get().equals("Verus")){
            if(mc.thePlayer.fallDistance >= 2.5 && e.pos.getY() < mc.thePlayer.posY && e.pos.getY() > mc.thePlayer.posY - 1){
                e.blockBB = AxisAlignedBB.fromBounds(-5, -1, -5, 5, 0, 5).offset(e.pos.getX(), e.pos.getY(), e.pos.getZ());
            }
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
