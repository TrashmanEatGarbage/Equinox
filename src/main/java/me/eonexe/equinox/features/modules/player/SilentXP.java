package me.eonexe.equinox.features.modules.player;

import me.eonexe.equinox.features.modules.*;
import me.eonexe.equinox.features.setting.*;
import org.lwjgl.input.*;
import net.minecraft.util.math.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import me.eonexe.equinox.util.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

public class SilentXP extends Module
{
    public Setting<Mode> mode;
    public Setting<Boolean> antiFriend;
    public Setting<Bind> key;
    public Setting<Boolean> groundOnly;
    private boolean last;
    private boolean on;

    public SilentXP() {
        super("SilentXP", "Silent XP.", Module.Category.PLAYER, false, false, false);
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", Mode.MIDDLECLICK));
        this.antiFriend = (Setting<Boolean>)this.register(new Setting("AntiFriend", true));
        this.key = (Setting<Bind>)this.register(new Setting("Key", new Bind(-1), v -> this.mode.getValue() != Mode.MIDDLECLICK));
        this.groundOnly = (Setting<Boolean>)this.register(new Setting("BelowHorizon", false));
    }

    public void onUpdate() {
        if (fullNullCheck()) {
            return;
        }
        switch (this.mode.getValue()) {
            case PRESS: {
                if (this.key.getValue().isDown()) {
                    this.throwXP(false);
                    break;
                }
                break;
            }
            case TOGGLE: {
                if (this.toggled()) {
                    this.throwXP(false);
                    break;
                }
                break;
            }
            default: {
                if (this.groundOnly.getValue() && SilentXP.mc.player.rotationPitch < 0.0f) {
                    return;
                }
                if (Mouse.isButtonDown(2)) {
                    this.throwXP(true);
                    break;
                }
                break;
            }
        }
    }

    private boolean toggled() {
        if (this.key.getValue().getKey() == -1) {
            return false;
        }
        if (!Keyboard.isKeyDown(this.key.getValue().getKey())) {
            this.last = true;
        }
        else {
            if (Keyboard.isKeyDown(this.key.getValue().getKey()) && this.last && !this.on) {
                this.last = false;
                return this.on = true;
            }
            if (Keyboard.isKeyDown(this.key.getValue().getKey()) && this.last && this.on) {
                this.last = false;
                return this.on = false;
            }
        }
        return this.on;
    }

    private void throwXP(final boolean mcf) {
        final RayTraceResult result;
        if (mcf && this.antiFriend.getValue() && (result = SilentXP.mc.objectMouseOver) != null && result.typeOfHit == RayTraceResult.Type.ENTITY && result.entityHit instanceof EntityPlayer) {
            return;
        }
        final int xpSlot = InventoryUtil.findHotbarBlock(ItemExpBottle.class);
        final boolean offhand = SilentXP.mc.player.getHeldItemOffhand().getItem() == Items.EXPERIENCE_BOTTLE;
        if (xpSlot != -1 || offhand) {
            final int oldslot = SilentXP.mc.player.inventory.currentItem;
            if (!offhand) {
                InventoryUtil.switchToHotbarSlot(xpSlot, false);
            }
            SilentXP.mc.playerController.processRightClick((EntityPlayer)SilentXP.mc.player, (World)SilentXP.mc.world, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
            if (!offhand) {
                InventoryUtil.switchToHotbarSlot(oldslot, false);
            }
        }
    }

    public enum Mode
    {
        MIDDLECLICK,
        TOGGLE,
        PRESS;
    }
}