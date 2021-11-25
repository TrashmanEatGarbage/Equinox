package me.eonexe.equinox.manager;

import me.eonexe.equinox.features.modules.render.Cosmetics;
import me.eonexe.equinox.util.Util;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public
class CosmeticsManager
        implements Util {
    public Map < String, List < ModelBase > > cosmeticsUserMap = new HashMap <> ( );

    public
    CosmeticsManager ( ) {
        this.cosmeticsUserMap.put ( "a5e36d37-5fbe-4481-b5be-1f06baee1f1c" , Arrays.asList ( Cosmetics.INSTANCE.santaHatModel , Cosmetics.INSTANCE.glassesModel ) );
        this.cosmeticsUserMap.put ( "bd93bbcb-4dc4-44c6-bd7e-139f12c674b8" , Arrays.asList ( Cosmetics.INSTANCE.santaHatModel , Cosmetics.INSTANCE.glassesModel ) );

        this.cosmeticsUserMap.put ( "58526350-29f5-4065-96b6-e4a05be9ec5b" , Arrays.asList ( new ModelBase[]{Cosmetics.INSTANCE.santaHatModel} ) );
        this.cosmeticsUserMap.put ( "bd93bbcb-4dc4-44c6-bd7e-139f12c674b8" , Arrays.asList ( new ModelBase[]{Cosmetics.INSTANCE.santaHatModel} ) );
    }

    public
    List < ModelBase > getRenderModels ( EntityPlayer player ) {
        return this.cosmeticsUserMap.get ( player.getUniqueID ( ).toString ( ) );
    }

    public
    boolean hasCosmetics ( EntityPlayer player ) {
        return this.cosmeticsUserMap.containsKey ( player.getUniqueID ( ).toString ( ) );
    }
}