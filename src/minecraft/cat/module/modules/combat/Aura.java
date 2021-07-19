package cat.module.modules.combat;

import cat.events.Subscriber;
import cat.events.impl.PacketEvent;
import cat.events.impl.UpdatePlayerEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.types.BooleanValue;
import cat.module.value.types.FloatValue;
import cat.module.value.types.IntegerValue;
import cat.module.value.types.ModeValue;
import cat.util.ClientUtils;
import cat.util.EntityManager;
import cat.util.MillisTimer;
import cat.util.PacketUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.*;
import net.minecraft.util.*;
import org.lwjgl.input.Keyboard;

import java.util.List;
import java.util.stream.Collectors;

public class Aura extends Module {
    public boolean blockStatus = false;
    private final ModeValue mode = new ModeValue("Mode", "Single", true, null, "Single", "Switch", "Multi");
    private final IntegerValue switchDelay = new IntegerValue("Switch Delay", 500, 50, 2000, 50, true, ___ -> mode.get().equals("Switch"));
    private final ModeValue sortMode = new ModeValue("Sort by", "Health", true,  (a1, a2) -> {target = null; return a2;}, null,"Health", "Distance", "HurtTime");
    private final IntegerValue minCPS = new IntegerValue("MinCPS", 15, 1, 20, 1, true, (a1, a2) -> {bruh(); return a2;}, null);
    private final IntegerValue maxCPS = new IntegerValue("MaxCPS", 7, 1, 20, 1, true, (a1, a2) -> {if(minCPS.get() > a2) minCPS.set(a2); return a2;}, null);
    private final FloatValue range = new FloatValue("Range", 3f, 1f, 6f, 1f, true, null);
    private final IntegerValue hurtTime = new IntegerValue( "HurtTime", 10, 1, 10, 1, true, __ -> !mode.get().equals("Multi"));
    private final BooleanValue swing = new BooleanValue( "Swing",true, true, null);
    private final BooleanValue rotations = new BooleanValue("Rotations",true, true, null);
    private final BooleanValue silent = new BooleanValue("Silent Rotations",true, true, __ -> rotations.get());
    private final BooleanValue autoBlock = new BooleanValue( "AutoBlock",true, true, null);
    private final BooleanValue vanillaAutoBlock = new BooleanValue( "Vanilla Autoblock",true, true, __ -> autoBlock.get());
    public Aura() {
        super("Aura", "", ModuleCategory.COMBAT, Keyboard.KEY_R, "aura", "ka", "killaura");
    }
    EntityLivingBase target = null;
    private final MillisTimer timer = new MillisTimer();
    private final MillisTimer switchTimer = new MillisTimer();

    @Subscriber
    public void onUpdate(UpdatePlayerEvent e) {
        //filter the list of entities
        List<EntityLivingBase> list = mc.theWorld.loadedEntityList.parallelStream().filter(ent -> ent instanceof EntityLivingBase
                && ((EntityLivingBase) ent).getHealth() > 0
                && EntityManager.isTarget(ent)
                && mc.thePlayer.getDistanceToEntity(ent) <= range.get())
                .map(j -> (EntityLivingBase) j) //due to the loadedEntityList being a list of Entity by default, you need to cast every entity to EntityLiving base
                .sorted((ent1, ent2) -> {
                    switch(sortMode.get()) { //this language fucking sucks
                        case "Distance":
                            return Float.compare(mc.thePlayer.getDistanceToEntity(ent1), mc.thePlayer.getDistanceToEntity(ent2));

                        case "HurtTime":
                            return Integer.compare(ent1.hurtTime, ent2.hurtTime);

                        default:
                            return Float.compare(ent1.getHealth(), ent2.getHealth());
                    }
        }).collect(Collectors.toList());

        if(list.isEmpty()) {
            if(blockStatus) {
                unBlock();
            }
            return;
        }

        switch(mode.get()) {
            case "Single":
                if(target == null) {
                    target = list.get(0);
                }
                attack(target, e);
                break;

            case "Switch":
                if(target == null || switchTimer.hasTimeReached(switchDelay.get())) {
                    костыль(list);
                    switchTimer.reset();
                }
            attack(target, e);
            break;

            case "Multi":
                if(target == null || target.hurtTime > 0)
                    костыль(list);
                attack(target, e);
            break;
        }
    }

    private void костыль(List<EntityLivingBase> f) {
           int g = f.indexOf(target) + 1;
           if(g >= f.size()) {
            target = f.get(0);
        } else target = f.get(g);
    }

    private void attack(EntityLivingBase target, UpdatePlayerEvent e) {
        if(target.getHealth() <= 0) { this.target = null; return; }
        if(target.hurtTime <= hurtTime.get()){
            if(!vanillaAutoBlock.get()){
                unBlock();
            }
            if(rotations.get()) {
                setRotation(e);
            }
            if(timer.hasTimeReached(ClientUtils.getRandomLong(minCPS.get(), maxCPS.get()))){
                if(swing.get()) {
                    mc.thePlayer.swingItem();
                } else PacketUtil.send(new C0APacketAnimation());
                mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
                timer.reset();
            }
            block();
        }
    }
    private void setRotation(UpdatePlayerEvent e){
        final Vec3 eyesPos = new Vec3(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
        AxisAlignedBB bb = target.getEntityBoundingBox();
        float cd = ClientUtils.getRandomFloat(-0.5f, 0.5f);
        final Vec3 entPos = new Vec3(bb.minX + (bb.maxX - bb.minX) * (0.5 * cd), bb.minY + (bb.maxY - bb.minY) * (0.5 * cd), bb.minZ + (bb.maxZ - bb.minZ) * (0.5 * cd));
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
        if(e.packet instanceof C09PacketHeldItemChange){
            mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            blockStatus = false;
        }
    }
    public boolean canBlock(){
        return mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && autoBlock.get();
    }

    @Override
    public String getTagName() {
        return this.displayName + " §7" +mode.get();
    }

    private void bruh() {
        if(minCPS.get() > maxCPS.get()) {
            maxCPS.set(minCPS.get());
        }
    }
}
