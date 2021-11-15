package me.eonexe.equinox.features.modules.render;

import me.eonexe.equinox.features.modules.Module;
import net.minecraft.entity.player.EntityPlayer;

public class AntiPlayerSwing extends Module {
    public AntiPlayerSwing() {
        super("AntiPlayerSwing", "kcp", Module.Category.RENDER, false, false, false);
    }

    @Override
    public void onTick() {
        try {
            for(EntityPlayer player : mc.world.playerEntities) {
                player.limbSwing = 0;
                player.limbSwingAmount = 0;
                player.prevLimbSwingAmount = 0;
            }
        } catch (Exception ignore) {}
    }
}
