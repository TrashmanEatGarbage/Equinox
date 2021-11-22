package me.eonexe.equinox.features.modules.misc;

import me.eonexe.equinox.event.events.DeathEvent;
import me.eonexe.equinox.features.command.Command;
import me.eonexe.equinox.features.modules.Module;
import me.eonexe.equinox.features.modules.client.HUD;
import me.eonexe.equinox.util.EntityUtil;
import me.eonexe.equinox.util.TextUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

public class PancakesGPS extends Module {
    private static PancakesGPS instance;
    private EntityPlayer trackedPlayer;
    private int usedExp = 0;
    private int usedStacks = 0;

    public PancakesGPS() {
        super("PancakesGPS", "Tracks players in 1v1s.", Module.Category.MISC, true, false, false);
        instance = this;
    }

    public static PancakesGPS getInstance() {
        if (instance == null) {
            instance = new PancakesGPS();
        }
        return instance;
    }

    @Override
    public void onUpdate() {
        if (trackedPlayer == null) {
            trackedPlayer = EntityUtil.getClosestEnemy(1000.0);
        } else if (usedStacks != usedExp / 64) {
            usedStacks = usedExp / 64;
            Command.sendMessage(TextUtil.coloredString(trackedPlayer.getName() + " has used " + usedStacks + " stacks of XP!", HUD.getInstance().commandColor.getValue()));
        }
    }

    public void onSpawnEntity(Entity entity) {
        if (entity instanceof EntityExpBottle && Objects.equals(PancakesGPS.mc.world.getClosestPlayerToEntity(entity, 3.0), trackedPlayer)) {
            ++usedExp;
        }
    }

    @Override
    public void onDisable() {
        trackedPlayer = null;
        usedExp = 0;
        usedStacks = 0;
    }

    @SubscribeEvent
    public void onDeath( DeathEvent event) {
        if (event.player.equals(trackedPlayer)) {
            usedExp = 0;
            usedStacks = 0;
        }
    }

    @Override
    public String getDisplayInfo() {
        if (trackedPlayer != null) {
            return trackedPlayer.getName();
        }
        return null;
    }
}
