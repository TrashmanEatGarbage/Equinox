package me.eonexe.equinox.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.awt.*;

/**
 * Created by 086 on 11/11/2017.
 */
public class Wrapper {

    //private static FontRenderer fontRenderer;
    static final Minecraft mc = Minecraft.getMinecraft();

    public static void init() {
        //fontRenderer = (FontRenderer) new CFontRenderer(new Font("Segoe UI", Font.PLAIN, 19), true, false);
    //    fontRenderer = KamiGUI.fontRenderer;
    }
    public static Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }
    public static EntityPlayerSP getPlayer() {
        return getMinecraft().player;
    }
    public static World getWorld() {
        return getMinecraft().world;
    }
    public static int getKey(String keyname){
        return Keyboard.getKeyIndex(keyname.toUpperCase());
    }

    //public static FontRenderer getFontRenderer() {
    //    return fontRenderer;
    //}
}