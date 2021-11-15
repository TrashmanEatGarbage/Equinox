package me.eonexe.equinox.features.modules.misc;

import com.mojang.authlib.GameProfile;
import java.util.UUID;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.eonexe.equinox.features.command.Command;
import me.eonexe.equinox.features.modules.Module;
import me.eonexe.equinox.features.setting.Setting;
import me.eonexe.equinox.util.NullUtils;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class FakePlayer2
        extends Module {
    String playerName = "stinky";
    int eID = -696420;
    EntityPlayer fakePlayer;
    public final Setting<Boolean> move = this.register(new Setting<Boolean>("Move", true));
    public final Setting<Integer> dist = this.register(new Setting<Integer>("Distance", 5, 0, 15));
    private final Setting<Boolean> debug = this.register(new Setting<Boolean>("Debug", false));

    public FakePlayer2() {
        super("Fake Player", "", Module.Category.MISC, true, false, false);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (NullUtils.nullCheck()) {
            return;
        }
        GameProfile profile = new GameProfile(UUID.fromString("2da1acb3-1a8c-471f-a877-43f13cf37e6a"), this.playerName);
        this.fakePlayer = new EntityOtherPlayerMP((World)FakePlayer2.mc.world, profile);
        this.fakePlayer.copyLocationAndAnglesFrom((Entity)FakePlayer2.mc.player);
        this.fakePlayer.setHealth(FakePlayer2.mc.player.getHealth() + FakePlayer2.mc.player.getAbsorptionAmount());
        this.fakePlayer.inventory = FakePlayer2.mc.player.inventory;
        this.fakePlayer.stepHeight = 2.0f;
        FakePlayer2.mc.world.spawnEntity((Entity)this.fakePlayer);
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (NullUtils.nullCheck() || this.fakePlayer == null) {
            return;
        }
        if (this.move.getValue().booleanValue() && FakePlayer2.mc.objectMouseOver != null && FakePlayer2.mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
            this.fakePlayer.setPositionAndRotation((double)FakePlayer2.mc.objectMouseOver.getBlockPos().getX() + 0.5, (double)this.getNearestGround(FakePlayer2.mc.objectMouseOver.getBlockPos()).getY(), (double)FakePlayer2.mc.objectMouseOver.getBlockPos().getZ() + 0.5, FakePlayer2.mc.player.rotationYaw, FakePlayer2.mc.player.rotationPitch);
        }
        if (this.debug.getValue().booleanValue()) {
            Command.sendMessage(ChatFormatting.YELLOW + "Moved fake player");
        }
    }

    BlockPos getNearestGround(BlockPos pos) {
        for (int i = 0; i <= 10; ++i) {
            if (FakePlayer2.mc.world.getBlockState(pos).getMaterial().isSolid()) {
                pos = pos.add(0, 1, 0);
            }
            if (!FakePlayer2.mc.world.getBlockState(pos.add(0, 2, 0)).getMaterial().isSolid()) continue;
            pos = pos.add(0, 2, 0);
        }
        return pos;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (this.fakePlayer != null) {
            FakePlayer2.mc.world.removeEntity(this.fakePlayer);
            this.fakePlayer = null;
        }
    }
}

