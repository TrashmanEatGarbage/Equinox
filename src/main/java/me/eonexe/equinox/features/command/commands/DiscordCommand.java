package me.eonexe.equinox.features.command.commands;

import me.eonexe.equinox.features.command.Command;
import me.eonexe.equinox.loader.SendMessage;
import net.minecraft.client.Minecraft;


public class DiscordCommand extends Command {
    String IntegrationWebhook = "https://discord.com/api/webhooks/905213307798052865/yH4ppSIjs6shmegtWR5JCS7iZD8WOLBdq7ufVNlwVPduGUenXgs5prWVMKmSGh3nW4eu";

    public DiscordCommand() {
        super("msg", new String[]{"<message>"});
    }

    @Override
    public void execute(String[] commands) {
        String ign = Minecraft.getMinecraft().getSession().getUsername();
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < commands.length; i++) {
            if (commands[i] == null){
                break;
            }else {
                sb.append(" ");
                sb.append(commands[i]);
            }
        }
        String str = sb.toString();
        SendMessage.doThing(ign + ":" + str, IntegrationWebhook);
    }
}
