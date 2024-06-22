package org.firestorm.soulpoints.manager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.firestorm.soulpoints.Soulpoints;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigManager {

    private FileConfiguration config;
    private File configFile;
    private final Soulpoints plugin;

    public ConfigManager(Soulpoints plugin) {
        this.plugin = plugin;
    }

    public void setupConfig() {
        configFile = new File(plugin.getDataFolder(), "config.yml");

        // Create the data folder if it doesn't exist
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        plugin.getLogger().info("Data folder path: " + plugin.getDataFolder().getAbsolutePath());

        if (!configFile.exists()) {
            // Copy the default config from resources
            try (InputStream defaultConfigStream = plugin.getResource("config.yml")) {
                if (defaultConfigStream != null && defaultConfigStream.available() > 0) {
                    plugin.getLogger().info("Copying default config from resources...");
                    Files.copy(defaultConfigStream, configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    plugin.getLogger().info("Default config copied successfully!");
                } else {
                    plugin.getLogger().warning("Default config.yml not found in resources or is empty!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        config = new YamlConfiguration(); // Use a custom YamlConfiguration instance
        try {
            config.load(configFile);
        } catch (IOException | org.bukkit.configuration.InvalidConfigurationException e) {
            e.printStackTrace();
        }
        plugin.getLogger().info("Config loaded from: " + configFile.getAbsolutePath());

        // Debug: Print the loaded config
//        plugin.getLogger().info("Loaded config content:");
//        for (String key : config.getKeys(true)) {
//            plugin.getLogger().info(key + ": " + config.get(key));
//        }
    }

    public FileConfiguration getConfig() {
        if (config == null) {
            setupConfig();
        }
        return config;
    }

    public void saveConfig() {
        if (config == null || configFile == null) {
            return;
        }
        try {
            getConfig().save(configFile);
            plugin.getLogger().warning("Config saved!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadConfig() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), "config.yml");
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public boolean getDeathScreen() {
        return config.getBoolean("death.skip_death_screen");
    }

    public String getPrefix() {
        return config.getString("settings.prefix");
    }

    public int getDefaultSoul() {
        return config.getInt("settings.default_soul");
    }
    public int getDefaultSoulMax() {
        return config.getInt("settings.default_soul_max");
    }

    public int getSoulLose() {
        return config.getInt("death.soul_lost");
    }


    public Boolean getRehabilitate() {
        return config.getBoolean("rehabilitate.enable");
    }

    public int getRehabilitateSoul() {
        return config.getInt("rehabilitate.soul");
    }

    public String getRehabilitateTime() {
        return config.getString("rehabilitate.time");
    }

    public Boolean getLostAllItem() {
        return config.getBoolean("death.lost_all_item");
    }

    public int getLostAllItemSoul() {
        return config.getInt("death.lost_all_item_soul");
    }

    public Boolean getMinToLoseDrop() {
        return config.getBoolean("death.min_to_lose_drop");
    }

    public int getMinToLose() {
        return config.getInt("death.min_to_lose");
    }

    public double getPvpChance() {
        return config.getDouble("pvp.chance");
    }

    public boolean getPvp() {
        return config.getBoolean("pvp.enable");
    }

    public int getPvpSoul() {
        return config.getInt("pvp.kill_soul");
    }

    public double getMonsterChance() {
        return config.getDouble("monster.chance");
    }

    public boolean getMonster() {
        return config.getBoolean("monster.enable");
    }

    public int getMonsterSoul() {
        return config.getInt("monster.kill_soul");
    }

    public int getDataPool() {
        return config.getInt("database.pool-size");
    }

    public String getBackuptime() {
        return config.getString("backup.interval");
    }

    public long parseTimeStringToTicks(String timeString) {
        long ticks = 0;
        Pattern pattern = Pattern.compile("(\\d+)([smhd])");
        Matcher matcher = pattern.matcher(timeString);

        while (matcher.find()) {
            int value = Integer.parseInt(matcher.group(1));
            String unit = matcher.group(2);

            switch (unit) {
                case "s":
                    ticks += value * 20L;
                    break;
                case "m":
                    ticks += value * 60L * 20L;
                    break;
                case "h":
                    ticks += value * 60L * 60L * 20L;
                    break;
                case "d":
                    ticks += value * 24L * 60L * 60L * 20L;
                    break;
            }
        }
        return ticks;
    }
}
