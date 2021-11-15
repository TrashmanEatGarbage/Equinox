package me.eonexe.equinox.features.modules.misc;

import me.eonexe.equinox.Equinox;
import me.eonexe.equinox.features.modules.Module;
import me.eonexe.equinox.features.modules.player.MCP;
import me.eonexe.equinox.features.setting.Bind;
import me.eonexe.equinox.features.setting.Setting;
import me.eonexe.equinox.util.InventoryUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class KeyEXP
        extends Module {
    private Setting<Bind> keyBind = this.register(new Setting<Bind>("Key", new Bind(-1)));
    public static final Logger LOGGER = LogManager.getLogger(Equinox.MODNAME);
    private static boolean clicked = false;
    private boolean keyHeld = false;
    private int serverSlot = -1;

    public KeyEXP() {
        super("KeyEXP", "Middleclick Friends.", Module.Category.MISC, true, false, false);
    }

    @Override
    public void onEnable() {
        if (MCP.fullNullCheck()) {
            this.disable();
        }
    }

    @Override
    public void onUpdate() {
        if (keyHeld) {
            int getSlot = getXPSlot();
            LOGGER.info("balls");
            if (getSlot != -1) {
                if (serverSlot == -1) {
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(getSlot));
                    serverSlot = getSlot;
                } else {
                    if (mc.player.inventory.getStackInSlot(serverSlot).getItem() != Items.EXPERIENCE_BOTTLE) {
                        serverSlot = -1;
                    }

                    boolean offhand;
                    int expSlot = InventoryUtil.findHotbarBlock(ItemExpBottle.class);
                    boolean bl = offhand = MCP.mc.player.getHeldItemOffhand().getItem() == Items.EXPERIENCE_BOTTLE;
                    if (expSlot != -1 || offhand) {
                        if (!offhand) {
                            InventoryUtil.switchToHotbarSlot(expSlot, false);
                        }
                    }
                    KeyEXP.mc.rightClickDelayTimer = 0;
                    mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                }
            }
        }
    }

    @SubscribeEvent
    public void onTick(InputEvent.KeyInputEvent event) {
        /*
        if (Keyboard.getEventKeyState()) {
            if (!this.clicked) {

                LOGGER.info("bruhs");
            }
            this.clicked = true;
        } else {
            this.clicked = false;
        }

         */
        if (!Keyboard.getEventKeyState()) {
            if (Keyboard.getEventKey() == this.keyBind.getValue().getKey()) {
                if (!Keyboard.getEventKeyState()) {
                    keyHeld = false;
                }
            }
        }

        if (Keyboard.getEventKeyState()) {
            if (Keyboard.getEventKey() == this.keyBind.getValue().getKey()) {
                if (Keyboard.getEventKeyState()) {
                    keyHeld = true;
                }else {
                    keyHeld = false;
                    serverSlot = -1;
                }
            }
        }
    }
/*
    @SubscribeEvent
    private void onKeyInput(PlayerLivingUpdateEvent event) {
        if (keyHeld) {
            int getSlot = getXPSlot();
            LOGGER.info("balls");
            if (getSlot != -1) {
                if (serverSlot == -1) {
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(getSlot));
                    serverSlot = getSlot;
                } else {
                    if (mc.player.inventory.getStackInSlot(serverSlot).getItem() != Items.EXPERIENCE_BOTTLE) {
                        serverSlot = -1;
                    }
                    ((IMinecraftMixin) mc).setRightClickDelayTimerAccessor(0);
                    mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                }
            }
        }
    }

 */


/*
    private void useExp() {
        if (keyHeld) {
            int getSlot = getXPSlot();
            LOGGER.info("hi");
            if (getSlot != -1) {
                if (serverSlot == -1) {
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(getSlot));
                    serverSlot = getSlot;
                } else {
                    if (mc.player.inventory.getStackInSlot(serverSlot).getItem() != Items.EXPERIENCE_BOTTLE) {
                        serverSlot = -1;
                    }
                    ((IMinecraftMixin) mc).setRightClickDelayTimerAccessor(0);
                    mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                }
            }
        }
    }

    private void throwExp() {
        if (keyHeld) {
            boolean offhand;
            int expSlot = InventoryUtil.findHotbarBlock(ItemExpBottle.class);
            KeyEXP.mc.rightClickDelayTimer = 0;
            boolean bl = offhand = MCP.mc.player.getHeldItemOffhand().getItem() == Items.EXPERIENCE_BOTTLE;
            if (expSlot != -1 || offhand) {
                int oldslot = MCP.mc.player.inventory.currentItem;
                if (!offhand) {
                    InventoryUtil.switchToHotbarSlot(expSlot, false);
                }
                MCP.mc.playerController.processRightClick(MCP.mc.player, MCP.mc.world, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
                if (!offhand) {
                    InventoryUtil.switchToHotbarSlot(oldslot, false);
                }
            }
        }
    }

 */


    private int getXPSlot() {
        final Item item = Items.EXPERIENCE_BOTTLE;
        final Minecraft mc = Minecraft.getMinecraft();
        int itemSlot = mc.player.getHeldItemMainhand().getItem() == item ? mc.player.inventory.currentItem : -1;
        if (itemSlot == -1) {
            for (int l = 0; l < 9; ++l) {
                if (mc.player.inventory.getStackInSlot(l).getItem() == item) {
                    itemSlot = l;
                }
            }
        }

        return itemSlot;
    }
}