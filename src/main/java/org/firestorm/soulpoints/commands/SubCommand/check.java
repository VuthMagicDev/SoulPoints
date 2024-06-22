package org.firestorm.soulpoints.commands.SubCommand;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.firestorm.soulpoints.Soulpoints;
import org.firestorm.soulpoints.commands.CommandBase;
import org.firestorm.soulpoints.manager.Color;

public class check implements CommandBase {

    private final Soulpoints soulpoints;

    public check(Soulpoints plugin) {
        this.soulpoints = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("soulpoints.user")) {
            if (args.length != 2) {
                sender.sendMessage(Color.colorize(soulpoints.getConfigManager().getPrefix() + "&cUsage: /souls reset <player>"));
                return;
            }

            String targetPlayerName = args[1];
            Player targetPlayer = Bukkit.getPlayer(targetPlayerName);

            if (targetPlayer == null) {
                sender.sendMessage("Player not found.");
                return;
            }

            sender.sendMessage(Color.colorize(soulpoints.getConfigManager().getPrefix() + "&e" + targetPlayerName + "&a's have &e" + soulpoints.getSouls().getSoulpoints(targetPlayer.getUniqueId()) + " &aSoul"));
        } else {
            sender.sendMessage(Color.colorize(soulpoints.getConfigManager().getPrefix() + "&cYou don't have permission to use that command!"));
        }
    }
}
