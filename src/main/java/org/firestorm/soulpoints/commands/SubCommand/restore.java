package org.firestorm.soulpoints.commands.SubCommand;

import org.bukkit.command.CommandSender;
import org.firestorm.soulpoints.Soulpoints;
import org.firestorm.soulpoints.commands.CommandBase;
import org.firestorm.soulpoints.manager.Color;

public class restore implements CommandBase {
    private final Soulpoints soulpoints;

    public restore(Soulpoints plugin) {
        this.soulpoints = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("soulpoints.admin")) {
            soulpoints.getSQLiteManager().restoreDatabase();
            sender.sendMessage(Color.colorize(soulpoints.getConfigManager().getPrefix() + "&aDatabase backup created."));
        } else {
            sender.sendMessage(Color.colorize(soulpoints.getConfigManager().getPrefix() + "&cYou don't have permission to use that command!"));
        }
    }
}
