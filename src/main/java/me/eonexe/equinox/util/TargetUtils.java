package me.eonexe.equinox.util;

import java.util.Comparator;

import me.eonexe.equinox.Equinox;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import me.eonexe.equinox.manager.FriendManager;
import me.eonexe.equinox.util.IMinecraft;


public class TargetUtils
        implements IMinecraft
{
    public static EntityLivingBase getTarget(double targetRange) {
        return (EntityLivingBase) mc.world.getLoadedEntityList().stream()
                .filter(entity -> entity instanceof net.minecraft.entity.player.EntityPlayer)
                .filter(TargetUtils::isAlive)
                .filter(entity -> (entity.getEntityId() != mc.player.getEntityId()))
                .filter(entity -> !Equinox.friendManager.isFriend(entity.getName()))
                .filter(entity -> (mc.player.getDistance(entity) <= targetRange))
                .min(Comparator.comparingDouble(entity -> mc.player.getDistance(entity)))
                .orElse(null);
    }


    public static boolean isAlive(Entity entity) {
        return (isLiving(entity) && !entity.isDead && ((EntityLivingBase)entity).getHealth() > 0.0F);
    }

    public static boolean isLiving(Entity entity) {
        return entity instanceof EntityLivingBase;
    }
}