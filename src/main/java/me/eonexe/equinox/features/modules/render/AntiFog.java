package me.eonexe.equinox.features.modules.render;

import me.eonexe.equinox.Equinox;
import me.eonexe.equinox.features.modules.Module;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AntiFog extends Module {

    public AntiFog() {
        super("AntiFog", "removes the fog", Module.Category.RENDER, false,false,false);
    }

    @SubscribeEvent
    public void fogDensity(EntityViewRenderEvent.FogDensity event) {
        event.setDensity(0);
        event.setCanceled(true);

    }
}