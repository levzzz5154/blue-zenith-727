package cat.module.modules.movement;

import cat.module.Module;
import cat.module.ModuleCategory;
import cat.util.MovementUtil;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;

public class Flight extends Module {
    public Flight() {
        super("Flight", "Vanilla", ModuleCategory.MOVEMENT, Keyboard.KEY_F);
    }
    public void onUpdate(){
        MovementUtil.setSpeed(1);
    }
    public AxisAlignedBB onBlockBB(BlockPos pos, Block block, AxisAlignedBB blockBB){
        return AxisAlignedBB.fromBounds(-5, -1, -5, 5 , 1, 5).offset(pos.getX(), pos.getY(), pos.getZ());
    }
}
