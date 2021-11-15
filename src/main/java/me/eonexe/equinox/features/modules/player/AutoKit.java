package me.eonexe.equinox.features.modules.player;

import me.eonexe.equinox.event.events.DeathEvent;
import me.eonexe.equinox.features.modules.Module;
import me.eonexe.equinox.features.setting.Bind;
import me.eonexe.equinox.features.setting.Setting;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class AutoKit
extends Module {
    public Setting<String> primaryKit = this.register(new Setting<String>("Primary Kit", "ffa"));
    public Setting<String> secondaryKit = this.register(new Setting<String>("Secondary Kit", "duel"));
    public Setting<Bind> swapBind = this.register(new Setting<Bind>("SwapBind", new Bind(-1)));
    private boolean toggle = false;
    private boolean runThisLife = false;
    public static AutoKit INSTANCE;

    public AutoKit() {
        super("AutoKit", "Automatically does /kit <name>", Module.Category.MISC, true, false, false);
        this.setInstance();
    }

    public static AutoKit getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AutoKit();
        }
        return INSTANCE;
    }

    @Override
    public void onUpdate() {
        if (AutoKit.mc.player.isEntityAlive() && !this.runThisLife) {
            AutoKit.mc.player.connection.sendPacket((Packet)new CPacketChatMessage("/kit " + (this.toggle ? this.secondaryKit.getValue() : this.primaryKit.getValue())));
            this.runThisLife = true;
        }
    }

    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState() && this.swapBind.getValue().getKey() == Keyboard.getEventKey()) {
            this.toggle = !this.toggle;
        }
    }

    @SubscribeEvent
    public void onEntityDeath(DeathEvent event) {
        if (event.player == AutoKit.mc.player) {
            this.runThisLife = false;
        }
    }

    private void setInstance() {
        INSTANCE = this;
    }
}