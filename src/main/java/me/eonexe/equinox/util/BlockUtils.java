package me.eonexe.equinox.util;

import java.util.Iterator;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.Block;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockAir;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.ArrayList;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.util.math.BlockPos;
import java.util.List;

public class BlockUtils implements IMinecraft {
    static List<BlockPos> tickCache;

    public BlockUtils() {
        MinecraftForge.EVENT_BUS.register((Object) this);
    }

    @SubscribeEvent
    public void onUpdate(final TickEvent.ClientTickEvent event) {
        BlockUtils.tickCache = new ArrayList<BlockPos>();
    }

    public static boolean placeBlock(final BlockPos pos, final boolean sneak) {
        final Block block = mc.world.getBlockState(pos).getBlock();
        if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid)) {
            return false;
        }
        final EnumFacing side = getPlaceableSide(pos);
        if (side == null) {
            return false;
        }
        final BlockPos neighbour = pos.offset(side);
        final EnumFacing opposite = side.getOpposite();
        final Vec3d hitVec = new Vec3d((Vec3i) neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        if (!mc.player.isSneaking()) {
            mc.getConnection().sendPacket((Packet) new CPacketEntityAction((Entity) mc.player, CPacketEntityAction.Action.START_SNEAKING));
        }
        final EnumActionResult action = mc.playerController.processRightClickBlock(mc.player, mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
        mc.getConnection().sendPacket((Packet) new CPacketEntityAction((Entity) mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        BlockUtils.tickCache.add(pos);
        return action == EnumActionResult.SUCCESS;
    }

    public static boolean placeBlock(final BlockPos pos, final boolean sneak, final EnumHand hand) {
        final Block block = mc.world.getBlockState(pos).getBlock();
        if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid)) {
            return false;
        }
        final EnumFacing side = getPlaceableSide(pos);
        if (side == null) {
            return false;
        }
        final BlockPos neighbour = pos.offset(side);
        final EnumFacing opposite = side.getOpposite();
        final Vec3d hitVec = new Vec3d((Vec3i) neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        if (!mc.player.isSneaking()) {
            mc.getConnection().sendPacket((Packet) new CPacketEntityAction((Entity) mc.player, CPacketEntityAction.Action.START_SNEAKING));
        }
        final EnumActionResult action = mc.playerController.processRightClickBlock(mc.player, mc.world, neighbour, opposite, hitVec, hand);
        mc.getConnection().sendPacket((Packet) new CPacketEntityAction((Entity) mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        BlockUtils.tickCache.add(pos);
        return action == EnumActionResult.SUCCESS;
    }

    public static void breakBlock(final BlockPos p) {
        final Block block = mc.world.getBlockState(p).getBlock();
        final EnumFacing side = getPlaceableSide(p);
        mc.player.connection.sendPacket((Packet) new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, p, side));
        mc.player.connection.sendPacket((Packet) new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, p, side));
        mc.player.swingArm(EnumHand.MAIN_HAND);
    }

    public static EnumFacing getPlaceableSide(final BlockPos pos) {
        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbour = pos.offset(side);
            if (mc.world.getBlockState(neighbour).getBlock().canCollideCheck(mc.world.getBlockState(neighbour), false) || BlockUtils.tickCache.contains(neighbour)) {
                final IBlockState blockState = mc.world.getBlockState(neighbour);
                if (!blockState.getMaterial().isReplaceable()) {
                    return side;
                }
            }
        }
        return null;
    }

    public static EnumFacing getPlaceableSideH(final BlockPos pos) {
        for (final EnumFacing side : EnumFacing.HORIZONTALS) {
            final BlockPos neighbour = pos.offset(side);
            if (mc.world.getBlockState(neighbour).getBlock().canCollideCheck(mc.world.getBlockState(neighbour), false)) {
                final IBlockState blockState = mc.world.getBlockState(neighbour);
                if (!blockState.getMaterial().isReplaceable()) {
                    return side;
                }
            }
        }
        return null;
    }

    public static float[] getNeededRotations(final Vec3d vec) {
        final Vec3d eyesPos = getEyesPos();
        final double diffX = vec.x - eyesPos.x;
        final double diffY = vec.y - eyesPos.y;
        final double diffZ = vec.z - eyesPos.z;
        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{mc.player.rotationYaw + MathHelper.wrapDegrees(yaw - mc.player.rotationYaw), mc.player.rotationPitch + MathHelper.wrapDegrees(pitch - mc.player.rotationPitch)};
    }

    public static Vec3d getEyesPos() {
        return new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ);
    }

    public static List<BlockPos> getSphere(final double range, final BlockPos pos, final boolean sphere, final boolean hollow) {
        final List<BlockPos> circleblocks = new ArrayList<BlockPos>();
        final int cx = pos.getX();
        final int cy = pos.getY();
        final int cz = pos.getZ();
        for (int x = cx - (int) range; x <= cx + range; ++x) {
            for (int z = cz - (int) range; z <= cz + range; ++z) {
                for (int y = sphere ? (cy - (int) range) : cy; y < (sphere ? (cy + range) : (cy + range)); ++y) {
                    final double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0);
                    if (dist < range * range && (!hollow || dist >= (range - 1.0) * (range - 1.0))) {
                        final BlockPos l = new BlockPos(x, y, z);
                        circleblocks.add(l);
                    }
                }
            }
        }
        return circleblocks;
    }

    public static boolean canPlaceBlock(final BlockPos pos) {
        boolean allow = true;
        if (!mc.world.getBlockState(pos).getMaterial().isReplaceable()) {
            allow = false;
        }
        final Iterator<Entity> iterator = mc.world.getEntitiesWithinAABB((Class) Entity.class, new AxisAlignedBB(pos)).iterator();
        if (iterator.hasNext()) {
            final Entity entity = iterator.next();
            allow = false;
        }
        return allow;
    }

    static {
        BlockUtils.tickCache = new ArrayList<BlockPos>();
        new BlockUtils();
    }
}