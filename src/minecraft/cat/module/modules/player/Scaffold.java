package cat.module.modules.player;

import cat.events.Subscriber;
import cat.events.impl.UpdatePlayerEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.types.BooleanValue;
import cat.module.value.types.FloatValue;
import net.minecraft.block.Block;
import net.minecraft.util.*;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class Scaffold extends Module {

	public Scaffold() {
		super("Scaffold", "", ModuleCategory.PLAYER, Keyboard.KEY_B, "BlockFly", "FeetPlace");
	}

	private final FloatValue searchRange = new FloatValue("Search range", 3f, 1f, 5f, 0.1f, true, null);
	private final BooleanValue keepRotation = new BooleanValue("Keep rotation", true, true, null);

	// public Block nearestBlock;
	// public BlockPos nearestBlockPos;
	public EnumFacing nearestFace;
	BlockPos pos;
	public double distanceNearest;
	public double distanceCurrent;
	double oldPlayerX;
	double oldPlayerY;
	double oldPlayerZ;
	float oldPlayerYaw;
	float oldPlayerPitch;
	// public ArrayList<BlockPos> le_fullBlockPoses = new ArrayList<BlockPos>();

	@Subscriber
	public void onUpdatePlayer(UpdatePlayerEvent e) {
		final float r = searchRange.get();

		if (oldPlayerX != mc.thePlayer.posX || oldPlayerZ != mc.thePlayer.posZ || oldPlayerY != mc.thePlayer.posY) {
			final ArrayList<BlockPos> le_blockPoses = getAllBlockPoses(r);
			final ArrayList<BlockPos> le_fullBlockPoses = removeAir(le_blockPoses);

			if (le_fullBlockPoses.isEmpty()) {
				return;
			}

			final BlockPos nearestBlockPos = getNearestBlockPos(le_fullBlockPoses);
			final Block nearestBlock = mc.theWorld.getBlockState(nearestBlockPos).getBlock();
			final double diffX = mc.thePlayer.posX - (nearestBlockPos.getX() + 0.5);
			final double diffZ = mc.thePlayer.posZ - (nearestBlockPos.getZ() + 0.5);

			double nearestFaceZ = nearestBlockPos.getZ() + 0.5;
			double nearestFaceX = nearestBlockPos.getX() + 0.5;
			double nearestFaceY = nearestBlockPos.getY() + 0.5;

			if (Math.abs(diffX) > 0.5 || Math.abs(diffZ) > 0.5) {
				final int distanceCompare = Double.compare(Math.abs(diffX), Math.abs(diffZ));

				if(distanceCompare == -1) {
					if(diffZ <= 0) {
						nearestFace = EnumFacing.NORTH;
						nearestFaceZ = nearestBlockPos.getZ();
						nearestFaceX = nearestBlockPos.getX() + 0.5;
						nearestFaceY = nearestBlockPos.getY() + 0.5;
					} else {
						nearestFace = EnumFacing.SOUTH;
						nearestFaceZ = nearestBlockPos.getZ() + 1.0;
						nearestFaceX = nearestBlockPos.getX() + 0.5;
						nearestFaceY = nearestBlockPos.getY() + 0.5;
					}
				} else if(distanceCompare == 1) {
					if(diffX <= 0) {
						nearestFace = EnumFacing.WEST;
						nearestFaceZ = nearestBlockPos.getZ() + 0.5;
						nearestFaceX = nearestBlockPos.getX();
						nearestFaceY = nearestBlockPos.getY() + 0.5;
					} else {
						nearestFaceZ = nearestBlockPos.getZ() + 0.5;
						nearestFaceX = nearestBlockPos.getX() + 1.0;
						nearestFaceY = nearestBlockPos.getY() + 0.5;
						nearestFace = EnumFacing.EAST;
					}
				}
			} else {
				if (keepRotation.get()) {
					e.yaw = oldPlayerYaw;
					e.pitch = oldPlayerPitch;
				}
				nearestFace = null;
			}

			if (nearestFace != null) {
				rotate(e, nearestFaceZ, nearestFaceX, nearestFaceY, nearestBlock, nearestBlockPos);
			}

			oldPlayerX = mc.thePlayer.posX;
			oldPlayerY = mc.thePlayer.posY;
			oldPlayerZ = mc.thePlayer.posZ;
			le_fullBlockPoses.clear();
		}
	}

	private void rotate(UpdatePlayerEvent e, double nearestFaceZ, double nearestFaceX, double nearestFaceY, Block nearestBlock, BlockPos nearestBlockPos) {
		final AxisAlignedBB nearestBlockBB = nearestBlock.getCollisionBoundingBox(mc.theWorld, nearestBlockPos, nearestBlock.getDefaultState());
		final Vec3 eyesPos = new Vec3(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
		final Vec3 facePos = new Vec3(nearestFaceX, nearestFaceY, nearestFaceZ);

		final double faceDiffX = facePos.xCoord - eyesPos.xCoord;
		final double faceDiffY = facePos.yCoord - eyesPos.yCoord;
		final double faceDiffZ = facePos.zCoord - eyesPos.zCoord;

		final double rotationYaw = MathHelper.wrapAngleTo180_double(Math.toDegrees(Math.atan2(faceDiffZ, faceDiffX)) - 90F);
		final double rotationPitch = MathHelper.wrapAngleTo180_double((-Math.toDegrees(Math.atan2(faceDiffY, Math.sqrt(faceDiffX * faceDiffX + faceDiffZ * faceDiffZ)))));

		e.yaw = (float) rotationYaw;
		e.pitch = (float) rotationPitch;
		oldPlayerYaw = (float) rotationYaw;
		oldPlayerPitch = (float) rotationPitch;

		mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), nearestBlockPos, nearestFace, eyesPos);
		mc.thePlayer.swingItem();
	}

	private BlockPos getNearestBlockPos(ArrayList<BlockPos> le_fullBlockPoses) {
		BlockPos nearestBlockPos = null;
		for (BlockPos blockPos : le_fullBlockPoses) {
			final Block block = mc.theWorld.getBlockState(blockPos).getBlock();

			final double playerCurrentXdiff = Math.abs(mc.thePlayer.posX - (blockPos.getX() + 0.5));
			final double playerCurrentYdiff = Math.abs(mc.thePlayer.posY - (blockPos.getY() + 0.5));
			final double playerCurrentZdiff = Math.abs(mc.thePlayer.posZ - (blockPos.getZ() + 0.5));

			distanceCurrent = (playerCurrentXdiff * playerCurrentXdiff) + (playerCurrentYdiff * playerCurrentYdiff) + (playerCurrentZdiff * playerCurrentZdiff);

			if (distanceNearest > distanceCurrent || nearestBlockPos == null) {
				nearestBlockPos = blockPos;
				distanceNearest = distanceCurrent;
			}

		}
		return nearestBlockPos;
	}

	private ArrayList<BlockPos> removeAir(ArrayList<BlockPos> le_blockPoses) {
		ArrayList<BlockPos> le_fullBlockPoses = new ArrayList<BlockPos>();
		for (final BlockPos block_pos : le_blockPoses) {
			final Block block = mc.theWorld.getBlockState(block_pos).getBlock();

			if(block.isFullBlock()) le_fullBlockPoses.add(block_pos);
		}
		return le_fullBlockPoses;
	}

	private ArrayList<BlockPos> getAllBlockPoses(float r) {
		ArrayList<BlockPos> le_blockPoses = new ArrayList<BlockPos>();
		for (double x = (mc.thePlayer.posX - r); x < (mc.thePlayer.posX + r); x++) {
			for (double y = (mc.thePlayer.posY - r); y < mc.thePlayer.posY; y++) {
				for (double z = (mc.thePlayer.posZ - r); z < (mc.thePlayer.posZ + r); z++) {
					pos = new BlockPos(x, y, z);
					// final Block block = mc.theWorld.getBlockState(pos).getBlock();
					le_blockPoses.add(pos);
				}
			}
		}
		return le_blockPoses;
	}
}
