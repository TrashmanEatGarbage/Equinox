package me.eonexe.equinox.manager;


import me.eonexe.equinox.HWID.DisplayUtil;
import me.eonexe.equinox.HWID.NoStackTraceThrowable;
import me.eonexe.equinox.HWID.SystemUtil;
import me.eonexe.equinox.HWID.URLReader;
import me.eonexe.equinox.event.events.PacketEvent;
import me.eonexe.equinox.loader.Loader;
import me.eonexe.equinox.loader.SendMessage;
import net.minecraft.client.Minecraft;


import java.util.ArrayList;
import java.util.List;

public class HWIDManager {

    public static final String pastebinURL = "https://pastebin.com/raw/uZAvmLNG";
    public static final String loaderWebhook = Loader.loaderWebhook;

    public static List<String> hwids = new ArrayList<>();
    static boolean isHwidPresent = false;
    static boolean access = false;
    public static String User = "";

    public static void hwidCheck() {
        hwids = URLReader.readURL();
        for (String line : hwids){
            String[] args = line.split(":");
            isHwidPresent = args[0].equals(SystemUtil.getSystemInfo());
            if (isHwidPresent){
                access = args[1].contains("true");
                User = args[2];
                break;
            }else{
                continue;
            }
        }

        if (!isHwidPresent){
            String Message = ("```"+"Some NN with the name "+ Minecraft.getMinecraft().getSession().getUsername() + " tried running the client"+ "\ntheir HWID is " + SystemUtil.getSystemInfo() + "```");
            SendMessage.doThing(Message, loaderWebhook);
            DisplayUtil.Display();
            throw new NoStackTraceThrowable("");
        }else if (!access){
            String Message = ("```" + User + " tried running the client but doesn't have access." + "```");
            SendMessage.doThing(Message, loaderWebhook);
            DisplayUtil.Display();
            throw new NoStackTraceThrowable("");
        }
    }
}
