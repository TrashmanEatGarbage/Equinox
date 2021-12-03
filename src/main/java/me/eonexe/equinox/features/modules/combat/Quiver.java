package me.eonexe.equinox.features.modules.combat;

import me.eonexe.equinox.features.modules.*;
import me.eonexe.equinox.features.setting.*;
import me.eonexe.equinox.features.command.*;
import net.minecraft.entity.*;
import me.eonexe.equinox.util.*;
import net.minecraft.potion.*;
import net.minecraft.init.*;
import net.minecraft.network.play.client.*;
import net.minecraft.util.math.*;
import net.minecraft.network.*;
import net.minecraft.inventory.*;
import net.minecraft.entity.player.*;
import java.util.*;
import net.minecraft.item.*;

public class Quiver extends Module
{
    private final Setting<Integer> delay;
    private final Setting<Integer> holdLength;
    private final Setting<mainEnum> main;
    private final Setting<mainEnum> secondary;
    private final TimerUtil delayTimer;
    private final TimerUtil holdTimer;
    private int stage;
    private ArrayList<Integer> map;
    private int strSlot;
    private int speedSlot;
    private int oldSlot;

    public Quiver() {
        super("Quiver", "Automatically shoots yourself with good effects.", Category.COMBAT, true, false, false);
        this.delay = (Setting<Integer>)this.register(new Setting("Delay", 200, 0, 500));
        this.holdLength = (Setting<Integer>)this.register(new Setting("Hold Length", 350,100, 1000));
        this.main = (Setting<mainEnum>)this.register(new Setting("Main", mainEnum.SPEED));
        this.secondary = (Setting<mainEnum>)this.register(new Setting("Secondary", mainEnum.STRENGTH));
        this.delayTimer = new TimerUtil();
        this.holdTimer = new TimerUtil();
        this.strSlot = -1;
        this.speedSlot = -1;
        this.oldSlot = 1;
    }

    @Override
    public void onEnable() {
        if (nullCheck()) {
            return;
        }
        InventoryUtil.switchToHotbarSlot(ItemBow.class, false);
        this.clean();
        this.oldSlot = Quiver.mc.player.inventory.currentItem;
        Quiver.mc.gameSettings.keyBindUseItem.pressed = false;
    }

    @Override
    public void onDisable() {
        if (nullCheck()) {
            return;
        }
        InventoryUtil.switchToHotbarSlot(this.oldSlot, false);
        Quiver.mc.gameSettings.keyBindUseItem.pressed = false;
        this.clean();
    }

