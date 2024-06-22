package org.firestorm.soulpoints.manager;

import org.firestorm.soulpoints.Soulpoints;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Souls {
    private final Soulpoints soulpoints;

    public Souls(Soulpoints plugin) {
        this.soulpoints = plugin;
    }

    public int getSoulpoints(UUID playerUUID) {
        String query = "SELECT soulpoints FROM souls WHERE uuid = ?";
        try (Connection connection = soulpoints.getSQLiteManager().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerUUID.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("soulpoints");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return soulpoints.getConfigManager().getDefaultSoul(); // Default value
    }

    public void setSoulpoints(UUID playerUUID, int souls) {
        int currentSoulpoints = getSoulpoints(playerUUID);
        if (currentSoulpoints != souls) {
            String update = "REPLACE INTO souls (uuid, soulpoints) VALUES (?, ?)";
            try (Connection connection = soulpoints.getSQLiteManager().getConnection();
                 PreparedStatement statement = connection.prepareStatement(update)) {
                statement.setString(1, playerUUID.toString());
                statement.setInt(2, souls);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void decreaseSoulpoints(UUID playerUUID) {
        int currentSoulpoints = getSoulpoints(playerUUID);
        setSoulpoints(playerUUID, Math.max(0, currentSoulpoints - soulpoints.getConfigManager().getSoulLose()));
    }

    public void increaseSoulpoints(UUID playerUUID) {
        int currentSoulpoints = getSoulpoints(playerUUID);
        int maxSoulpoints = soulpoints.getConfigManager().getDefaultSoulMax();
        int newSoulpoints = Math.min(maxSoulpoints, currentSoulpoints + soulpoints.getConfigManager().getRehabilitateSoul());
        if (newSoulpoints > currentSoulpoints) {
            setSoulpoints(playerUUID, newSoulpoints);
        }
    }

    public void addSoulpoints(UUID playerUUID, int amount) {
        int currentSoulpoints = getSoulpoints(playerUUID);
        int maxSoulpoints = soulpoints.getConfigManager().getDefaultSoulMax();
        int newSoulpoints = Math.min(maxSoulpoints, currentSoulpoints + amount);
        if (newSoulpoints > currentSoulpoints) {
            setSoulpoints(playerUUID, newSoulpoints);
        }
    }

    public void removeSoulpoints(UUID playerUUID, int amount) {
        int currentSoulpoints = getSoulpoints(playerUUID);
        setSoulpoints(playerUUID, Math.max(0, currentSoulpoints - amount));
    }

    public int getInitialSoulpoints() {
        return soulpoints.getConfigManager().getDefaultSoulMax();
    }
}
