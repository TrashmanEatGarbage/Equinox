package me.eonexe.equinox.features.modules.misc;

import me.eonexe.equinox.features.modules.Module;
import me.eonexe.equinox.features.setting.Bind;
import me.eonexe.equinox.features.setting.Setting;
import me.eonexe.equinox.util.InventoryUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.util.EnumHand;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class KeyPearl
        extends Module {
    private boolean clicked = false;
    private Setting<Bind> keyBind = this.register(new Setting<Bind>("Key", new Bind(-1)));

    public KeyPearl() {
        super("KeyPearl", "Throws a pearl", Module.Category.MISC, false, false, false);
    }

    @Override
    public void onEnable() {
        if (fullNullCheck()) {
            this.disable();
        }
    }

    @Override
    public void onTick() {
        if (Keyboard.getEventKey() == this.keyBind.getValue().getKey()) {
            if (!this.clicked) {
                this.throwPearl();
            }
            this.clicked = true;
        } else {
            this.clicked = false;
        }
    }

    private void throwPearl() {
        boolean offhand;
        int pearlSlot = InventoryUtil.findHotbarBlock(ItemEnderPearl.class);
        boolean bl = offhand = mc.player.getHeldItemOffhand().getItem() == Items.ENDER_PEARL;
        if (pearlSlot != -1 || offhand) {
            int oldslot = mc.player.inventory.currentItem;
            if (!offhand) {
                InventoryUtil.switchToHotbarSlot(pearlSlot, false);
            }
            mc.playerController.processRightClick(mc.player, mc.world, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
            if (!offhand) {
                InventoryUtil.switchToHotbarSlot(oldslot, false);
            }
        }
    }
}

