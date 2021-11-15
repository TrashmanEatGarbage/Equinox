package me.eonexe.equinox.features.command.commands;

import me.eonexe.equinox.Equinox;
import me.eonexe.equinox.features.command.Command;

public class ReloadCommand
        extends Command {
    public ReloadCommand() {
        super("reload", new String[0]);
    }

    @Override
    public void execute(String[] commands) {
        Equinox.reload();
    }
}