    @Override
    public void onUpdate() {
        if (nullCheck()) {
            return;
        }
        if (Quiver.mc.currentScreen != null) {
            return;
        }
        if (InventoryUtil.findItemInventorySlot((Item)Items.BOW, true) == -1) {
            Command.sendMessage("Couldn't find bow in inventory! Toggling!");
            this.toggle();
        }
        RotationUtil.faceVector(EntityUtil.getInterpolatedPos((Entity)Quiver.mc.player, Quiver.mc.timer.elapsedPartialTicks).add(0.0, 3.0, 0.0), false);
        if (this.stage == 0) {
            this.map = this.mapArrows();
            for (final int a : this.map) {
                final ItemStack arrow = (ItemStack)Quiver.mc.player.inventoryContainer.getInventory().get(a);
                if ((PotionUtils.getPotionFromItem(arrow).equals(PotionTypes.STRENGTH) || PotionUtils.getPotionFromItem(arrow).equals(PotionTypes.STRONG_STRENGTH) || PotionUtils.getPotionFromItem(arrow).equals(PotionTypes.LONG_STRENGTH)) && this.strSlot == -1) {
                    this.strSlot = a;
                }
                if ((PotionUtils.getPotionFromItem(arrow).equals(PotionTypes.SWIFTNESS) || PotionUtils.getPotionFromItem(arrow).equals(PotionTypes.LONG_SWIFTNESS) || PotionUtils.getPotionFromItem(arrow).equals(PotionTypes.STRONG_SWIFTNESS)) && this.speedSlot == -1) {
                    this.speedSlot = a;
                }
            }
            ++this.stage;
        }
        else if (this.stage == 1) {
            if (!this.delayTimer.passedMs(this.delay.getValue())) {
                return;
            }
            this.delayTimer.reset();
            ++this.stage;
        }
        else if (this.stage == 2) {
            this.switchTo(this.main.getValue());
            ++this.stage;
        }
        else if (this.stage == 3) {
            if (!this.delayTimer.passedMs(this.delay.getValue())) {
                return;
            }
            this.delayTimer.reset();
            ++this.stage;
        }
        else if (this.stage == 4) {
            Quiver.mc.gameSettings.keyBindUseItem.pressed = true;
            this.holdTimer.reset();
            ++this.stage;
        }
        else if (this.stage == 5) {
            if (!this.holdTimer.passedMs(this.holdLength.getValue())) {
                return;
            }
            this.holdTimer.reset();
            ++this.stage;
        }
        else if (this.stage == 6) {
            Quiver.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Quiver.mc.player.getHorizontalFacing()));
            Quiver.mc.player.resetActiveHand();
            Quiver.mc.gameSettings.keyBindUseItem.pressed = false;
            ++this.stage;
        }
        else if (this.stage == 7) {
            if (!this.delayTimer.passedMs(this.delay.getValue())) {
                return;
            }
            this.delayTimer.reset();
            ++this.stage;
        }
        else if (this.stage == 8) {
            this.map = this.mapArrows();
            this.strSlot = -1;
            this.speedSlot = -1;
            for (final int a : this.map) {
                final ItemStack arrow = (ItemStack)Quiver.mc.player.inventoryContainer.getInventory().get(a);
                if ((PotionUtils.getPotionFromItem(arrow).equals(PotionTypes.STRENGTH) || PotionUtils.getPotionFromItem(arrow).equals(PotionTypes.STRONG_STRENGTH) || PotionUtils.getPotionFromItem(arrow).equals(PotionTypes.LONG_STRENGTH)) && this.strSlot == -1) {
                    this.strSlot = a;
                }
                if ((PotionUtils.getPotionFromItem(arrow).equals(PotionTypes.SWIFTNESS) || PotionUtils.getPotionFromItem(arrow).equals(PotionTypes.LONG_SWIFTNESS) || PotionUtils.getPotionFromItem(arrow).equals(PotionTypes.STRONG_SWIFTNESS)) && this.speedSlot == -1) {
                    this.speedSlot = a;
                }
            }
            ++this.stage;
        }
        if (this.stage == 9) {
            this.switchTo(this.secondary.getValue());
            ++this.stage;
        }
        else if (this.stage == 10) {
            if (!this.delayTimer.passedMs(this.delay.getValue())) {
                return;
            }
            ++this.stage;
        }
        else if (this.stage == 11) {
            Quiver.mc.gameSettings.keyBindUseItem.pressed = true;
            this.holdTimer.reset();
            ++this.stage;
        }
        else if (this.stage == 12) {
            if (!this.holdTimer.passedMs(this.holdLength.getValue())) {
                return;
            }
            this.holdTimer.reset();
            ++this.stage;
        }
        else if (this.stage == 13) {
            Quiver.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Quiver.mc.player.getHorizontalFacing()));
            Quiver.mc.player.resetActiveHand();
            Quiver.mc.gameSettings.keyBindUseItem.pressed = false;
            ++this.stage;
        }
        else if (this.stage == 14) {
            final ArrayList<Integer> map = this.mapEmpty();
            if (!map.isEmpty()) {
                final int a = map.get(0);
                Quiver.mc.playerController.windowClick(Quiver.mc.player.inventoryContainer.windowId, a, 0, ClickType.PICKUP, (EntityPlayer)Quiver.mc.player);
            }
            ++this.stage;
        }
        else if (this.stage == 15) {
            this.setEnabled(false);
        }
    }

    private void switchTo(final Enum<mainEnum> mode) {
        if (mode.toString().equalsIgnoreCase("STRENGTH") && this.strSlot != -1) {
            this.switchTo(this.strSlot);
        }
        if (mode.toString().equalsIgnoreCase("SPEED") && this.speedSlot != -1) {
            this.switchTo(this.speedSlot);
        }
    }

    private ArrayList<Integer> mapArrows() {
        final ArrayList<Integer> map = new ArrayList<Integer>();
        for (int a = 9; a < 45; ++a) {
            if (((ItemStack)Quiver.mc.player.inventoryContainer.getInventory().get(a)).getItem() instanceof ItemTippedArrow) {
                final ItemStack arrow = (ItemStack)Quiver.mc.player.inventoryContainer.getInventory().get(a);
                if (PotionUtils.getPotionFromItem(arrow).equals(PotionTypes.STRENGTH) || PotionUtils.getPotionFromItem(arrow).equals(PotionTypes.STRONG_STRENGTH) || PotionUtils.getPotionFromItem(arrow).equals(PotionTypes.LONG_STRENGTH)) {
                    map.add(a);
                }
                if (PotionUtils.getPotionFromItem(arrow).equals(PotionTypes.SWIFTNESS) || PotionUtils.getPotionFromItem(arrow).equals(PotionTypes.LONG_SWIFTNESS) || PotionUtils.getPotionFromItem(arrow).equals(PotionTypes.STRONG_SWIFTNESS)) {
                    map.add(a);
                }
            }
        }
        return map;
    }

    private ArrayList<Integer> mapEmpty() {
        final ArrayList<Integer> map = new ArrayList<Integer>();
        for (int a = 9; a < 45; ++a) {
            if (((ItemStack)Quiver.mc.player.inventoryContainer.getInventory().get(a)).getItem() instanceof ItemAir || Quiver.mc.player.inventoryContainer.getInventory().get(a) == ItemStack.EMPTY) {
                map.add(a);
            }
        }
        return map;
    }

    private void switchTo(final int from) {
        if (from == 9) {
            return;
        }
        Quiver.mc.playerController.windowClick(Quiver.mc.player.inventoryContainer.windowId, from, 0, ClickType.PICKUP, (EntityPlayer)Quiver.mc.player);
        Quiver.mc.playerController.windowClick(Quiver.mc.player.inventoryContainer.windowId, 9, 0, ClickType.PICKUP, (EntityPlayer)Quiver.mc.player);
        Quiver.mc.playerController.windowClick(Quiver.mc.player.inventoryContainer.windowId, from, 0, ClickType.PICKUP, (EntityPlayer)Quiver.mc.player);
        Quiver.mc.playerController.updateController();
    }

    private void clean() {
        this.holdTimer.reset();
        this.delayTimer.reset();
        this.map = null;
        this.speedSlot = -1;
        this.strSlot = -1;
        this.stage = 0;
    }

    private enum mainEnum
    {
        STRENGTH,
        SPEED;
    }
}