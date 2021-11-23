package me.eonexe.equinox.features.modules.player;

import me.eonexe.equinox.features.command.Command;
import me.eonexe.equinox.features.modules.Module;
import me.eonexe.equinox.features.setting.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;

import java.util.HashSet;
import java.util.Set;

public
class OppDetector
        extends Module {
    private final Set < Entity > ghasts = new HashSet <> ( );
    public Setting < Boolean > Chat = this.register ( new Setting <> ( "Chat" , true ) );
    public Setting < Boolean > Sound = this.register ( new Setting <> ( "Sound" , true ) );

    public
    OppDetector ( ) {
        super ( "OppDetector" , "Helps you find OppDetector" , Category.PLAYER , true , false , false );
    }

    @Override
    public
    void onEnable ( ) {
        this.ghasts.clear ( );
    }

    @Override
    public
    void onUpdate ( ) {
        for (Entity entity : OppDetector.mc.world.getLoadedEntityList ( )) {
            if ( ! ( entity instanceof EntityPlayer ) || this.ghasts.contains ( entity ) ) continue;
            if ( this.Chat.getValue ( ) ) {
                Command.sendMessage ( "Opp Detected at: " + entity.getPosition ( ).getX ( ) + "x, " + entity.getPosition ( ).getY ( ) + "y, " + entity.getPosition ( ).getZ ( ) + "z." );
            }
            this.ghasts.add ( entity );
            if ( ! this.Sound.getValue ( ) ) continue;
            OppDetector.mc.player.playSound ( SoundEvents.BLOCK_ANVIL_DESTROY , 1.0f , 1.0f );
        }
    }
}