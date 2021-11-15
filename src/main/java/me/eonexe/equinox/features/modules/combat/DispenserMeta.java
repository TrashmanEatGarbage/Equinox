package me.eonexe.equinox.features.modules.combat;

import me.eonexe.equinox.features.command.Command;
import me.eonexe.equinox.features.modules.Module;
import me.eonexe.equinox.features.setting.Setting;
import me.eonexe.equinox.util.BlockInteractionHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by hub on 10 December 2019
 * Updated by hub on 13 December 2019
 */
public class DispenserMeta extends Module {

    private static final DecimalFormat df = new DecimalFormat("#.#");

    private Setting<Boolean> rotate = this.register(new Setting<>("Rotate", false));
    private Setting<Boolean> grabItem = this.register(new Setting<>("Grab Item", false));
    private Setting<Boolean> autoEnableHitAura = this.register(new Setting<>("Auto enable Hit Aura", false));
    private Setting<Boolean> autoEnableBypass = this.register(new Setting<>("Auto enable Illegals Bypass", false));
    private Setting<Boolean> debugMessages = this.register(new Setting<>("Debug Messages", false));

    public DispenserMeta() {
        super("Auto32k", "Don't use with any AntiGhostBlock Mod!", Category.COMBAT, true, false, false);
    }


    private int stage;

    private BlockPos placeTarget;

    private int obiSlot;
    private int dispenserSlot;
    private int shulkerSlot;
    private int redstoneSlot;
    private int hopperSlot;

    private boolean isSneaking;

    @Override
    public void onEnable() {
        df.setRoundingMode(RoundingMode.CEILING);

        stage = 0;

        placeTarget = null;

        obiSlot = -1;
        dispenserSlot = -1;
        shulkerSlot = -1;
        redstoneSlot = -1;
        hopperSlot = -1;

        isSneaking = false;

        for (int i = 0; i < 9; i++) {

            if (obiSlot != -1 && dispenserSlot != -1 && shulkerSlot != -1 && redstoneSlot != -1 && hopperSlot != -1) {
                break;
            }

            ItemStack stack = mc.player.inventory.getStackInSlot(i);

            if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof ItemBlock)) {
                continue;
            }

            Block block = ((ItemBlock) stack.getItem()).getBlock();

            if (block == Blocks.HOPPER) {
                hopperSlot = i;
            } else if (BlockInteractionHelper.shulkerList.contains(block)) {
                shulkerSlot = i;
            } else if (block == Blocks.OBSIDIAN) {
                obiSlot = i;
            } else if (block == Blocks.DISPENSER) {
                dispenserSlot = i;
            } else if (block == Blocks.REDSTONE_BLOCK) {
                redstoneSlot = i;
            }

        }

        if (obiSlot == -1 || dispenserSlot == -1 || shulkerSlot == -1 || redstoneSlot == -1 || hopperSlot == -1) {
            if (debugMessages.getValue()) {
                Command.sendMessage("[Auto32k] Items missing, disabling.");
            }
            this.disable();
            return;
        }

        // IntelliJ dumb, iirc this can cause npe when looking in water, at crystal etc.
        if (mc.objectMouseOver == null || mc.objectMouseOver.getBlockPos() == null || mc.objectMouseOver.getBlockPos().up() == null) {
            if (debugMessages.getValue()) {
                Command.sendMessage("[Auto32k] Not a valid place target, disabling.");
            }
            this.disable();
            return;
        }

        placeTarget = mc.objectMouseOver.getBlockPos().up();

        //if (autoEnableBypass.getValue()) {
        //    ModuleManager.getModuleByName("IllegalItemBypass").enable();
        //}

        if (debugMessages.getValue()) {
            Command.sendMessage("[Auto32k] Place Target: " + placeTarget.getX() + " " + placeTarget.getY() + " " + placeTarget.getZ() + " Distance: " + df.format(mc.player.getPositionVector().distanceTo(new Vec3d(placeTarget))));
        }

    }

    @Override
    public void onUpdate() {

        //if (mc.player == null || ModuleManager.isModuleEnabled("Freecam")) {
        //    return;
        //}

        // stage 0: place obi and dispenser
        if (stage == 0) {

            mc.player.inventory.currentItem = obiSlot;
            placeBlock(new BlockPos(placeTarget), EnumFacing.DOWN);

            mc.player.inventory.currentItem = dispenserSlot;
            placeBlock(new BlockPos(placeTarget.add(0, 1, 0)), EnumFacing.DOWN);

            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            isSneaking = false;

            mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(placeTarget.add(0, 1, 0), EnumFacing.DOWN, EnumHand.MAIN_HAND, 0, 0, 0));

            stage = 1;
            return;

        }

        // stage 1: put shulker, place redstone
        if (stage == 1) {

            if (!(mc.currentScreen instanceof GuiContainer)) {
                return;
            }

            mc.playerController.windowClick(mc.player.openContainer.windowId, 1, shulkerSlot, ClickType.SWAP, mc.player);
            mc.player.closeScreen();

            mc.player.inventory.currentItem = redstoneSlot;
            placeBlock(new BlockPos(placeTarget.add(0, 2, 0)), EnumFacing.DOWN);

            stage = 2;
            return;

        }

        // stage 2: place hopper
        if (stage == 2) {

            // TODO: fix instahopper, why boken? ;(
            Block block = mc.world.getBlockState(placeTarget.offset(mc.player.getHorizontalFacing().getOpposite()).up()).getBlock();
            if ((block instanceof BlockAir) || (block instanceof BlockLiquid)) {
                return;
            }

            mc.player.inventory.currentItem = hopperSlot;
            placeBlock(new BlockPos(placeTarget.offset(mc.player.getHorizontalFacing().getOpposite())), mc.player.getHorizontalFacing());

            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            isSneaking = false;

            mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(placeTarget.offset(mc.player.getHorizontalFacing().getOpposite()), EnumFacing.DOWN, EnumHand.MAIN_HAND, 0, 0, 0));

            mc.player.inventory.currentItem = shulkerSlot;

            if (!grabItem.getValue()) {
                this.disable();
                return;
            }

            stage = 3;
            return;

        }

        // stage 3: hopper gui
        if (stage == 3) {

            if (!(mc.currentScreen instanceof GuiContainer)) {
                return;
            }

            if (((GuiContainer) mc.currentScreen).inventorySlots.getSlot(0).getStack().isEmpty) {
                return;
            }

            mc.playerController.windowClick(mc.player.openContainer.windowId, 0, mc.player.inventory.currentItem, ClickType.SWAP, mc.player);

            if (autoEnableHitAura.getValue()) {
                //ModuleManager.getModuleByName("Aura").enable();
            }

            this.disable();

        }

    }

    private void placeBlock(BlockPos pos, EnumFacing side) {

        BlockPos neighbour = pos.offset(side);
        EnumFacing opposite = side.getOpposite();

        if (!isSneaking) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
            isSneaking = true;
        }

        Vec3d hitVec = new Vec3d(neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));

        if (rotate.getValue()) {
            //faceVectorPacketInstant(hitVec);
        }

        mc.playerController.processRightClickBlock(mc.player, mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
        mc.player.swingArm(EnumHand.MAIN_HAND);

    }

}