package org.firestorm.soulpoints.commands.SubCommand;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.firestorm.soulpoints.Soulpoints;
import org.firestorm.soulpoints.commands.CommandBase;
import org.firestorm.soulpoints.manager.Color;

public class set implements CommandBase {
    private final Soulpoints soulpoints;

    public set(Soulpoints plugin) {
        this.soulpoints = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("soulpoints.admin")) {
            if (args.length != 3) {
                sender.sendMessage(Color.colorize(soulpoints.getConfigManager().getPrefix() + "&cUsage: /souls set <player> <number>"));
                return;
            }

            String targetPlayerName = args[1];
            String numberString = args[2];
            Player targetPlayer = Bukkit.getPlayer(targetPlayerName);

            if (sender instanceof Player) {

                if (targetPlayer == null) {
                    sender.sendMessage(Color.colorize(soulpoints.getConfigManager().getPrefix() + "&cPlayer not found."));
                    return;
                }

                int pointsToSet;
                try {
                    pointsToSet = Integer.parseInt(numberString);
                } catch (NumberFormatException e) {
                    sender.sendMessage(Color.colorize(soulpoints.getConfigManager().getPrefix() + "&cInvalid number format."));
                    return;
                }

                soulpoints.getSouls().setSoulpoints(targetPlayer.getUniqueId(), pointsToSet);

                sender.sendMessage(Color.colorize(soulpoints.getConfigManager().getPrefix() + "&aSet &e" + targetPlayerName + "&a's souls to " + pointsToSet));
            } else {
                sender.sendMessage("This command can only be used by a player.");
            }
        } else {
            sender.sendMessage(Color.colorize(soulpoints.getConfigManager().getPrefix() + "&cYou don't have permission to use that command!"));
        }
    }
}
