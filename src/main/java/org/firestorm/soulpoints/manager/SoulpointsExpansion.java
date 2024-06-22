package org.firestorm.soulpoints.manager;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.firestorm.soulpoints.Soulpoints;
import org.jetbrains.annotations.NotNull;

public class SoulpointsExpansion extends PlaceholderExpansion {
    private final Soulpoints soulpoints;

    public SoulpointsExpansion(Soulpoints plugin) {
        this.soulpoints = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "soulpoints";
    }

    @Override
    public @NotNull String getAuthor() {
        return soulpoints.getDescription().getAuthors().get(0);
    }

    @Override
    public @NotNull String getVersion() {
        return soulpoints.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true; // This is required to ensure the expansion is not unloaded on reload
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if (player == null) {
            return "";
        }

        if (identifier.equals("soul")) {
            int soul = soulpoints.getSouls().getSoulpoints(player.getUniqueId());
            return String.valueOf(soul);
        } else if (identifier.equals("max_soul")) {
            int max = soulpoints.getSouls().getInitialSoulpoints();
            return String.valueOf(max);
        }

        return null;
    }
}
