package me.eonexe.equinox.features.modules.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import me.eonexe.equinox.features.modules.Module;
import me.eonexe.equinox.features.setting.Setting;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import me.eonexe.equinox.event.events.PacketEvent;
import me.eonexe.equinox.util.NullUtils;
import me.eonexe.equinox.util.Timer;
import me.eonexe.equinox.util.InventoryUtils;
import me.eonexe.equinox.util.BlockUtils;
import me.eonexe.equinox.util.CrystalUtil;

public class NewSurround extends Module {

    Timer timer = new Timer();
    /*
    Value<Number> delay = new ValueBuilder().withDescriptor("Delay").withValue(0).withRange(0, 1000).register(this);
    Value<Number> blocksPerTick = new ValueBuilder().withDescriptor("BPT").withValue(20).withRange(1, 50).register(this);
    Value<Number> retryAmount = new ValueBuilder().withDescriptor("Retry Amount").withValue(20).withRange(1, 50).register(this);
    Value<Boolean> multiThread = new ValueBuilder().withDescriptor("Threading").withValue(false).register(this);
    Value<String> mode = new ValueBuilder().withDescriptor("Mode").withValue("Dynamic").withModes("Dynamic", "Normal", "Calc").register(this);
    Value<Number> allowHealth = new ValueBuilder().withDescriptor("Allow Health").withValue(5).withRange(0, 36).register(this);
    Value<Number> calcRange = new ValueBuilder().withDescriptor("Calc Range").withValue(3).withRange(0, 10).register(this);
    Value<Number> wallRange = new ValueBuilder().withDescriptor("Wall Range").withValue(3).withRange(0, 10).register(this);
    Value<Number> raytraceHits = new ValueBuilder().withDescriptor("Raytrace Hits").withValue(2).withRange(1, 9).register(this);
    Value<Number> shrinkFactor = new ValueBuilder().withDescriptor("Shrink Factor").withValue(0.3).withRange(0d, 1d).register(this);
    Value<Boolean> oneThirteen = new ValueBuilder().withDescriptor("1.13", "oneThirteen").withValue(false).register(this);
    Value<Boolean> antiPhase = new ValueBuilder().withDescriptor("Clip Extend").withValue(true).register(this);
    Value<Number> clipTries = new ValueBuilder().withDescriptor("Clip Tries").withValue(2).withRange(1, 10).register(this);
    Value<Boolean> predict = new ValueBuilder().withDescriptor("Predict").withValue(false).register(this);
    Value<Boolean> center = new ValueBuilder().withDescriptor("Center").withValue(false).register(this);
    Value<Boolean> centerBounds = new ValueBuilder().withDescriptor("Center Bounds").withValue(false).register(this);
    Value<Boolean> jumpDisable = new ValueBuilder().withDescriptor("Jump Disable").withValue(true).register(this);
     */

    private final Setting<Integer> delay = this.register (new Setting<Object>("Delay", 0 , 0 , 1000));
    private final Setting<Integer> blocksPerTick = this.register (new Setting <Object>("BPT", 20, 1 , 50));
    private final Setting<Integer>retryAmount = this.register (new Setting <Object> ("Retry Amount",20,1,50));
    private final Setting<Boolean> multiThread = this.register (new Setting <Object>("Threading", false));
    private final Setting<thing> mode = this.register (new Setting <Object>("Mode",thing.Dynamic));
    private final Setting<Integer> allowHealth = this.register (new Setting <Object>("Allow Health", 5 , 0 , 36));
    private final Setting<Integer> calcRange = this.register (new Setting <Object>("Calc Range",3, 9 ,10));
    private final Setting<Integer> wallRange = this.register (new Setting <Object>("Wall Range", 3 ,0 , 10));
    private final Setting<Integer> raytraceHits = this.register (new Setting <Object>("Raytrace Hits",2,1,9));
    private final Setting<Float> shrinkFactor = this.register (new Setting <Object>("Shrink Factor",0.3,0,1));
    private final Setting<Boolean> oneThirteen = this.register (new Setting <Object>("1.13",false));
    private final Setting<Boolean> predict = this.register (new Setting <Object>("Predict",false));
    private final Setting<Boolean> center = this.register (new Setting <Object>("Center",false));
    private final Setting<Boolean> centerBounds = this.register (new Setting <Object>("Center Bounds",false));
    private final Setting<Boolean> jumpDisable = this.register (new Setting <Object>("Jump Disable",true));

