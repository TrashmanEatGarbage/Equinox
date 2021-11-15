package me.eonexe.equinox.features.modules.player;

import me.eonexe.equinox.util.InventoryUtils;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.eonexe.equinox.Equinox;
import me.eonexe.equinox.features.command.Command;
import me.eonexe.equinox.features.modules.misc.MCF;
import me.eonexe.equinox.features.setting.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;
import me.eonexe.equinox.features.modules.Module;
import me.eonexe.equinox.util.NullUtils;


public class MiddleClick
        extends Module {
    //public Setting<Boolean> friend = this.register(new Setting<Boolean>("friend", true));
    public Setting<Boolean> pearl = this.register(new Setting<Boolean>("Pearl", true));
    public Setting<Boolean> xp = this.register(new Setting<Boolean>("Xp", true));
    public Setting<Boolean> offhandSwap = this.register(new Setting<Boolean>("Offhand Swap", false));
    boolean hasPressed;

    public MiddleClick() {
        super("Middle Click", "does the middle click", Module.Category.PLAYER, true, false, false);
        this.hasPressed = false;
    }

    @Override
    public void onEnable() {
        if (MiddleClick.fullNullCheck()) {
            this.disable();
        }
    }


    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (Mouse.isButtonDown(2)) {
            Entity pointed = (mc.getRenderManager()).pointedEntity;
            if (!this.hasPressed) {
                Entity entity;
                RayTraceResult result = MCF.mc.objectMouseOver;
                /*
                if (((Boolean) this.friend.getValue()).booleanValue() && pointed != null && (entity = result.entityHit) instanceof EntityPlayer) {
                    if (Equinox.friendManager.isFriend(entity.getName())) {
                        Equinox.friendManager.removeFriend(entity.getName());
                        Command.sendMessage(ChatFormatting.RED + entity.getName() + ChatFormatting.RED + " has been unfriended.");
                    } else {
                        Equinox.friendManager.addFriend(entity.getName());
                        Command.sendMessage(ChatFormatting.AQUA + entity.getName() + ChatFormatting.AQUA + " has been friended.");
                    }
                }
                 */
                if (pointed == null && ((Boolean) this.pearl.getValue()).booleanValue() && allowPearl()) {
                    int oldSlot = mc.player.inventory.currentItem;
                    int pearlSlot = InventoryUtils.getHotbarItemSlot(Items.ENDER_PEARL);
                    if (pearlSlot == -1 &&
                            !((Boolean) this.offhandSwap.getValue()).booleanValue()) {
                        Command.sendMessage(ChatFormatting.RED + "No pearls in hotbar");
                        //Kami5ChatUtils.sendMessage(new ChatMessage("No pearls in hotbar", false, 0));
                        this.hasPressed = true;
                        return;
                    }
                    Item oldItem = mc.player.getHeldItemOffhand().getItem();
                    if (((Boolean) this.offhandSwap.getValue()).booleanValue()) {
                        InventoryUtils.moveItemToOffhand(Items.ENDER_PEARL);
                    } else {
                        InventoryUtils.switchToSlotGhost(pearlSlot);
                    }
                    mc.player.connection.sendPacket((Packet) new CPacketPlayerTryUseItem(((Boolean) this.offhandSwap.getValue()).booleanValue() ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND));
                    if (((Boolean) this.offhandSwap.getValue()).booleanValue()) {
                        InventoryUtils.moveItemToOffhand(oldItem);
                    } else {
                        InventoryUtils.switchToSlotGhost(oldSlot);
                    }
                }
            }
            if (((Boolean) this.xp.getValue()).booleanValue() && allowExp()) {
                int oldSlot = mc.player.inventory.currentItem;
                int xpSlot = InventoryUtils.getHotbarItemSlot(Items.EXPERIENCE_BOTTLE);
                if (xpSlot == -1) {
                    this.hasPressed = true;
                    return;
                }
                InventoryUtils.switchToSlotGhost(xpSlot);
                mc.player.connection.sendPacket((Packet) new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                InventoryUtils.switchToSlotGhost(oldSlot);
            }
            this.hasPressed = true;
        } else {
            this.hasPressed = false;
        }
    }

    boolean allowPearl() {
        RayTraceResult mouseOver = mc.objectMouseOver;
        return (mouseOver == null || mouseOver.typeOfHit == RayTraceResult.Type.MISS);
    }

    boolean allowExp() {
        RayTraceResult mouseOver = mc.objectMouseOver;
        return (mouseOver != null && mouseOver.typeOfHit == RayTraceResult.Type.BLOCK);
    }
}