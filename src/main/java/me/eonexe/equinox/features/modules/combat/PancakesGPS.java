package me.eonexe.equinox.features.modules.combat;

import me.eonexe.equinox.event.events.ConnectionEvent;
import me.eonexe.equinox.event.events.DeathEvent;
import me.eonexe.equinox.event.events.PacketEvent;
import me.eonexe.equinox.event.events.UpdateWalkingPlayerEvent;
import me.eonexe.equinox.features.command.Command;
import me.eonexe.equinox.features.modules.Module;
import me.eonexe.equinox.features.modules.combat.Asset;
import me.eonexe.equinox.features.modules.combat.AutoCrystal;
import me.eonexe.equinox.features.setting.Setting;
import me.eonexe.equinox.util.EntityUtil;
import me.eonexe.equinox.util.TextUtil;
import me.eonexe.equinox.util.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public
class PancakesGPS
        extends Module {
    private static PancakesGPS instance;
    private final Timer timer = new Timer ( );
    private final Set < BlockPos > manuallyPlaced = new HashSet <> ( );
    public Setting < TextUtil.Color > color = this.register ( new Setting <> ( "Color" , TextUtil.Color.RED ) );
    public Setting < Boolean > autoEnable = this.register ( new Setting <> ( "AutoEnable" , false ) );
    public Setting < Boolean > autoDisable = this.register ( new Setting <> ( "AutoDisable" , true ) );
    private EntityPlayer trackedPlayer;
    private int usedExp;
    private int usedStacks;
    private int usedCrystals;
    private int usedCStacks;
    private boolean shouldEnable;

    public
    PancakesGPS ( ) {
        super ( "PancakesGPS" , "Tracks players in 1v1s." , Module.Category.MISC , true , false , true );
        instance = this;
    }

    public static
    PancakesGPS getInstance ( ) {
        if ( instance == null ) {
            instance = new PancakesGPS ( );
        }
        return instance;
    }

    @SubscribeEvent
    public
    void onPacketReceive ( PacketEvent.Receive event ) {
        if ( ! PancakesGPS.fullNullCheck ( ) && ( this.autoEnable.getValue ( ) || this.autoDisable.getValue ( ) ) && event.getPacket ( ) instanceof SPacketChat ) {
            SPacketChat packet = event.getPacket ( );
            String message = packet.getChatComponent ( ).getFormattedText ( );
            if ( this.autoEnable.getValue ( ) && ( message.contains ( "has accepted your duel request" ) || message.contains ( "Accepted the duel request from" ) ) && ! message.contains ( "<" ) ) {
                Command.sendMessage ( "Tracker will enable in 5 seconds." );
                this.timer.reset ( );
                this.shouldEnable = true;
            } else if ( this.autoDisable.getValue ( ) && message.contains ( "has defeated" ) && message.contains ( PancakesGPS.mc.player.getName ( ) ) && ! message.contains ( "<" ) ) {
                this.disable ( );
            }
        }
    }

    @SubscribeEvent
    public
    void onPacketSend ( PacketEvent.Send event ) {
        if ( ! PancakesGPS.fullNullCheck ( ) && this.isOn ( ) && event.getPacket ( ) instanceof CPacketPlayerTryUseItemOnBlock ) {
            CPacketPlayerTryUseItemOnBlock packet = event.getPacket ( );
            if ( PancakesGPS.mc.player.getHeldItem ( packet.hand ).getItem ( ) == Items.END_CRYSTAL && ! Asset.placedPos.contains ( packet.position ) && ! AutoCrystal.placedPos.contains ( packet.position ) ) {
                this.manuallyPlaced.add ( packet.position );
            }
        }
    }

    @SubscribeEvent
    public
    void onUpdateWalkingPlayer ( UpdateWalkingPlayerEvent event ) {
        if ( this.shouldEnable && this.timer.passedS ( 5.0 ) && this.isOff ( ) ) {
            this.enable ( );
        }
    }

    @Override
    public
    void onUpdate ( ) {
        if ( this.isOff ( ) ) {
            return;
        }
        if ( this.trackedPlayer == null ) {
            this.trackedPlayer = EntityUtil.getClosestEnemy ( 1000.0 );
        } else {
            if ( this.usedStacks != this.usedExp / 64 ) {
                this.usedStacks = this.usedExp / 64;
                Command.sendMessage ( TextUtil.coloredString ( this.trackedPlayer.getName ( ) + " used: " + this.usedStacks + " Stacks of EXP." , this.color.getValue ( ) ) );
            }
            if ( this.usedCStacks != this.usedCrystals / 64 ) {
                this.usedCStacks = this.usedCrystals / 64;
                Command.sendMessage ( TextUtil.coloredString ( this.trackedPlayer.getName ( ) + " used: " + this.usedCStacks + " Stacks of Crystals." , this.color.getValue ( ) ) );
            }
        }
    }

    public
    void onSpawnEntity ( Entity entity ) {
        if ( this.isOff ( ) ) {
            return;
        }
        if ( entity instanceof EntityExpBottle && Objects.equals ( PancakesGPS.mc.world.getClosestPlayerToEntity ( entity , 3.0 ) , this.trackedPlayer ) ) {
            ++ this.usedExp;
        }
        if ( entity instanceof EntityEnderCrystal ) {
            if ( Asset.placedPos.contains ( entity.getPosition ( ).down ( ) ) ) {
                Asset.placedPos.remove ( entity.getPosition ( ).down ( ) );
            } else if ( this.manuallyPlaced.contains ( entity.getPosition ( ).down ( ) ) ) {
                this.manuallyPlaced.remove ( entity.getPosition ( ).down ( ) );
            } else if ( ! AutoCrystal.placedPos.contains ( entity.getPosition ( ).down ( ) ) ) {
                ++ this.usedCrystals;
            }
        }
    }

    @SubscribeEvent
    public
    void onConnection ( ConnectionEvent event ) {
        if ( this.isOff ( ) || event.getStage ( ) != 1 ) {
            return;
        }
        String name = event.getName ( );
        if ( this.trackedPlayer != null && name != null && name.equals ( this.trackedPlayer.getName ( ) ) && this.autoDisable.getValue ( ) ) {
            Command.sendMessage ( name + " logged, Tracker disabling." );
            this.disable ( );
        }
    }

    @Override
    public
    void onToggle ( ) {
        this.manuallyPlaced.clear ( );
        Asset.placedPos.clear ( );
        this.shouldEnable = false;
        this.trackedPlayer = null;
        this.usedExp = 0;
        this.usedStacks = 0;
        this.usedCrystals = 0;
        this.usedCStacks = 0;
    }

    @Override
    public
    void onLogout ( ) {
        if ( this.autoDisable.getValue ( ) ) {
            this.disable ( );
        }
    }

    @SubscribeEvent
    public
    void onDeath ( DeathEvent event ) {
        if ( this.isOn ( ) && ( event.player.equals ( this.trackedPlayer ) || event.player.equals ( PancakesGPS.mc.player ) ) ) {
            this.usedExp = 0;
            this.usedStacks = 0;
            this.usedCrystals = 0;
            this.usedCStacks = 0;
            if ( this.autoDisable.getValue ( ) ) {
                this.disable ( );
            }
        }
    }

    @Override
    public
    String getDisplayInfo ( ) {
        if ( this.trackedPlayer != null ) {
            return this.trackedPlayer.getName ( );
        }
        return null;
    }
}