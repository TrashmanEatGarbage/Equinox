package me.eonexe.equinox.features.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.eonexe.equinox.features.modules.Module;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Timestamps extends Module {
    public Timestamps() {
        super("Timestamps", "Prefixes chat messages with the time", Module.Category.MISC, true, false, false);
    }

    @SubscribeEvent
    public void onClientChatReceived(ClientChatReceivedEvent event) {
        Date date = new Date();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm");
        String strDate = dateFormatter.format(date);
        TextComponentString time = new TextComponentString(ChatFormatting.RED + "<" + ChatFormatting.GRAY + strDate + ChatFormatting.RED + ">" + ChatFormatting.RESET + " ");
        event.setMessage(time.appendSibling(event.getMessage()));
    }
}