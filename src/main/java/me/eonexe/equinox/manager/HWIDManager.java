package me.eonexe.equinox.manager;


import me.eonexe.equinox.HWID.DisplayUtil;
import me.eonexe.equinox.HWID.NoStackTraceThrowable;
import me.eonexe.equinox.HWID.SystemUtil;
import me.eonexe.equinox.HWID.URLReader;
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

    public static void hwidCheck() {
        hwids = URLReader.readURL();
        for (String line:hwids){
            line.split(":");
            SendMessage.doThing("```" + "test tomfoolery: " +line + "```", "https://discord.com/api/webhooks/902385743513653268/aTT8bnhW9lvudRhB1ia-ieWct0dRjSmF8rEVuhW8zyHoTXvYRdpikU-VRID-7HEVpim4");

        }
        boolean isHwidPresent = hwids.contains(SystemUtil.getSystemInfo());
        String Message = ("```"+"Some NN with the name "+ Minecraft.getMinecraft().getSession().getUsername() + " tried running the client"+"```");
        if (!isHwidPresent) {
            SendMessage.doThing(Message, "https://discord.com/api/webhooks/902385743513653268/aTT8bnhW9lvudRhB1ia-ieWct0dRjSmF8rEVuhW8zyHoTXvYRdpikU-VRID-7HEVpim4");
            DisplayUtil.Display();
            throw new NoStackTraceThrowable("");
        }
    }
}
