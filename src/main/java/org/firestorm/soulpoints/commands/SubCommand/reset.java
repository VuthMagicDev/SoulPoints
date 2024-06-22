package org.firestorm.soulpoints.commands.SubCommand;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.firestorm.soulpoints.Soulpoints;
import org.firestorm.soulpoints.commands.CommandBase;
import org.firestorm.soulpoints.manager.Color;

public class reset implements CommandBase {
    private final Soulpoints soulpoints;

    public reset(Soulpoints plugin) {
        this.soulpoints = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("soulpoints.admin")) {
            if (args.length != 2) {
                sender.sendMessage(Color.colorize(soulpoints.getConfigManager().getPrefix() + "Usage: /souls reset <player>"));
                return;
            }

            if (sender instanceof Player) {
                String targetPlayerName = args[1];
                Player targetPlayer = Bukkit.getPlayer(targetPlayerName);

                if (targetPlayer == null) {
                    sender.sendMessage(Color.colorize(soulpoints.getConfigManager().getPrefix() + "&cPlayer not found."));
                    return;
                }

                soulpoints.getSouls().setSoulpoints(targetPlayer.getUniqueId(), soulpoints.getSouls().getInitialSoulpoints());
                sender.sendMessage(Color.colorize(soulpoints.getConfigManager().getPrefix() + "&e" + targetPlayerName + "&a's souls is now reset"));
            } else {
                sender.sendMessage("This command can only be used by a player.");
            }
        } else {
            sender.sendMessage(Color.colorize(soulpoints.getConfigManager().getPrefix() + "&cYou don't have permission to use that command!"));
        }
    }
}
