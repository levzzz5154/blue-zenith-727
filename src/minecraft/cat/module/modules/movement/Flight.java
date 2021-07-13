package cat.module.modules.movement;

import cat.events.Subscriber;
import cat.events.impl.BlockBBEvent;
import cat.events.impl.UpdateEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.types.BoolValue;
import cat.module.value.types.FloatValue;
import cat.module.value.types.ModeValue;
import cat.util.MovementUtil;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.input.Keyboard;

public class Flight extends Module {
    FloatValue speed = new FloatValue("1", "Speed", 2f, 0f, 5f, true);
    BoolValue blockBB = new BoolValue("2", "BlockBB", true, true);
    private final ModeValue mode = new ModeValue("flight_mode", "Mode", "Vanilla", true, "Vanilla", "Mode2", "Mode3", "Mode4");
    public Flight() {
        super("Flight", "Vanilla", ModuleCategory.MOVEMENT, Keyboard.KEY_F);
    }
    @Subscriber
    public void onUpdate(UpdateEvent e){
        MovementUtil.setSpeed(speed.get());
    }
    @Subscriber
    public void onBlockBB(BlockBBEvent e){
        if(this.blockBB.get() && !e.block.getMaterial().isSolid()){
            e.blockBB = AxisAlignedBB.fromBounds(-5, -1, -5, 5 , 1, 5).offset(e.pos.getX(), e.pos.getY(), e.pos.getZ());
        }
    }
}
