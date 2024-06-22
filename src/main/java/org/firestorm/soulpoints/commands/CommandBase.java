package org.firestorm.soulpoints.commands;

import org.bukkit.command.CommandSender;

public interface CommandBase {
    void execute(CommandSender player, String[] args);
}
