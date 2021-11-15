package me.eonexe.equinox.features.modules.player;

import java.awt.Color;
import me.eonexe.equinox.event.events.BlockEvent;
import me.eonexe.equinox.event.events.Render3DEvent;
import me.eonexe.equinox.event.events.PacketEvent.Send;
import me.eonexe.equinox.features.modules.Module;
import me.eonexe.equinox.features.setting.Setting;
import me.eonexe.equinox.util.BlockUtil;
import me.eonexe.equinox.util.RenderUtil;
import me.eonexe.equinox.util.Timer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class InstantMine extends Module {
    private BlockPos renderBlock;
    private BlockPos lastBlock;
    private boolean packetCancel = false;
    private final Timer breakTimer = new Timer();
    private EnumFacing direction;
    private boolean broke = false;
    private final Setting<Integer> delay = this.register(new Setting("Delay", 65, 0, 500));
    private final Setting<Boolean> picOnly = this.register(new Setting("PicOnly", true));

    public InstantMine() {
        super("InstantMine", "Instantly mine blocks", Category.MISC, true, false, false);
    }

    public void onRender3D(Render3DEvent event) {
        if (this.renderBlock != null) {
            Color color = new Color(93, 2, 198, 150);
            RenderUtil.drawBoxESP(this.renderBlock, color, false, color, 1.2F, true, true, 120, false);
        }

    }

    @SubscribeEvent
    public void onPacketSend(Send event) {
        if (event.getPacket() instanceof CPacketPlayerDigging) {
            CPacketPlayerDigging digPacket = (CPacketPlayerDigging)event.getPacket();
            if (digPacket.getAction() == Action.START_DESTROY_BLOCK && this.packetCancel) {
                event.setCanceled(true);
            }
        }

    }

    public void onTick() {
        if (this.renderBlock != null && this.breakTimer.passedMs((long)(Integer)this.delay.getValue())) {
            this.breakTimer.reset();
            if (!(Boolean)this.picOnly.getValue() || mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.DIAMOND_PICKAXE) {
                mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, this.renderBlock, this.direction));
            }
        } else {
            try {
                mc.playerController.blockHitDelay = 0;
            } catch (Exception var2) {
            }

        }
    }

    @SubscribeEvent
    public void onBlockEvent(BlockEvent event) {
        if (!Speedmine.fullNullCheck()) {
            if (event.getStage() == 3 && Speedmine.mc.playerController.curBlockDamageMP > 0.1F) {
                Speedmine.mc.playerController.isHittingBlock = true;
            }

            if (event.getStage() == 4 && BlockUtil.canBreak(event.pos)) {
                Speedmine.mc.playerController.isHittingBlock = false;
                if (this.canBreak(event.pos)) {
                    if (this.lastBlock != null && event.pos.getX() == this.lastBlock.getX() && event.pos.getY() == this.lastBlock.getY() && event.pos.getZ() == this.lastBlock.getZ()) {
                        this.packetCancel = true;
                    } else {
                        this.packetCancel = false;
                        mc.player.swingArm(EnumHand.MAIN_HAND);
                        mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.START_DESTROY_BLOCK, event.pos, event.facing));
                        this.packetCancel = true;
                    }

                    mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, event.pos, event.facing));
                    this.renderBlock = event.pos;
                    this.lastBlock = event.pos;
                    this.direction = event.facing;
                    event.setCanceled(true);
                }
            }

        }
    }

    private boolean canBreak(BlockPos pos) {
        IBlockState blockState = mc.world.getBlockState(pos);
        Block block = blockState.getBlock();
        return block.getBlockHardness(blockState, mc.world, pos) != -1.0F;
    }
}
