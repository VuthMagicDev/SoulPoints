package org.firestorm.soulpoints.manager;

import org.bukkit.ChatColor;

public class Color {
    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}