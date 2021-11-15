package me.eonexe.equinox.features.command.commands;

import me.eonexe.equinox.Equinox;
import me.eonexe.equinox.features.command.Command;

public class UnloadCommand
        extends Command {
    public UnloadCommand() {
        super("unload", new String[0]);
    }

    @Override
    public void execute(String[] commands) {
        Equinox.unload(true);
    }
}