    double startY = 0.0;
    java.util.List<BlockPos> activeBlocks = new ArrayList<BlockPos>();
    boolean shouldPredict = false;
    java.util.List<BlockPos> offsets = new ArrayList<BlockPos>();

    public NewSurround() {
        super("Surround", "pov you paste", Module.Category.COMBAT, true, false, false);
        this.timer.setDelay((delay.getValue()).longValue());
    }


    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        this.shouldPredict = true;
        if (!(!this.jumpDisable.getValue().booleanValue() || Surround.mc.player.isInLava() || Surround.mc.player.isInWater() || Surround.mc.player.onGround && Surround.mc.player.posY == this.startY)) {
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
            new Thread(() -> {
                this.offsets = this.getOffsets();
            }).start();
            int blocksInTick = 0;
            block0: for (int i = 0; i < this.retryAmount.getValue().intValue(); ++i) {
                if (!this.multiThread.getValue().booleanValue()) {
                    this.offsets = this.getOffsets();
                }
                for (BlockPos pos : this.offsets) {
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


    @Override
    public void onEnable() {
        super.onEnable();
        if (NullUtils.nullCheck()) {
            return;
        }
        if (this.center.getValue().booleanValue()) {
            double x = (double)Surround.mc.player.getPosition().getX() + 0.5;
            double z = (double)Surround.mc.player.getPosition().getZ() + 0.5;
            mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(x, Surround.mc.player.posY, z, Surround.mc.player.onGround));
            if (this.centerBounds.getValue().booleanValue()) {
                mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(x, 1337.0, z, Surround.mc.player.onGround));
            }
            Surround.mc.player.setPosition(x, Surround.mc.player.posY, z);
        }
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

    java.util.List<BlockPos> getOffsets() {
        BlockPos playerPos = this.getPlayerPos();
        ArrayList<BlockPos> offsets = new ArrayList<BlockPos>();
        switch (this.mode.getValue()) {
            case Dynamic: {
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
                for (BlockPos pos2 : tempOffsets) {
                    if (!this.hasSurroundingBlock(pos2)) {
                        offsets.add(pos2.add(0, -1, 0));
                    }
                    offsets.add(pos2);
                }
                break;
            }
            case Calc: {
                java.util.List<BlockPos> positions = CrystalUtil.getAvailablePositions(this.calcRange.getValue().doubleValue(), this.wallRange.getValue().doubleValue(), this.oneThirteen.getValue(), false, this.raytraceHits.getValue().intValue(), this.shrinkFactor.getValue().floatValue());
                positions.sort(Comparator.comparingDouble(pos -> Surround.mc.player.getDistance((double)pos.getX(), (double)pos.getY(), (double)pos.getZ())));
                for (BlockPos pos3 : positions) {
                    double damage = CrystalUtil.calculateDamage((double)pos3.getX() + 0.5, pos3.getY() + 1, (double)pos3.getZ() + 0.5, (Entity)Surround.mc.player, 0.0);
                    if (!(damage > this.allowHealth.getValue().doubleValue())) continue;
                    offsets.add(pos3.add(0, 1, 0));
                }
                break;
            }
            case Normal: {
                offsets.add(playerPos.add(0, -1, 0));
                for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                    if (!this.hasSurroundingBlock(playerPos.add(facing.getXOffset(), 0, facing.getZOffset()))) {
                        offsets.add(playerPos.add(facing.getXOffset(), -1, facing.getZOffset()));
                    }
                    offsets.add(playerPos.add(facing.getXOffset(), 0, facing.getZOffset()));
                }
                break;
            }
        }
        return offsets;
    }

    boolean hasSurroundingBlock(BlockPos pos) {
        for (EnumFacing facing : EnumFacing.VALUES) {
            if (Surround.mc.world.getBlockState(pos.offset(facing)).getBlock() == Blocks.AIR) continue;
            return true;
        }
        return false;
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

    boolean checkForEntities(BlockPos pos) {
        Iterator iterator = Surround.mc.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos)).iterator();
        if (iterator.hasNext()) {
            Entity e = (Entity)iterator.next();
            return false;
        }
        return true;
    }

    public enum thing {
        Dynamic,
        Normal,
        Calc
    }
}

