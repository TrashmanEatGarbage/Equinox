package me.eonexe.equinox.features.modules.combat;

import me.eonexe.equinox.Equinox;
import me.eonexe.equinox.event.events.UpdateWalkingPlayerEvent;
import me.eonexe.equinox.features.modules.Module;
import me.eonexe.equinox.features.setting.Setting;
import me.eonexe.equinox.util.Timer;
import me.eonexe.equinox.util.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;

public
class Asset
        extends Module {
    public static Set < BlockPos > placedPos = new HashSet <> ( );
    private final Setting < Integer > coolDown = this.register ( new Setting <> ( "CoolDown" , 400 , 0 , 1000 ) );
    private final Setting < InventoryUtil.Switch > switchMode = this.register ( new Setting <> ( "Switch" , InventoryUtil.Switch.NORMAL ) );
    private final Vec3d[] surroundTargets = {new Vec3d ( 1.0 , 0.0 , 0.0 ) , new Vec3d ( 0.0 , 0.0 , 1.0 ) , new Vec3d ( - 1.0 , 0.0 , 0.0 ) , new Vec3d ( 0.0 , 0.0 , - 1.0 ) , new Vec3d ( 1.0 , 0.0 , - 1.0 ) , new Vec3d ( 1.0 , 0.0 , 1.0 ) , new Vec3d ( - 1.0 , 0.0 , - 1.0 ) , new Vec3d ( - 1.0 , 0.0 , 1.0 ) , new Vec3d ( 1.0 , 1.0 , 0.0 ) , new Vec3d ( 0.0 , 1.0 , 1.0 ) , new Vec3d ( - 1.0 , 1.0 , 0.0 ) , new Vec3d ( 0.0 , 1.0 , - 1.0 ) , new Vec3d ( 1.0 , 1.0 , - 1.0 ) , new Vec3d ( 1.0 , 1.0 , 1.0 ) , new Vec3d ( - 1.0 , 1.0 , - 1.0 ) , new Vec3d ( - 1.0 , 1.0 , 1.0 )};
    private final Timer timer = new Timer ( );
    public Setting < Rotate > rotate = this.register ( new Setting <> ( "Rotate" , Rotate.NORMAL ) );
    public Setting < Boolean > sortY = this.register ( new Setting <> ( "SortY" , true ) );
    private int lastHotbarSlot = - 1;
    private boolean switchedItem;
    private boolean offhand;

    public
    Asset ( ) {
        super ( "Asset" , "need for pancake gps." , Module.Category.COMBAT , true , false , false );
    }

    @Override
    public
    void onEnable ( ) {
        if ( Asset.fullNullCheck ( ) || ! this.timer.passedMs ( this.coolDown.getValue ( ) ) ) {
            this.disable ( );
            return;
        }
        this.lastHotbarSlot = Asset.mc.player.inventory.currentItem;
    }

    @Override
    public
    void onDisable ( ) {
        if ( Asset.fullNullCheck ( ) ) {
            return;
        }
        this.switchItem ( true );
    }

    @SubscribeEvent
    public
    void onUpdateWalkingPlayer ( UpdateWalkingPlayerEvent event ) {
        if ( ! Asset.fullNullCheck ( ) && event.getStage ( ) == 0 ) {
            this.doAntiTrap ( );
        }
    }

    public
    void doAntiTrap ( ) {
        this.offhand = Asset.mc.player.getHeldItemOffhand ( ).getItem ( ) == Items.END_CRYSTAL;
        if ( ! this.offhand && InventoryUtil.findHotbarBlock ( ItemEndCrystal.class ) == - 1 ) {
            this.disable ( );
            return;
        }
        this.lastHotbarSlot = Asset.mc.player.inventory.currentItem;
        ArrayList < Vec3d > targets = new ArrayList <> ( );
        Collections.addAll ( targets , BlockUtil.convertVec3ds ( Asset.mc.player.getPositionVector ( ) , this.surroundTargets ) );
        EntityPlayer closestPlayer = EntityUtil.getClosestEnemy ( 6.0 );
        if ( closestPlayer != null ) {
            targets.sort ( ( vec3d , vec3d2 ) -> Double.compare ( closestPlayer.getDistanceSq ( vec3d2.x , vec3d2.y , vec3d2.z ) , closestPlayer.getDistanceSq ( vec3d.x , vec3d.y , vec3d.z ) ) );
            if ( this.sortY.getValue ( ) ) {
                targets.sort ( Comparator.comparingDouble ( vec3d -> vec3d.y ) );
            }
        }
        for (Vec3d vec3d3 : targets) {
            BlockPos pos = new BlockPos ( vec3d3 );
            if ( ! BlockUtil.canPlaceCrystal ( pos ) ) continue;
            this.placeCrystal ( pos );
            this.disable ( );
            break;
        }
    }

    private
    void placeCrystal ( BlockPos pos ) {
        boolean mainhand;
        mainhand = Asset.mc.player.getHeldItemMainhand ( ).getItem ( ) == Items.END_CRYSTAL;
        if ( ! ( mainhand || this.offhand || this.switchItem ( false ) ) ) {
            this.disable ( );
            return;
        }
        RayTraceResult result = Asset.mc.world.rayTraceBlocks ( new Vec3d ( Asset.mc.player.posX , Asset.mc.player.posY + (double) Asset.mc.player.getEyeHeight ( ) , Asset.mc.player.posZ ) , new Vec3d ( (double) pos.getX ( ) + 0.5 , (double) pos.getY ( ) - 0.5 , (double) pos.getZ ( ) + 0.5 ) );
        EnumFacing facing = result == null || result.sideHit == null ? EnumFacing.UP : result.sideHit;
        float[] angle = MathUtil.calcAngle ( Asset.mc.player.getPositionEyes ( mc.getRenderPartialTicks ( ) ) , new Vec3d ( (float) pos.getX ( ) + 0.5f , (float) pos.getY ( ) - 0.5f , (float) pos.getZ ( ) + 0.5f ) );
        switch (this.rotate.getValue ( )) {
            case NONE: {
                break;
            }
            case NORMAL: {
                Equinox.rotationManager.setPlayerRotations ( angle[0] , angle[1] );
                break;
            }
            case PACKET: {
                Asset.mc.player.connection.sendPacket ( new CPacketPlayer.Rotation ( angle[0] , (float) MathHelper.normalizeAngle ( (int) angle[1] , 360 ) , Asset.mc.player.onGround ) );
                break;
            }
        }
        placedPos.add ( pos );
        Asset.mc.player.connection.sendPacket ( new CPacketPlayerTryUseItemOnBlock ( pos , facing , this.offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND , 0.0f , 0.0f , 0.0f ) );
        Asset.mc.player.swingArm ( EnumHand.MAIN_HAND );
        this.timer.reset ( );
    }

    private
    boolean switchItem ( boolean back ) {
        if ( this.offhand ) {
            return true;
        }
        boolean[] value = InventoryUtil.switchItemToItem ( back , this.lastHotbarSlot , this.switchedItem , this.switchMode.getValue ( ) , Items.END_CRYSTAL );
        this.switchedItem = value[0];
        return value[1];
    }

    public
    enum Rotate {
        NONE,
        NORMAL,
        PACKET

    }
}