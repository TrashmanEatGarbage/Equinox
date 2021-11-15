package me.eonexe.equinox.features.modules.combat;

import java.util.ArrayList;
import java.util.List;

import me.eonexe.equinox.event.events.PacketEvent;
import me.eonexe.equinox.features.modules.Module;
import me.eonexe.equinox.features.setting.Setting;
import me.eonexe.equinox.util.BlockUtils;
import me.eonexe.equinox.util.InventoryUtils;
import me.eonexe.equinox.util.Kami5Timer;
import me.eonexe.equinox.util.NullUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Surround
        extends Module {
    Kami5Timer timer = new Kami5Timer();
    public final Setting<Integer> delay = this.register(new Setting<Integer>("Delay", 0, 0, 1000));
    public final Setting<Integer> blocksPerTick = this.register(new Setting<Integer>("BPT", 20, 1, 50));
    public final Setting<Integer> retryAmount = this.register(new Setting<Integer>("Retry Amount", 20, 1, 50));
    public final Setting<Boolean> dynamic = this.register(new Setting<Boolean>("Dynamic", true));
    public final Setting<Boolean> antiPhase = this.register(new Setting<Boolean>("Anti Phase", true));
    public final Setting<Boolean> predict = this.register(new Setting<Boolean>("Predict", false));
    public final Setting<Boolean> jumpDisable = this.register(new Setting<Boolean>("Jump Disable", true));

    //Value<Color> activeFillColor = new ValueBuilder().withDescriptor("Active Fill Color").withValue(new Color(0, 200, 12, 20)).register(this);
    //Value<Color> activeLineColor = new ValueBuilder().withDescriptor("Active Line Color").withValue(new Color(0, 200, 12, 255)).register(this);
    double startY = 0.0;
    List<BlockPos> activeBlocks = new ArrayList<BlockPos>();
    boolean shouldPredict = false;

    public Surround() {
        super("Surround", "pov you paste", Module.Category.COMBAT, true, false, false);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        this.shouldPredict = true;
        if (this.jumpDisable.getValue().booleanValue() && (!Surround.mc.player.onGround || Surround.mc.player.posY != this.startY)) {
            this.setEnabled(false);
            return;
        }
        if (this.timer.isPassed()) {
            this.activeBlocks.clear();
            boolean switched = false;
            int oldSlot = Surround.mc.player.inventory.currentItem;
            int blockSlot = this.getSlot();
            if (blockSlot == -1) {
                this.setEnabled(false);
                return;
            }
            int blocksInTick = 0;
            block0: for (int i = 0; i < this.retryAmount.getValue().intValue(); ++i) {
                for (BlockPos pos : this.getOffsets()) {
                    if (blocksInTick > this.blocksPerTick.getValue().intValue()) continue block0;
                    if (!this.canPlaceBlock(pos)) continue;
                    this.activeBlocks.add(pos);
                    if (!switched) {
                        InventoryUtils.switchToSlotGhost(blockSlot);
                        switched = true;
                    }
                    BlockUtils.placeBlock(pos, true);
                    ++blocksInTick;
                }
            }
            if (switched) {
                InventoryUtils.switchToSlotGhost(oldSlot);
            }
            this.timer.resetDelay();
        }
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof SPacketBlockChange && this.predict.getValue().booleanValue()) {
            SPacketBlockChange packet = (SPacketBlockChange)event.getPacket();
            for (BlockPos pos : this.getOffsets()) {
                if (!this.shouldPredict || !pos.equals((Object)packet.getBlockPosition()) || packet.getBlockState().getBlock() != Blocks.AIR) continue;
                int oldSlot = Surround.mc.player.inventory.currentItem;
                int blockSlot = this.getSlot();
                if (blockSlot == -1) {
                    return;
                }
                InventoryUtils.switchToSlotGhost(blockSlot);
                BlockUtils.placeBlock(pos, true);
                InventoryUtils.switchToSlotGhost(oldSlot);
                this.shouldPredict = false;
                break;
            }
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        for (BlockPos pos : this.activeBlocks) {
            //RenderUtil.renderBB(7, new AxisAlignedBB(pos), this.activeFillColor.getValue(), this.activeFillColor.getValue());
            //RenderUtil.renderBB(3, new AxisAlignedBB(pos), this.activeLineColor.getValue(), this.activeLineColor.getValue());
        }
    }

    @Override
    public void onEnable() {
        if (NullUtils.nullCheck()) {
            return;
        }
        super.onEnable();
        this.startY = Surround.mc.player.posY;
    }

    int getSlot() {
        int slot = -1;
        slot = InventoryUtils.getHotbarItemSlot(Item.getItemFromBlock((Block)Blocks.OBSIDIAN));
        if (slot == -1) {
            slot = InventoryUtils.getHotbarItemSlot(Item.getItemFromBlock((Block)Blocks.ENDER_CHEST));
        }
        return slot;
    }

    boolean canPlaceBlock(BlockPos pos) {
        boolean allow = true;
        if (!Surround.mc.world.getBlockState(pos).getMaterial().isReplaceable()) {
            allow = false;
        }
        for (Entity entity : Surround.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos))) {
            if (!(entity instanceof EntityPlayer)) continue;
            allow = false;
            break;
        }
        return allow;
    }

    List<BlockPos> getOffsets() {
        BlockPos playerPos = this.getPlayerPos();
        ArrayList<BlockPos> offsets = new ArrayList<BlockPos>();
        if (this.dynamic.getValue().booleanValue()) {
            int z;
            int x;
            double decimalX = Math.abs(Surround.mc.player.posX) - Math.floor(Math.abs(Surround.mc.player.posX));
            double decimalZ = Math.abs(Surround.mc.player.posZ) - Math.floor(Math.abs(Surround.mc.player.posZ));
            int offX = this.calcOffset(decimalX);
            int offZ = this.calcOffset(decimalZ);
            int lengthXPos = this.calcLength(decimalX, false);
            int lengthXNeg = this.calcLength(decimalX, true);
            int lengthZPos = this.calcLength(decimalZ, false);
            int lengthZNeg = this.calcLength(decimalZ, true);
            ArrayList<BlockPos> tempOffsets = new ArrayList<BlockPos>();
            offsets.addAll(this.getOverlapPos());
            for (x = 1; x < lengthXPos + 1; ++x) {
                tempOffsets.add(this.addToPlayer(playerPos, x, 0.0, 1 + lengthZPos));
                tempOffsets.add(this.addToPlayer(playerPos, x, 0.0, -(1 + lengthZNeg)));
            }
            for (x = 0; x <= lengthXNeg; ++x) {
                tempOffsets.add(this.addToPlayer(playerPos, -x, 0.0, 1 + lengthZPos));
                tempOffsets.add(this.addToPlayer(playerPos, -x, 0.0, -(1 + lengthZNeg)));
            }
            for (z = 1; z < lengthZPos + 1; ++z) {
                tempOffsets.add(this.addToPlayer(playerPos, 1 + lengthXPos, 0.0, z));
                tempOffsets.add(this.addToPlayer(playerPos, -(1 + lengthXNeg), 0.0, z));
            }
            for (z = 0; z <= lengthZNeg; ++z) {
                tempOffsets.add(this.addToPlayer(playerPos, 1 + lengthXPos, 0.0, -z));
                tempOffsets.add(this.addToPlayer(playerPos, -(1 + lengthXNeg), 0.0, -z));
            }
            for (BlockPos pos : tempOffsets) {
                offsets.add(pos.add(0, -1, 0));
                offsets.add(pos);
            }
        } else {
            offsets.add(playerPos.add(0, -1, 0));
            for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                offsets.add(playerPos.add(facing.getXOffset(), -1, facing.getZOffset()));
                offsets.add(playerPos.add(facing.getXOffset(), 0, facing.getZOffset()));
            }
        }
        return offsets;
    }

    BlockPos addToPlayer(BlockPos playerPos, double x, double y, double z) {
        if (playerPos.getX() < 0) {
            x = -x;
        }
        if (playerPos.getY() < 0) {
            y = -y;
        }
        if (playerPos.getZ() < 0) {
            z = -z;
        }
        return playerPos.add(x, y, z);
    }

    int calcLength(double decimal, boolean negative) {
        if (negative) {
            return decimal <= 0.3 ? 1 : 0;
        }
        return decimal >= 0.7 ? 1 : 0;
    }

    boolean isOverlapping(int offsetX, int offsetZ) {
        boolean overlapping = false;
        double decimalX = Surround.mc.player.posX - Math.floor(Surround.mc.player.posX);
        decimalX = Math.abs(decimalX);
        double decimalZ = Surround.mc.player.posZ - Math.floor(Surround.mc.player.posZ);
        decimalZ = Math.abs(decimalZ);
        if (offsetX > 0 && decimalX > 0.7) {
            overlapping = true;
        }
        if (offsetX < 0 && decimalX < 0.3) {
            overlapping = true;
        }
        if (offsetZ > 0 && decimalZ >= 0.7) {
            overlapping = true;
        }
        if (offsetZ < 0 && decimalZ < 0.3) {
            overlapping = true;
        }
        return overlapping;
    }

    List<BlockPos> getOverlapPos() {
        ArrayList<BlockPos> positions = new ArrayList<BlockPos>();
        double decimalX = Surround.mc.player.posX - Math.floor(Surround.mc.player.posX);
        double decimalZ = Surround.mc.player.posZ - Math.floor(Surround.mc.player.posZ);
        int offX = this.calcOffset(decimalX);
        int offZ = this.calcOffset(decimalZ);
        positions.add(this.getPlayerPos());
        for (int x = 0; x <= Math.abs(offX); ++x) {
            for (int z = 0; z <= Math.abs(offZ); ++z) {
                int properX = x * offX;
                int properZ = z * offZ;
                positions.add(this.getPlayerPos().add(properX, -1, properZ));
            }
        }
        return positions;
    }

    int calcOffset(double dec) {
        return dec >= 0.7 ? 1 : (dec <= 0.3 ? -1 : 0);
    }

    BlockPos getPlayerPos() {
        double decimalPoint = Surround.mc.player.posY - Math.floor(Surround.mc.player.posY);
        return new BlockPos(Surround.mc.player.posX, decimalPoint > 0.8 ? Math.floor(Surround.mc.player.posY) + 1.0 : Math.floor(Surround.mc.player.posY), Surround.mc.player.posZ);
    }
}

