package me.eonexe.equinox.features.modules.player;

import me.eonexe.equinox.features.modules.Module;
import me.eonexe.equinox.features.setting.Setting;
import me.eonexe.equinox.util.BlockUtil;
import me.eonexe.equinox.util.Util;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public
class AirPlace
        extends Module {
    private final Setting< Mode > mode = this.register ( new Setting <> ( "Mode" , Mode.UP ) );

    public
    AirPlace ( ) {
        super ( "AirPlace" , "Place blocks in the air for gay 1.13+" , Category.PLAYER , false , false , false );
    }

    public
    void onEnable ( ) {
        switch (this.mode.getValue ( )) {
            case UP:
                BlockPos pos = ( Util.mc.player.getPosition ( ).add ( 0 , 1 , 0 ) );
                BlockUtil.placeBlock ( pos , EnumFacing.UP , false );
                disable ( );
            case DOWN:
                pos = ( Util.mc.player.getPosition ( ).add ( 0 , 0 , 0 ) );
                BlockUtil.placeBlock ( pos , EnumFacing.DOWN , false );
                disable ( );
        }
    }

    public
    enum Mode {
        UP,
        DOWN

    }
}