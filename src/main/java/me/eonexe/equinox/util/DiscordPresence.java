package me.eonexe.equinox.util;
import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import me.eonexe.equinox.Equinox;
import me.eonexe.equinox.features.modules.client.RPC;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import java.util.Random;

public class DiscordPresence {
    public static final Logger LOGGER = LogManager.getLogger(Equinox.MODNAME);
    private static Thread thread;
    private static int index;
    private static final DiscordRichPresence discordRichPresence = new DiscordRichPresence();
    private static final DiscordRPC discordRPC = DiscordRPC.INSTANCE;

    public static void start() {
        DiscordEventHandlers eventHandlers = new DiscordEventHandlers();
        String ip;
        if(Minecraft.getMinecraft().isIntegratedServerRunning()) {
            ip = "Fortnite";
        }else {
            ip = Objects.requireNonNull(Minecraft.getMinecraft().getCurrentServerData()).serverIP;
        }
        eventHandlers.disconnected = ((var1, var2) -> System.out.println("Discord RPC disconnected, var1: " + var1 + ", var2: " + var2));

        String discordID = "895233401961975838";
        discordRPC.Discord_Initialize(discordID, eventHandlers, true, null);

        discordRichPresence.startTimestamp = System.currentTimeMillis() / 1000L;
        discordRichPresence.details = "Owning on " + ip;
        discordRichPresence.largeImageKey = "equinox";
        //discordRichPresence.smallImageKey = "jerking_off";
        //discordRichPresence.smallImageText = "Busting a nut in your mom.";
        discordRichPresence.state = presenceDetails[new Random().nextInt(presenceDetails.length)];
        discordRPC.Discord_UpdatePresence(discordRichPresence);
        thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                discordRPC.Discord_RunCallbacks();
                discordRichPresence.largeImageText = Equinox.MODVER;
                //discordRichPresence.details = Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu ? "In the main menu." : "Playing " + (Minecraft.getMinecraft().currentServerData != null ? (RPC.INSTANCE.showIP.getValue().booleanValue() ? "on " + Minecraft.getMinecraft().currentServerData.serverIP + "." : " multiplayer.") : " singleplayer.");
                discordRichPresence.state = presenceDetails[new Random().nextInt(presenceDetails.length)];
                /*if (RPC.getInstance().spazz.getValue().booleanValue()) {
                    discordRichPresence.largeImageKey = "spazzlil";
                }

                if (RPC.getInstance().feminist.getValue().booleanValue()) {
                    if (index == 10) {
                        index = 1;
                    }
                    discordRichPresence.largeImageKey = "girl" + index;
                    ++index;
                }
                if (RPC.getInstance().serverIP.getValue().booleanValue()) {

                }
                 */

                discordRPC.Discord_UpdatePresence(discordRichPresence);
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException interruptedException) {
                }
            }
        }, "RPC-Callback-Handler");
        thread.start();
    }

    public static void stop() {
        discordRPC.Discord_Shutdown();
        discordRPC.Discord_ClearPresence();
    }

    private static final String[] presenceDetails = {
            "Putting 29 on the bin",
            "spazzlarge",
            "stealing spazzlarge's webs",
            "bed bombing web fags",
            "Equinox Just Keeps On Winnin’",
            "You Mad? Equinox Owns You",
            "Eonexe spent 3 hours coding an RPC",
            "Keep looking at the RPC fan",
            "Trashing 5 32k pvpers",
            "bru",
            "Undoxxable -Aztrohh",
            "Life could be dream",
            "ratted by kcp",
            "Proud spawn owners",
            "Professional Teamviewer users",
            "LOL!! BAD BAIT",
            "ctrl c ctrl v",
            "ace refactor user",
            "stop diddling kids",
            "suicide is badass",
            "equinox are magnum dong havers",
            "fetuses aren't human",
            "life begins at birth",
            "birth control is healthcare",
            "Otter being Otter!",
            "JakeOrgy is a bad boi",
            "this was the 69th commit -kcp"

    };
}
