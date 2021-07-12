package cat.module.modules.movement;

import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.SettingManager;
import cat.module.value.types.FloatValue;
import cat.util.MovementUtil;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;

public class Flight extends Module {
    FloatValue speed = new FloatValue(this, "what", "Speed", 2f, 5f, 0.1f, true);
    public Flight() {
        super("Flight", "Vanilla", ModuleCategory.MOVEMENT, Keyboard.KEY_F);
        values.add(speed);
    }
    public void onUpdate(){
        MovementUtil.setSpeed(speed.get());
    }
    public AxisAlignedBB onBlockBB(BlockPos pos, Block block, AxisAlignedBB blockBB){
        return AxisAlignedBB.fromBounds(-5, -1, -5, 5 , 1, 5).offset(pos.getX(), pos.getY(), pos.getZ());
    }
}
