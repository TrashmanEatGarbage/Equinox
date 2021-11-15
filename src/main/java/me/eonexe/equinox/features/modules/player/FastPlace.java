package me.eonexe.equinox.features.modules.player;

import me.eonexe.equinox.features.modules.Module;
import me.eonexe.equinox.util.InventoryUtil;
import net.minecraft.item.ItemExpBottle;

public class FastPlace
        extends Module {
    public FastPlace() {
        super("FastEXP", "Fast everything.", Module.Category.PLAYER, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (FastPlace.fullNullCheck()) {
            return;
        }
        if (InventoryUtil.holdingItem(ItemExpBottle.class)) {
            FastPlace.mc.rightClickDelayTimer = 0;
        }
    }
}

