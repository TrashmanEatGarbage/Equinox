package me.eonexe.equinox.features.modules.client;

import me.eonexe.equinox.features.modules.Module;
import me.eonexe.equinox.features.setting.Setting;
import me.eonexe.equinox.util.DiscordPresence;

public class RPC extends Module {
    public static RPC INSTANCE = new RPC();
    //public Setting<Boolean> feminist = this.register(new Setting<Boolean>("Female Mode", false, "troll"));
    //public Setting<Boolean> spazz = this.register(new Setting<Boolean>("Spazzlil Mode", false));
    //public Setting<Boolean> serverIP = this.register(new Setting<Boolean>("display server IP on rpc", false ));
    public RPC() {
        super("DiscordRPC", "DiscordRPC", Module.Category.CLIENT, false, false, false);
    }

    public static RPC getInstance() {
        if (INSTANCE == null)
            INSTANCE = new RPC();
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        DiscordPresence.start();

    }

    @Override
    public void onDisable() {
        DiscordPresence.stop();
    }

    public enum Mode {
        Spazz,
        Equinox

    }
}
