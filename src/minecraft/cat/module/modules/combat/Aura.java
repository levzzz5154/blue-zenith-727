package cat.module.modules.combat;

import cat.events.Subscriber;
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
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;

public class Aura extends Module {
    private final IntegerValue minCPS = new IntegerValue("1", "MinCPS", 15, 0, 20, true);
    private final IntegerValue maxCPS = new IntegerValue("2", "MaxCPS", 7, 0, 20, true);
    private final FloatValue range = new FloatValue("3", "Range", 3f, 0f, 8f, true);
    private final IntegerValue hurtTime = new IntegerValue("4", "hurtTime", 10, 1, 10, true);
    private final BoolValue swing = new BoolValue("5", "SwingItem",true, true);
    private final BoolValue silent = new BoolValue("6", "SilentRotations",true, true);
    public Aura() {
        super("Aura", "", ModuleCategory.COMBAT, Keyboard.KEY_R, "aura", "ka", "killaura");
    }
    EntityLivingBase target = null;
    MillisTimer timer = new MillisTimer();
    long targetTime = 0;
    @Subscriber
    public void onUpdate(UpdatePlayerEvent e){
        for (Entity ent : mc.theWorld.loadedEntityList) {
            if(ent != null && mc.thePlayer.getDistanceToEntity(ent) <= range.get() && EntityManager.isTarget(ent)){
                target = (EntityLivingBase) ent;
            }
        }
        if(target != null){
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
        }
    }
}
