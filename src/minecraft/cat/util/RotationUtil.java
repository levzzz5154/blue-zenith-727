package cat.util;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class RotationUtil extends MinecraftInstance {
    //TODO: fix this shid
    public static boolean isFacingPlayer(float yaw, float pitch) {
        final float collisionBorderSize = mc.thePlayer.getCollisionBorderSize();
        final AxisAlignedBB axisAlignedBB = mc.thePlayer.getEntityBoundingBox().expand(collisionBorderSize, collisionBorderSize, collisionBorderSize);
        final float yawCos = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
        final float yawSin = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
        final float pitchCos = -MathHelper.cos(-pitch * 0.017453292F);
        final float pitchSin = MathHelper.sin(-pitch * 0.017453292F);

        final Vec3 entityLook = new Vec3(yawSin * pitchCos, pitchSin, yawCos * pitchCos);
        return axisAlignedBB.isVecInside(entityLook);
    }
}
