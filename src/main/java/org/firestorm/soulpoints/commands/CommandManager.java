package org.firestorm.soulpoints.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.firestorm.soulpoints.Soulpoints;
import org.firestorm.soulpoints.commands.SubCommand.*;
import org.firestorm.soulpoints.manager.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager implements CommandExecutor, TabCompleter {

    private final Map<String, CommandBase> commands = new HashMap<>();
    private final Soulpoints soulpoints;

    public CommandManager(Soulpoints plugin) {
        this.soulpoints = plugin;
        registerCommand("reload", new reload(plugin));
        registerCommand("add", new add(plugin));
        registerCommand("set", new set(plugin));
        registerCommand("check", new check(plugin));
        registerCommand("reset", new reset(plugin));
        registerCommand("remove", new remove(plugin));
        registerCommand("help", new help(plugin));
        registerCommand("restore", new restore(plugin));
        registerCommand("backup", new backup(plugin));
    }

    private void registerCommand(String commandName, CommandBase commandExecutor) {
        commands.put(commandName, commandExecutor);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 0) {
                player.sendMessage(Color.colorize(soulpoints.getConfigManager().getPrefix() + "&aYou have &e" + soulpoints.getSouls().getSoulpoints(player.getUniqueId()) + " &aSoul" ));
                return true;
            }

            String subCommand = args[0].toLowerCase();

            if (commands.containsKey(subCommand)) {
                CommandBase commandExecutor = commands.get(subCommand);
                commandExecutor.execute(player, args);
            } else {
                player.sendMessage(Color.colorize(soulpoints.getConfigManager().getPrefix() + "Unknown command. Type /souls for help."));
            }
        } else if (sender instanceof ConsoleCommandSender){
            if (args.length == 0) {
                sender.sendMessage("Usage: /souls <command>");
                return true;
            }

            String subCommand = args[0].toLowerCase();

            if (commands.containsKey(subCommand)) {
                CommandBase commandExecutor = commands.get(subCommand);
                commandExecutor.execute(sender, args);
            } else {
                sender.sendMessage("Unknown command. Type /souls for help.");
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            // Tab completion for the first argument (sub-command)
            String partialCommand = args[0].toLowerCase();

            for (String commandName : commands.keySet()) {
                if (commandName.startsWith(partialCommand)) {
                    completions.add(commandName);
                }
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("add")) {
            // Tab completion for the second argument (player names) when the sub-command is "add"
            String partialPlayer = args[1].toLowerCase();
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().toLowerCase().startsWith(partialPlayer)) {
                    completions.add(player.getName());
                }
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("add")) {
            // Tab completion for the third argument (number) when the sub-command is "add"
            completions.add("<number>"); // Just a placeholder, replace with actual logic if needed
        } else if (args.length == 2 && args[0].equalsIgnoreCase("set")) {
            // Tab completion for the second argument (player names) when the sub-command is "add"
            String partialPlayer = args[1].toLowerCase();
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().toLowerCase().startsWith(partialPlayer)) {
                    completions.add(player.getName());
                }
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("set")) {
            // Tab completion for the third argument (number) when the sub-command is "add"
            completions.add("<number>"); // Just a placeholder, replace with actual logic if needed
        } else if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
            // Tab completion for the second argument (player names) when the sub-command is "add"
            String partialPlayer = args[1].toLowerCase();
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().toLowerCase().startsWith(partialPlayer)) {
                    completions.add(player.getName());
                }
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("remove")) {
            // Tab completion for the third argument (number) when the sub-command is "add"
            completions.add("<number>"); // Just a placeholder, replace with actual logic if needed
        } else if (args.length == 2 && args[0].equalsIgnoreCase("check")) {
            // Tab completion for the second argument (player names) when the sub-command is "add"
            String partialPlayer = args[1].toLowerCase();
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().toLowerCase().startsWith(partialPlayer)) {
                    completions.add(player.getName());
                }
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("reset")) {
            // Tab completion for the second argument (player names) when the sub-command is "add"
            String partialPlayer = args[1].toLowerCase();
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().toLowerCase().startsWith(partialPlayer)) {
                    completions.add(player.getName());
                }
            }
        }

        return completions;
    }
}