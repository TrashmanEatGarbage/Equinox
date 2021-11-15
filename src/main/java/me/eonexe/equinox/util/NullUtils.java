package me.eonexe.equinox.util;

public class NullUtils
        implements IMinecraft {
    public static boolean nullCheck() {
        return NullUtils.mc.player == null || NullUtils.mc.world == null || NullUtils.mc.playerController == null;
    }
}

