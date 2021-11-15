package me.eonexe.equinox.features.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.eonexe.equinox.Equinox;
import me.eonexe.equinox.features.command.Command;

public class HelpCommand
        extends Command {
    public HelpCommand() {
        super("help");
    }

    @Override
    public void execute(String[] commands) {
        HelpCommand.sendMessage("Commands: ");
        for (Command command : Equinox.commandManager.getCommands()) {
            HelpCommand.sendMessage(ChatFormatting.GRAY + Equinox.commandManager.getPrefix() + command.getName());
        }
    }
}

