package me.eonexe.equinox.features.modules.combat;

import me.eonexe.equinox.features.modules.Module;
import me.eonexe.equinox.features.modules.Module.Category;
import me.eonexe.equinox.util.BlockUtil;
import me.eonexe.equinox.util.InventoryUtil;
import net.minecraft.item.ItemSkull;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class SkullBurrow extends Module {
    public SkullBurrow() {
        super("SelfSkull", "Places skulls inside yourself", Category.COMBAT, false, false, false);
    }

    public void onUpdate() {
        int slot = InventoryUtil.findHotbarBlock(ItemSkull.class);
        if (slot == -1) {
            this.disable();
        } else {
            int lastSlot = -1;
            if (mc.player.getHeldItemMainhand().getItem().getClass() != ItemSkull.class) {
                lastSlot = mc.player.inventory.currentItem;
                mc.getConnection().sendPacket(new CPacketHeldItemChange(slot));
            }

            BlockUtil.placeBlock(new BlockPos(mc.player.getPositionVector()), EnumHand.MAIN_HAND, true, true, mc.player.isSneaking());
            if (lastSlot != -1) {
                mc.getConnection().sendPacket(new CPacketHeldItemChange(lastSlot));
            }

            this.disable();
        }
    }
}
