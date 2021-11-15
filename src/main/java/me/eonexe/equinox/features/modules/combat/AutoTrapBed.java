package me.eonexe.equinox.features.modules.combat;

import java.util.List;

import me.eonexe.equinox.Equinox;
import me.eonexe.equinox.features.modules.Module;
import me.eonexe.equinox.features.setting.Setting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;

public class AutoTrapBed extends Module {
    public AutoTrapBed() {
        super("AutoTrapBed", "funny airplace module", Category.COMBAT, true, false, false);
    }

    private int obiSlot;
    private int lastSlot;
    private int tickDelay;
    private boolean firstRun;
    private String lastTickTargetName;
    private EntityPlayer closestTarget;
    private BlockPos target;

    public Setting<Integer> range = register(new Setting<Integer>("Range", 7, 1, 9));

    @Override
    public void onEnable() {
        if(mc.player == null) {
            this.toggle();
            return;
        }

        MinecraftForge.EVENT_BUS.register(this);


        lastSlot = mc.player.inventory.currentItem;
    }

    @Override
    public void onDisable() {
        if(mc.player == null) {
            return;
        }

        if(lastSlot != -1) {
            mc.player.inventory.currentItem = lastSlot;
        }

        tickDelay = 0;
    }

    @Override
    public void onUpdate() {
        if(mc.player == null) {
            this.toggle();
            return;
        }

        try {
            findClosestTarget();
        } catch(NullPointerException npe) {}

        if(closestTarget == null) {
            if(firstRun) {
                firstRun = false;
            }
        }

        if (firstRun && closestTarget != null) {
            firstRun = false;
            lastTickTargetName = closestTarget.getName();
        }

        if(closestTarget != null && lastTickTargetName != null) {
            if (!lastTickTargetName.equals(closestTarget.getName())) {
                lastTickTargetName = closestTarget.getName();
            }
        }
        if(closestTarget != null) {
            if(mc.player.getPositionVector().distanceTo(closestTarget.getPositionVector()) <= range.getValue()) {

                tickDelay++;

                obiSlot = this.findObby();

                if(tickDelay == 1 && obiSlot != -1) {
                    mc.player.inventory.currentItem = obiSlot;
                }

                target = new BlockPos(closestTarget.getPositionVector().add(0, 3, 0));


                if(tickDelay == 25) {
                    placeBlock(target, EnumFacing.DOWN);
                }

                if(tickDelay == 40) {
                    this.toggle();
                }
            }
        }
    }

    private void placeBlock(BlockPos pos, EnumFacing side) {
        BlockPos neighbour = pos.offset(side);
        EnumFacing opposite = side.getOpposite();
        Vec3d hitVec = new Vec3d(neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        mc.playerController.processRightClickBlock(mc.player, mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
        mc.player.swingArm(EnumHand.MAIN_HAND);
    }

    private boolean canPlace(BlockPos pos) {
        Block block = mc.world.getBlockState(pos).getBlock();
        if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid)) {
            return false;
        } else {
            return true;
        }
    }

    private int findObby() {
        int slot = -1;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof ItemBlock)) {
                continue;
            }
            Block block = ((ItemBlock) stack.getItem()).getBlock();
            if (block instanceof BlockObsidian) {
                slot = i;
                break;
            }
        }
        return slot;
    }

    private void findClosestTarget() {
        List<EntityPlayer> playerList = mc.world.playerEntities;
        closestTarget = null;
        for (EntityPlayer target : playerList) {

            if (target == mc.player) {
                continue;
            }
            if (Equinox.friendManager.isFriend(target.getName())) {
                continue;
            }
            if (!isLiving(target)) {
                continue;
            }
            if ((target).getHealth() <= 0) {
                continue;
            }
            if (closestTarget == null) {
                closestTarget = target;
                continue;
            }
            if (mc.player.getDistance(target) < mc.player.getDistance(closestTarget)) {
                closestTarget = target;
            }
        }
    }
    public static boolean isLiving(Entity e) {
        return e instanceof EntityLivingBase;
    }
}

