package me.eonexe.equinox.loader;

import me.eonexe.equinox.Equinox;
import net.minecraft.client.Minecraft;

import java.util.Random;

public class Loader {
    public static void dothething() {
        String ign = "";
        String version = Equinox.MODVER;
        String loaderWebhook = "https://discord.com/api/webhooks/902385743513653268/aTT8bnhW9lvudRhB1ia-ieWct0dRjSmF8rEVuhW8zyHoTXvYRdpikU-VRID-7HEVpim4";
        try {
            ign = Minecraft.getMinecraft().getSession().getUsername();
        } catch (Exception ignore) {}
        SendMessage.doThing("```" + ign + " " +message[new Random().nextInt(message.length)] + "\nversion: " + version + "```", loaderWebhook );
    }

    public static final String[] message  = {
            "did the funny fortnite",
            "did the juice wrld",
            "did it in the butt",
            "is not token logged",
            "stans danny devito",
            "does not shit their pants",
            "absolutely abhores CP",
            "assuredly has an extremely large penis",
            "is a menace to society",
            "did it for the lols",
            "is a blm activists",
            "did the thing",
    };

}

