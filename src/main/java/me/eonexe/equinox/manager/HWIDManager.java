package me.eonexe.equinox.manager;


import me.eonexe.equinox.HWID.DisplayUtil;
import me.eonexe.equinox.HWID.NoStackTraceThrowable;
import me.eonexe.equinox.HWID.SystemUtil;
import me.eonexe.equinox.HWID.URLReader;
import me.eonexe.equinox.event.events.PacketEvent;
import me.eonexe.equinox.loader.SendMessage;
import net.minecraft.client.Minecraft;


import java.util.ArrayList;
import java.util.List;

public class HWIDManager {

    /**
     * Your pastebin URL goes inside the empty string below.
     * It should be a raw pastebin link, for example: pastebin.com/raw/pasteid
     */


    public static final String pastebinURL = "https://pastebin.com/raw/uZAvmLNG";

    public static List<String> hwids = new ArrayList<>();
    static boolean isHwidPresent = false;
    static boolean access = false;

    public static void hwidCheck() {
        hwids = URLReader.readURL();
        for (String line : hwids){
            String[] hwid = line.split(":");
            isHwidPresent = hwid[0].contains(SystemUtil.getSystemInfo());
            access = hwid[1].contains("true");
        }
        String Message = ("```"+"Some NN with the name "+ Minecraft.getMinecraft().getSession().getUsername() + " tried running the client"+"```");
        if (!isHwidPresent || !access) {
            SendMessage.doThing(Message, "https://discord.com/api/webhooks/902385743513653268/aTT8bnhW9lvudRhB1ia-ieWct0dRjSmF8rEVuhW8zyHoTXvYRdpikU-VRID-7HEVpim4");
            DisplayUtil.Display();
            throw new NoStackTraceThrowable("");
        }
    }
}
