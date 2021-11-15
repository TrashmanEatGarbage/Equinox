package me.eonexe.equinox.features.modules.player;

import me.eonexe.equinox.Equinox;
import me.eonexe.equinox.features.modules.Module;

public class Suicide extends Module {
    public Suicide() {
        super("Suicide", "comit die", Category.PLAYER, true, false, false);
    }

    @Override
    public void onEnable() {
        if (Suicide.mc.player != null) {
            Suicide.mc.player.sendChatMessage("/kill");
        }
        this.toggle();
    }
}
