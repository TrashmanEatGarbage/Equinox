package me.eonexe.equinox.features.modules.player;

import me.eonexe.equinox.features.modules.Module;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;

public class ChestSwap extends Module {
    public ChestSwap() {
        super("ChestSwap", "Swaps your chest plate with an elytra and vice versa", Category.PLAYER, true, false, false);
    }

    public void onEnable() {
        if (mc.player != null) {
            ItemStack l_ChestSlot = mc.player.inventoryContainer.getSlot(6).getStack();
            int l_Slot;
            if (l_ChestSlot.isEmpty()) {
                l_Slot = this.FindChestItem(true);
                if (l_Slot != -1) {
                    mc.playerController.windowClick(mc.player.inventoryContainer.windowId, l_Slot, 0, ClickType.PICKUP, mc.player);
                    mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 6, 0, ClickType.PICKUP, mc.player);
                    mc.playerController.windowClick(mc.player.inventoryContainer.windowId, l_Slot, 0, ClickType.PICKUP, mc.player);
                    mc.playerController.updateController();
                }

                this.toggle();
            } else {
                l_Slot = this.FindChestItem(l_ChestSlot.getItem() instanceof ItemArmor);
                if (l_Slot != -1) {
                    mc.playerController.windowClick(mc.player.inventoryContainer.windowId, l_Slot, 0, ClickType.PICKUP, mc.player);
                    mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 6, 0, ClickType.PICKUP, mc.player);
                    mc.playerController.windowClick(mc.player.inventoryContainer.windowId, l_Slot, 0, ClickType.PICKUP, mc.player);
                    mc.playerController.updateController();
                }

                this.toggle();
            }
        }
    }

    private int FindChestItem(boolean p_Elytra) {
        int slot = -1;
        float damage = 0.0F;

        for(int i = 0; i < mc.player.inventoryContainer.getInventory().size(); ++i) {
            if (i != 0 && i != 5 && i != 6 && i != 7 && i != 8) {
                ItemStack s = (ItemStack)mc.player.inventoryContainer.getInventory().get(i);
                if (s != null && s.getItem() != Items.AIR) {
                    if (s.getItem() instanceof ItemArmor) {
                        ItemArmor armor = (ItemArmor)s.getItem();
                        if (armor.armorType == EntityEquipmentSlot.CHEST) {
                            float currentDamage = (float)(armor.damageReduceAmount + EnchantmentHelper.getEnchantmentLevel(Enchantments.PROTECTION, s));
                            boolean cursed = EnchantmentHelper.hasBindingCurse(s);
                            if (currentDamage > damage && !cursed) {
                                damage = currentDamage;
                                slot = i;
                            }
                        }
                    } else if (p_Elytra && s.getItem() instanceof ItemElytra) {
                        return i;
                    }
                }
            }
        }

        return slot;
    }
}