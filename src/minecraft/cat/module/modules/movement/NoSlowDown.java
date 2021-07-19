package cat.module.modules.movement;

import cat.events.Subscriber;
import cat.events.impl.SlowdownEvent;
import cat.events.impl.UpdatePlayerEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.types.FloatValue;
import cat.module.value.types.ModeValue;
import cat.util.PacketUtil;
import net.minecraft.item.ItemFood;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import static cat.util.ClientUtils.fancyMessage;

@SuppressWarnings("unused")
public class NoSlowDown extends Module {
    public FloatValue itemMulti = new FloatValue("Reduce by", 1f, 0f, 1f, 0.1f, true, null);
    public NoSlowDown() {
        super("NoSlowDown", "", ModuleCategory.MOVEMENT, "noslow");
    }

    private final ModeValue mode = new ModeValue("Mode", "NCP", true, null, "NCP", "Vanilla");

    @Subscriber
    public void onSlowdown(SlowdownEvent event) {
        if(itemMulti.get() == 0F) event.cancel();
        event.reducer = itemMulti.get();
    }

    @Subscriber
    public void onUpdate(UpdatePlayerEvent event) {
            if (mc.thePlayer.isUsingItem() && !(mc.thePlayer.getHeldItem().getItem() instanceof ItemFood) && event.post()) {
                PacketUtil.send(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.UP));
            }
            if(mc.thePlayer.isUsingItem() && !(mc.thePlayer.getHeldItem().getItem() instanceof ItemFood) && event.pre()) {
                PacketUtil.send(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.getHeldItem(), 0, 0, 255));
            }
        }


}
