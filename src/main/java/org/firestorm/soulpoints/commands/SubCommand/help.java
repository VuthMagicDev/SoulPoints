package org.firestorm.soulpoints.commands.SubCommand;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.firestorm.soulpoints.Soulpoints;
import org.firestorm.soulpoints.commands.CommandBase;
import org.firestorm.soulpoints.manager.Color;

public class help implements CommandBase {
    private final Soulpoints soulpoints;

    public help(Soulpoints plugin) {
        this.soulpoints = plugin;
    }
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("soulpoints.user")) {
            if (args.length != 1) {
                sender.sendMessage(Color.colorize(soulpoints.getConfigManager().getPrefix() + "&cUsage: /souls help"));
                return;
            }

            sender.sendMessage(Color.colorize("&e&m                  &r &6&lHELP COMMAND &e&m                  "));
            sender.sendMessage(Color.colorize("&e/soul check <player> &f- Check soul of other player"));
            sender.sendMessage(Color.colorize("&e/soul reset <player> &f- Reset player soul"));
            sender.sendMessage(Color.colorize("&e/soul [add/remove/set] <player> <amount> &f- Change soul of other player"));
            sender.sendMessage(Color.colorize("&e/soul reload &f- Reload files"));
            sender.sendMessage(Color.colorize("&e&m                                                           "));
        } else {
            sender.sendMessage(Color.colorize(soulpoints.getConfigManager().getPrefix() + "&cYou don't have permission to use that command!"));
        }
    }
}
