package cat.module.modules.combat;

import cat.events.Subscriber;
import cat.events.impl.PacketEvent;
import cat.events.impl.Render2DEvent;
import cat.events.impl.UpdateEvent;
import cat.events.impl.UpdatePlayerEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.types.BoolValue;
import cat.module.value.types.FloatValue;
import cat.module.value.types.IntegerValue;
import cat.util.ClientUtils;
import cat.util.EntityManager;
import cat.util.MillisTimer;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.*;
import org.lwjgl.input.Keyboard;

public class Aura extends Module {
    //TODO: Add autoBlock
    public boolean blockStatus = false;
    private final IntegerValue minCPS = new IntegerValue("1", "MinCPS", 15, 0, 20, 1, true, null);
    private final IntegerValue maxCPS = new IntegerValue("2", "MaxCPS", 7, 0, 20, 1, true, null);
    private final FloatValue range = new FloatValue("3", "Range", 3f, 1f, 0.1f, 6f, true, null);
    private final IntegerValue hurtTime = new IntegerValue("4", "HurtTime", 10, 1, 10, 1, true, null);
    private final BoolValue swing = new BoolValue("5", "SwingItem",true, true, null);
    private final BoolValue silent = new BoolValue("6", "SilentRotations",true, true, null);
    private final BoolValue autoBlock = new BoolValue("7", "AutoBlock",true, true, null);
    public Aura() {
        super("Aura", "", ModuleCategory.COMBAT, Keyboard.KEY_R, "aura", "ka", "killaura");
    }
    EntityLivingBase target = null;
    MillisTimer timer = new MillisTimer();
    long targetTime = 0;
    @Subscriber
    public void onUpdate(UpdatePlayerEvent e){
        if(target == null){
            unBlock();
        }
        for (Entity ent : mc.theWorld.loadedEntityList) {
            if(ent != null && mc.thePlayer.getDistanceToEntity(ent) <= range.get() && EntityManager.isTarget(ent)){
                target = (EntityLivingBase) ent;
            }
        }
        if(target != null && target.hurtTime <= hurtTime.get()){
            unBlock();
            final Vec3 eyesPos = new Vec3(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
            AxisAlignedBB bb = target.getEntityBoundingBox();
            final Vec3 entPos = new Vec3(bb.minX + (bb.maxX - bb.minX) * 0.5, bb.minY + (bb.maxY - bb.minY) * 0.5, bb.minZ + (bb.maxZ - bb.minZ) * 0.5f);
            final double diffX = entPos.xCoord - eyesPos.xCoord;
            final double diffY = entPos.yCoord - eyesPos.yCoord;
            final double diffZ = entPos.zCoord - eyesPos.zCoord;
            final float yaw = (float) MathHelper.wrapAngleTo180_double(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F);
            final float pitch = (float)MathHelper.wrapAngleTo180_double((-Math.toDegrees(Math.atan2(diffY, Math.sqrt(diffX * diffX + diffZ * diffZ)))));
            if(silent.get()){
                e.yaw = yaw;
                e.pitch = pitch;
            }else{
                mc.thePlayer.rotationYaw = yaw;
                mc.thePlayer.rotationPitch = pitch;
            }
            if(timer.hasTimeReached(targetTime)){
                mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
                if(swing.get()) {
                    mc.thePlayer.swingItem();
                }

                targetTime = ClientUtils.getRandomLong(minCPS.get(), maxCPS.get());
                timer.reset();
                target = null;
            }
            block();
        }
    }
    public void unBlock(){
        if (canBlock() || mc.thePlayer.isBlocking()) {
            mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            blockStatus = false;
        }
    }
    public void block(){
        if(canBlock() || mc.thePlayer.isBlocking()) {
            mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
            this.blockStatus = true;
        }
    }
    @Override
    public void onEnable(){
        target = null;
        blockStatus = false;
    }
    @Override
    public void onDisable(){
        target = null;
        if(blockStatus){
            unBlock();
            blockStatus = false;
        }
    }
    @Subscriber
    public void onPacket(PacketEvent e) {
        if((canBlock() || mc.thePlayer.isBlocking()) && e.packet instanceof C08PacketPlayerBlockPlacement || (e.packet instanceof C07PacketPlayerDigging && ((C07PacketPlayerDigging) e.packet).getStatus() == C07PacketPlayerDigging.Action.RELEASE_USE_ITEM)){
            e.cancel();
        }
        if(e.packet instanceof C09PacketHeldItemChange){
            mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            blockStatus = false;
        }
    }
    public boolean canBlock(){
        return mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && autoBlock.get();
    }
}
