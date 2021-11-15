package me.eonexe.equinox.mixin.mixins;

import net.minecraft.network.play.client.CPacketPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({CPacketPlayer.class})
public interface ICPacketPlayer {
    @Accessor("yaw")
    void setYaw(float paramFloat);

    @Accessor("pitch")
    void setPitch(float paramFloat);

    @Accessor("x")
    void setX(double paramDouble);

    @Accessor("y")
    void setY(double paramDouble);

    @Accessor("z")
    void setZ(double paramDouble);

    @Accessor("onGround")
    void setOnGround(boolean paramBoolean);
}