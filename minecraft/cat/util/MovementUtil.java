package cat.util;

public class MovementUtil extends MinecraftInstance{
    public static void setSpeed(float f){
        if(mc.thePlayer == null || !(mc.thePlayer.movementInput.moveForward != 0F || mc.thePlayer.movementInput.moveStrafe != 0F)){
            return;
        }
        float rotationYaw = mc.thePlayer.rotationYaw;
        if (mc.thePlayer.moveForward < 0.0F)
            rotationYaw += 180.0F;
        float forward = 1.0F;
        if (mc.thePlayer.moveForward < 0.0F) {
            forward = -0.5F;
        } else if (mc.thePlayer.moveForward > 0.0F) {
            forward = 0.5F;
        }
        if (mc.thePlayer.moveStrafing > 0.0F)
            rotationYaw -= 90.0F * forward;
        if (mc.thePlayer.moveStrafing < 0.0F)
            rotationYaw += 90.0F * forward;

        float yaw = (float) Math.toRadians(rotationYaw);
        mc.thePlayer.motionX = -Math.sin(yaw) * f;
        mc.thePlayer.motionZ = Math.cos(yaw) * f;
    }
}
