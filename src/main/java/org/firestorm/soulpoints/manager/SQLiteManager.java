package org.firestorm.soulpoints.manager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.firestorm.soulpoints.Soulpoints;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class SQLiteManager {
    private HikariDataSource dataSource;
    private final Soulpoints soulpoints;

    public SQLiteManager(Soulpoints plugin) {
        this.soulpoints = plugin;
        initializeDataSource();
        createTable();
    }

    private void initializeDataSource() {
        String databaseFolder = soulpoints.getDataFolder().getAbsolutePath();
        String connectionString = "jdbc:sqlite:" + databaseFolder + "/soulpoints.db";

        File databaseFile = new File(databaseFolder);
        if (!databaseFile.exists() && !databaseFile.mkdirs()) {
            soulpoints.getLogger().severe("Failed to create database directory: " + databaseFolder);
            return;
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(connectionString);
        config.setMaximumPoolSize(soulpoints.getConfigManager().getDataPool());
        config.setConnectionTestQuery("SELECT 1");
        config.setPoolName("SoulpointsPool");

        dataSource = new HikariDataSource(config);
        soulpoints.getLogger().info("Database connection pool established.");
    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS souls (" +
                "uuid TEXT PRIMARY KEY," +
                "soulpoints INTEGER NOT NULL DEFAULT 10" +
                ")";
        executeUpdate(sql);
    }

    public Connection getConnection() throws SQLException {
        if (dataSource != null && !dataSource.isClosed()) {
            return dataSource.getConnection();
        } else {
            throw new SQLException("DataSource is not initialized or already closed.");
        }
    }

    public void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            soulpoints.getLogger().info("Database connection pool closed.");
        }
    }

    public CompletableFuture<Void> executeUpdateAsync(String sql) {
        return CompletableFuture.runAsync(() -> executeUpdate(sql));
    }

    private void executeUpdate(String sql) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            soulpoints.getLogger().severe("Failed to execute update: " + sql);
        }
    }

    public void backupDatabase() {
        CompletableFuture.runAsync(() -> {
            File backupFile = new File(soulpoints.getDataFolder(), "soulpoints.db.backup");
            try (FileChannel source = new FileInputStream(new File(soulpoints.getDataFolder(), "soulpoints.db")).getChannel();
                 FileChannel destination = new FileOutputStream(backupFile).getChannel()) {
                destination.transferFrom(source, 0, source.size());
                soulpoints.getLogger().info("Database backup created successfully.");
            } catch (Exception e) {
                e.printStackTrace();
                soulpoints.getLogger().severe("Failed to create database backup.");
            }
        });
    }

    public void restoreDatabase() {
        CompletableFuture.runAsync(() -> {
            File backupFile = new File(soulpoints.getDataFolder(), "soulpoints.db.backup");
            try (FileChannel source = new FileInputStream(backupFile).getChannel();
                 FileChannel destination = new FileOutputStream(new File(soulpoints.getDataFolder(), "soulpoints.db")).getChannel()) {
                destination.transferFrom(source, 0, source.size());
                soulpoints.getLogger().info("Database restored successfully.");
            } catch (Exception e) {
                e.printStackTrace();
                soulpoints.getLogger().severe("Failed to restore database.");
            }
        });
    }
}