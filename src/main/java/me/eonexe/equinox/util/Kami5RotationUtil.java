package me.eonexe.equinox.util;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.eonexe.equinox.event.events.PacketEvent;
import me.eonexe.equinox.mixin.mixins.ICPacketPlayer;



public class Kami5RotationUtil
        implements IMinecraft
{
    public static Kami5RotationUtil INSTANCE;
    public boolean rotating = false;
    public float yaw;
    public float pitch;
    public float rotatedYaw;
    public float rotatedPitch;

    public Kami5RotationUtil() {
        MinecraftForge.EVENT_BUS.register(this);
    }


    @SubscribeEvent
    public void onPacket(PacketEvent event) {
        if (NullUtils.nullCheck())
            return;
        if (event.getTime() == PacketEvent.Time.Send &&
                this.rotating && event.getPacket() instanceof net.minecraft.network.play.client.CPacketPlayer) {
            ((ICPacketPlayer)event.getPacket()).setYaw(this.yaw);
            ((ICPacketPlayer)event.getPacket()).setPitch(this.pitch);
            this.rotatedYaw = this.yaw;
            this.rotatedPitch = this.pitch;
        }
    }


    public void rotate(Vec3d toRotate) {
        float[] rotations = getNeededRotations(toRotate);
        this.yaw = rotations[0];
        this.pitch = rotations[1];
    }

    public void resetRotations() {
        this.yaw = mc.player.rotationYaw;
        this.pitch = mc.player.rotationPitch;
    }


    public static float[] getNeededRotations(Vec3d vec) {
        Vec3d eyesPos = getEyesPos();

        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;

        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
        float pitch = (float)-Math.toDegrees(Math.atan2(diffY, diffXZ));

        return new float[] { mc.player.rotationYaw +
                MathHelper.wrapDegrees(yaw - mc.player.rotationYaw), mc.player.rotationPitch +
                MathHelper.wrapDegrees(pitch - mc.player.rotationPitch) };
    }


    public static Vec3d getEyesPos() {
        return new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ);
    }
}