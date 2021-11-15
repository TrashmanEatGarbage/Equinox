/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.entity.Entity
 *  net.minecraft.world.World
 */
package me.eonexe.equinox.features.modules.misc;

import com.mojang.authlib.GameProfile;
import java.util.UUID;

import me.eonexe.equinox.features.modules.Module;
import me.eonexe.equinox.features.setting.Setting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class NewFakePlayer
        extends Module {
    private static NewFakePlayer INSTANCE = new NewFakePlayer();
    public Setting<Boolean> copyInv = this.register(new Setting<Boolean>("Copy Inventory", true));
    public Setting<Boolean> moving = this.register(new Setting<Boolean>("Moving", false));
    public Setting<Integer> motion = this.register(new Setting<Object>("Motion", Integer.valueOf(2), Integer.valueOf(-5), Integer.valueOf(5), v -> this.moving.getValue()));
    private final int entityId = -420;

    public NewFakePlayer() {
        super("NewFakePlayer", "Spawns a NewFakePlayer for testing.", Module.Category.MISC, false , false, false);
        this.setInstance();
    }

    public static NewFakePlayer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NewFakePlayer();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onDisable() {
        if (NewFakePlayer.fullNullCheck()) {
            return;
        }
        NewFakePlayer.mc.world.removeEntityFromWorld(-420);
    }

    @Override
    public void onUpdate() {
        if (this.moving.getValue().booleanValue()) {
            if (NewFakePlayer.fullNullCheck()) {
                return;
            }
            GameProfile profile = new GameProfile(UUID.fromString("12cbdfad-33b7-4c07-aeac-01766e609482"), "NewFakePlayer");
            EntityOtherPlayerMP player = new EntityOtherPlayerMP((World)NewFakePlayer.mc.world, profile);
            player.setLocationAndAngles(NewFakePlayer.mc.player.posX + player.motionX + (double)this.motion.getValue().intValue(), NewFakePlayer.mc.player.posY + player.motionY, NewFakePlayer.mc.player.posZ + player.motionZ + (double)this.motion.getValue().intValue(), 90.0f, 90.0f);
            player.rotationYawHead = NewFakePlayer.mc.player.rotationYawHead;
            if (this.copyInv.getValue().booleanValue()) {
                player.inventory.copyInventory(NewFakePlayer.mc.player.inventory);
            }

            NewFakePlayer.mc.world.addEntityToWorld(-420, (Entity)player);
            return;
        }
    }

    @Override
    public void onEnable() {
        if (!this.moving.getValue().booleanValue()) {
            if (NewFakePlayer.fullNullCheck()) {
                return;
            }
            GameProfile profile = new GameProfile(UUID.fromString("12cbdfad-33b7-4c07-aeac-01766e609482"), "NewFakePlayer");
            EntityOtherPlayerMP player = new EntityOtherPlayerMP((World)NewFakePlayer.mc.world, profile);
            player.copyLocationAndAnglesFrom((Entity)NewFakePlayer.mc.player);
            player.rotationYawHead = NewFakePlayer.mc.player.rotationYawHead;
            if (this.copyInv.getValue().booleanValue()) {
                player.inventory.copyInventory(NewFakePlayer.mc.player.inventory);
            }
            NewFakePlayer.mc.world.addEntityToWorld(-420, (Entity)player);
            return;
        }
    }
}
