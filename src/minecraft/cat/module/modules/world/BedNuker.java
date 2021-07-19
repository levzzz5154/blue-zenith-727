package cat.module.modules.world;

import cat.events.Subscriber;
import cat.events.impl.BlockBBEvent;
import cat.events.impl.UpdatePlayerEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.util.ClientUtils;
import net.minecraft.block.BlockBed;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@SuppressWarnings("unused")
public class BedNuker extends Module {
    public BedNuker() {
        super("BedNuker", "", ModuleCategory.WORLD);
    }
    BlockPos pos = null;
    boolean novolean = false;
    @Subscriber
    public void onUpdatePlayer(UpdatePlayerEvent e){
        if(pos != null){
            if(novolean){
                mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.DOWN));
                novolean = false;
            }
            mc.thePlayer.swingItem();
            mc.playerController.onPlayerDestroyBlock(pos, EnumFacing.DOWN);
        }
    }
    @Subscriber
    public void onBlockBB(BlockBBEvent e){
        if(mc.thePlayer.getDistanceSq(e.pos) <= 3 && mc.theWorld.getBlockState(e.pos).equals(Blocks.bed.getDefaultState()) && pos == null){
            pos = e.pos;
            novolean = true;
            ClientUtils.fancyMessage("detected funny block");
        }else if(e.pos == pos){
            mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, EnumFacing.DOWN));
            pos = null;
        }
    }
}
