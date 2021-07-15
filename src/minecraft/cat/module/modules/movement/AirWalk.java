package cat.module.modules.movement;

import cat.events.Subscriber;
import cat.events.impl.BlockBBEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import net.minecraft.util.AxisAlignedBB;

@SuppressWarnings("unused")
public class AirWalk extends Module {
    public AirWalk() {
        super("AirWalk", "", ModuleCategory.MOVEMENT, "AirWalk");
    }
    // this is an excuse to have 2 fly
    @Subscriber
    public void onBlockBB(BlockBBEvent e){
        if(e.pos.getY() < mc.thePlayer.posY){
            e.blockBB = AxisAlignedBB.fromBounds(-5, -1, -5, 5, 1, 5).offset(e.pos.getX(), e.pos.getY(), e.pos.getZ());
        }
    }
}
