package me.eonexe.equinox.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.item.Item;

public class InventoryUtils implements IMinecraft
{
    public static int getHotbarItemSlot(final Item item) {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            if (mc.player.inventory.getStackInSlot(i).getItem().equals(item)) {
                slot = i;
                break;
            }
        }
        return slot;
    }

    public static int getHotbarItemSlot2(final Item item) {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            if (mc.player.inventory.getStackInSlot(i).getItem().equals(item)) {
                slot = i;
                break;
            }
        }
        if (slot == -1) {
            return mc.player.inventory.currentItem;
        }
        return slot;
    }

    public static void switchToSlot(final int slot) {
        mc.player.inventory.currentItem = slot;
    }

    public static void switchToSlot(final Item item) {
        mc.player.inventory.currentItem = getHotbarItemSlot2(item);
    }

    public static void switchToSlotGhost(final int slot) {
        mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(slot));
    }

    public static void switchToSlotGhost(final Item item) {
        switchToSlotGhost(getHotbarItemSlot2(item));
    }

    public static int getItemCount(final Item item) {
        int count = 0;
        count = mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem().equals(item)).mapToInt(ItemStack::getCount).sum();
        return count;
    }

    public static int getInventoryItemSlot(final Item item) {
        int slot = -1;
        for (int i = 45; i > 0; --i) {
            if (mc.player.inventory.getStackInSlot(i).getItem().equals(item)) {
                slot = i;
                break;
            }
        }
        return slot;
    }

    public static void moveItemToOffhand(final int slot) {
        boolean startMoving = true;
        boolean moving = false;
        boolean returning = false;
        int returnSlot = 0;
        if (slot == -1) {
            return;
        }
        if (!moving && startMoving) {
            mc.playerController.windowClick(0, (slot < 9) ? (slot + 36) : slot, 0, ClickType.PICKUP, (EntityPlayer) mc.player);
            moving = true;
            startMoving = false;
        }
        if (moving) {
            mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer) mc.player);
            moving = false;
            returning = true;
        }
        if (returning) {
            for (int i = 0; i < 45; ++i) {
                if (mc.player.inventory.getStackInSlot(i).isEmpty()) {
                    returnSlot = i;
                    break;
                }
            }
            if (returnSlot != -1) {
                mc.playerController.windowClick(0, (returnSlot < 9) ? (returnSlot + 36) : returnSlot, 0, ClickType.PICKUP, (EntityPlayer) mc.player);
            }
            returning = false;
        }
        startMoving = true;
    }

    public static void moveItemToOffhand(final int slot, final int returnSlot) {
        boolean startMoving = true;
        boolean moving = false;
        boolean returning = false;
        if (slot == -1) {
            return;
        }
        if (!moving && startMoving) {
            mc.playerController.windowClick(0, (slot < 9) ? (slot + 36) : slot, 0, ClickType.PICKUP, (EntityPlayer) mc.player);
            moving = true;
            startMoving = false;
        }
        if (moving) {
            mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer) mc.player);
            moving = false;
            returning = true;
        }
        if (returning) {
            if (returnSlot != -1) {
                mc.playerController.windowClick(0, (returnSlot < 9) ? (returnSlot + 36) : returnSlot, 0, ClickType.PICKUP, (EntityPlayer) mc.player);
            }
            returning = false;
        }
        startMoving = true;
    }

    public static void moveItemToOffhand(final Item item) {
        final int slot = getInventoryItemSlot(item);
        if (slot != -1) {
            moveItemToOffhand(slot);
        }
    }

    public static void moveItem(final int slot, final int slotOut) {
        boolean startMoving = true;
        boolean moving = false;
        boolean returning = false;
        if (!moving && startMoving) {
            mc.playerController.windowClick(0, (slot < 9) ? (slot + 36) : slot, 0, ClickType.PICKUP, (EntityPlayer) mc.player);
            moving = true;
            startMoving = false;
        }
        if (moving) {
            mc.playerController.windowClick(0, (slotOut < 9) ? (slotOut + 36) : slotOut, 0, ClickType.PICKUP, (EntityPlayer) mc.player);
            moving = false;
            returning = true;
        }
        if (returning) {
            mc.playerController.windowClick(0, (slot < 9) ? (slot + 36) : slot, 0, ClickType.PICKUP, (EntityPlayer) mc.player);
            returning = false;
        }
        startMoving = true;
    }

    public static void moveItem(final Item item, final int slot) {
        moveItem(getInventoryItemSlot(item), slot);
    }
}