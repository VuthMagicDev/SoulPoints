package org.firestorm.soulpoints.commands.SubCommand;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.firestorm.soulpoints.Soulpoints;
import org.firestorm.soulpoints.commands.CommandBase;
import org.firestorm.soulpoints.manager.Color;

public class remove implements CommandBase {
    private final Soulpoints soulpoints;

    public remove(Soulpoints plugin) {
        this.soulpoints = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("soulpoints.admin")) {
            if (args.length != 3) {
                sender.sendMessage(Color.colorize(soulpoints.getConfigManager().getPrefix() + "&cUsage: /souls remove <player> <number>"));
                return;
            }

            String targetPlayerName = args[1];
            String numberString = args[2];
            Player targetPlayer = Bukkit.getPlayer(targetPlayerName);

            if (targetPlayer == null) {
                sender.sendMessage(Color.colorize(soulpoints.getConfigManager().getPrefix() + "&cPlayer not found."));
                return;
            }

            int pointsToAdd;
            try {
                pointsToAdd = Integer.parseInt(numberString);
            } catch (NumberFormatException e) {
                sender.sendMessage(Color.colorize(soulpoints.getConfigManager().getPrefix() + "&cInvalid number format."));
                return;
            }

            if (pointsToAdd > 10) {
                pointsToAdd = 10;
            }

            soulpoints.getSouls().removeSoulpoints(targetPlayer.getUniqueId(), pointsToAdd);

            sender.sendMessage(Color.colorize(soulpoints.getConfigManager().getPrefix() + "&aRemoved &e" + pointsToAdd + " &asoul points to &e" + targetPlayerName));
        } else {
            sender.sendMessage(Color.colorize(soulpoints.getConfigManager().getPrefix() + "&cYou don't have permission to use that command!"));
        }
    }
}
