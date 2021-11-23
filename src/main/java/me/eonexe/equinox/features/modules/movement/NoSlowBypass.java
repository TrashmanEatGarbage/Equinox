package me.eonexe.equinox.features.modules.movement;

import me.eonexe.equinox.Equinox;
import me.eonexe.equinox.event.events.KeyEvent;
import me.eonexe.equinox.event.events.PacketEvent;
import me.eonexe.equinox.features.modules.Module;
import me.eonexe.equinox.features.setting.Setting;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public
class NoSlowBypass
        extends Module {
    private static final KeyBinding[] keys = {NoSlowBypass.mc.gameSettings.keyBindForward , NoSlowBypass.mc.gameSettings.keyBindBack , NoSlowBypass.mc.gameSettings.keyBindLeft , NoSlowBypass.mc.gameSettings.keyBindRight , NoSlowBypass.mc.gameSettings.keyBindJump , NoSlowBypass.mc.gameSettings.keyBindSprint};
    private static NoSlowBypass INSTANCE = new NoSlowBypass ( );
    public final Setting < Double > webHorizontalFactor = this.register ( new Setting <> ( "WebHSpeed" , 2.0 , 0.0 , 100.0 ) );
    public final Setting < Double > webVerticalFactor = this.register ( new Setting <> ( "WebVSpeed" , 2.0 , 0.0 , 100.0 ) );
    public Setting < Boolean > guiMove = this.register ( new Setting <> ( "GuiMove" , true ) );
    public Setting < Boolean > noSlow = this.register ( new Setting <> ( "NoSlow" , true ) );
    public Setting < Boolean > soulSand = this.register ( new Setting <> ( "SoulSand" , true ) );
    public Setting < Boolean > strict = this.register ( new Setting <> ( "Strict" , false ) );
    public Setting < Boolean > sneakPacket = this.register ( new Setting <> ( "SneakPacket" , false ) );
    public Setting < Boolean > endPortal = this.register ( new Setting <> ( "EndPortal" , false ) );
    public Setting < Boolean > webs = this.register ( new Setting <> ( "Webs" , false ) );
    private boolean sneaking;

    public
    NoSlowBypass ( ) {
        super ( "NoSlowDown" , "Prevents you from getting slowed down." , Module.Category.MOVEMENT , true , false , false );
        this.setInstance ( );
    }

    public static
    NoSlowBypass getInstance ( ) {
        if ( INSTANCE == null ) {
            INSTANCE = new NoSlowBypass ( );
        }
        return INSTANCE;
    }

    private
    void setInstance ( ) {
        INSTANCE = this;
    }

    @Override
    public
    void onUpdate ( ) {
        if ( this.guiMove.getValue ( ) ) {
            {
                for (KeyBinding bind : keys) {
                    KeyBinding.setKeyBindState ( bind.getKeyCode ( ) , Keyboard.isKeyDown ( bind.getKeyCode ( ) ) );
                }
            }
            if ( NoSlowBypass.mc.currentScreen == null ) {
                for (KeyBinding bind : keys) {
                    if ( Keyboard.isKeyDown ( bind.getKeyCode ( ) ) ) continue;
                    KeyBinding.setKeyBindState ( bind.getKeyCode ( ) , false );
                }
            }
        }
        if ( this.webs.getValue ( ) && Equinox.moduleManager.getModuleByClass ( Flight.class ).isDisabled ( ) && Equinox.moduleManager.getModuleByClass ( Phase.class ).isDisabled ( ) && NoSlowBypass.mc.player.isInWeb ) {
            NoSlowBypass.mc.player.motionX *= this.webHorizontalFactor.getValue ( );
            NoSlowBypass.mc.player.motionZ *= this.webHorizontalFactor.getValue ( );
            NoSlowBypass.mc.player.motionY *= this.webVerticalFactor.getValue ( );
        }
        NoSlowBypass.mc.player.getActiveItemStack ( ).getItem ( );
        if ( this.sneaking && ! NoSlowBypass.mc.player.isHandActive ( ) && this.sneakPacket.getValue ( ) ) {
            NoSlowBypass.mc.player.connection.sendPacket ( new CPacketEntityAction ( NoSlowBypass.mc.player , CPacketEntityAction.Action.STOP_SNEAKING ) );
            this.sneaking = false;
        }
    }

    @SubscribeEvent
    public
    void onUseItem ( PlayerInteractEvent.RightClickItem event ) {
        Item item = NoSlowBypass.mc.player.getHeldItem ( event.getHand ( ) ).getItem ( );
        if ( ( item instanceof ItemFood || item instanceof ItemBow || item instanceof ItemPotion && this.sneakPacket.getValue ( ) ) && ! this.sneaking ) {
            NoSlowBypass.mc.player.connection.sendPacket ( new CPacketEntityAction ( NoSlowBypass.mc.player , CPacketEntityAction.Action.START_SNEAKING ) );
            this.sneaking = true;
        }
    }

    @SubscribeEvent
    public
    void onInput ( InputUpdateEvent event ) {
        if ( this.noSlow.getValue ( ) && NoSlowBypass.mc.player.isHandActive ( ) && ! NoSlowBypass.mc.player.isRiding ( ) ) {
            event.getMovementInput ( ).moveStrafe *= 5.0f;
            event.getMovementInput ( ).moveForward *= 5.0f;
        }
    }

    @SubscribeEvent
    public
    void onKeyEvent ( KeyEvent event ) {
        if ( this.guiMove.getValue ( ) && event.getStage ( ) == 0 && ! ( NoSlowBypass.mc.currentScreen instanceof GuiChat ) ) {
            event.info = event.pressed;
        }
    }

    @SubscribeEvent
    public
    void onPacket ( PacketEvent.Send event ) {
        if ( event.getPacket ( ) instanceof CPacketPlayer && this.strict.getValue ( ) && this.noSlow.getValue ( ) && NoSlowBypass.mc.player.isHandActive ( ) && ! NoSlowBypass.mc.player.isRiding ( ) ) {
            NoSlowBypass.mc.player.connection.sendPacket ( new CPacketPlayerDigging ( CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK , new BlockPos ( Math.floor ( NoSlowBypass.mc.player.posX ) , Math.floor ( NoSlowBypass.mc.player.posY ) , Math.floor ( NoSlowBypass.mc.player.posZ ) ) , EnumFacing.DOWN ) );
        }
    }
}
