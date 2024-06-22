package org.firestorm.soulpoints;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.firestorm.soulpoints.commands.CommandManager;
import org.firestorm.soulpoints.listener.Died;
import org.firestorm.soulpoints.listener.MobKill;
import org.firestorm.soulpoints.listener.PlayerKill;
import org.firestorm.soulpoints.manager.*;

import java.util.UUID;

public final class Soulpoints extends JavaPlugin {
    private Souls souls;
    private ConfigManager configManager;
    private SQLiteManager sqLiteManager;
    private BukkitTask rehabilitationTask;
    private BukkitTask backupTask;
    private SoulpointsExpansion soulpointsExpansion;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        saveDefaultConfig();
        configManager.setupConfig();
        souls = new Souls(this);
        sqLiteManager = new SQLiteManager(this);

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            soulpointsExpansion = new SoulpointsExpansion(this);
            soulpointsExpansion.register();
        }

        Bukkit.getPluginManager().registerEvents(new Died(this), this);
        Bukkit.getPluginManager().registerEvents(new MobKill(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerKill(this), this);

        getCommand("souls").setExecutor(new CommandManager(this));

        scheduleRehabilitationTask();
        scheduleBackupTask();

        Bukkit.getConsoleSender().sendMessage(Color.colorize("&a[SoulPoints] has been enabled!"));
    }

    private void scheduleRehabilitationTask() {
        String interval = configManager.getRehabilitateTime();
        long intervalInTicks = configManager.parseTimeStringToTicks(interval);

        rehabilitationTask = Bukkit.getScheduler().runTaskTimer(this, () -> {
            if (configManager.getRehabilitate()) {
                Bukkit.getServer().getOnlinePlayers().forEach(p -> {
                    UUID playerUUID = p.getUniqueId();
                    if (souls.getSoulpoints(playerUUID) < souls.getInitialSoulpoints()) {
                        souls.increaseSoulpoints(playerUUID);
                        p.sendMessage(Color.colorize("&f[&a+&f] " + configManager.getRehabilitateSoul() + " soul"));
                    }
                });
            }
        }, intervalInTicks, intervalInTicks);
    }

    private void scheduleBackupTask() {
        // Replace "24h" with your desired interval string in the config file
        String interval = configManager.getBackuptime();
        long intervalInTicks = configManager.parseTimeStringToTicks(interval);

        backupTask = Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            sqLiteManager.backupDatabase();
            Bukkit.getConsoleSender().sendMessage(Color.colorize("&aDatabase backup completed."));
        }, intervalInTicks, intervalInTicks);
    }

    @Override
    public void onDisable() {
        // Cancel scheduled tasks
        if (rehabilitationTask != null && !rehabilitationTask.isCancelled()) {
            rehabilitationTask.cancel();
        }

        if (backupTask != null && !backupTask.isCancelled()) {
            backupTask.cancel();
        }

        if (soulpointsExpansion != null) {
            soulpointsExpansion.unregister();
        }

        if (sqLiteManager != null) {
            sqLiteManager.closeDataSource();
        }

        Bukkit.getConsoleSender().sendMessage(Color.colorize("&c[SoulPoints] has been enabled!"));
    }

    public Souls getSouls() {
        return souls;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public SQLiteManager getSQLiteManager() {
        return sqLiteManager;
    }
}
