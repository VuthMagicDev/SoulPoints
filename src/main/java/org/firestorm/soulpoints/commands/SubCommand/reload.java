package org.firestorm.soulpoints.commands.SubCommand;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.firestorm.soulpoints.Soulpoints;
import org.firestorm.soulpoints.commands.CommandBase;
import org.firestorm.soulpoints.manager.Color;
import org.firestorm.soulpoints.manager.SQLiteManager;

public class reload implements CommandBase {
    private final Soulpoints soulpoints;

    public reload(Soulpoints plugin) {
        this.soulpoints = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("soulpoints.admin")) {
            if (args.length != 1) {
                sender.sendMessage(Color.colorize(soulpoints.getConfigManager().getPrefix() + "&cUsage: /souls reload"));
                return;
            }

            soulpoints.getConfigManager().reloadConfig();
            long time = System.currentTimeMillis();
            long durations = System.currentTimeMillis() - time;
            sender.sendMessage(Color.colorize(soulpoints.getConfigManager().getPrefix() + "&aConfig.yml is reloaded successfully took " + durations + "ms"));
        } else {
            sender.sendMessage(Color.colorize(soulpoints.getConfigManager().getPrefix() + "&cYou don't have permission to use that command!"));
        }
    }
}
