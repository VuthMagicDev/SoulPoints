package org.firestorm.soulpoints.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.firestorm.soulpoints.Soulpoints;
import org.firestorm.soulpoints.manager.Color;

import java.util.Random;

public class PlayerKill implements Listener {
    private Soulpoints soulpoints;
    private final Random random = new Random();

    public PlayerKill(Soulpoints plugin) {
        this.soulpoints = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (soulpoints.getConfigManager().getPvp()) {
            double MONSTER_KILL_CHANCE = soulpoints.getConfigManager().getPvpChance() / 100.0;
            Player victim = event.getEntity();
            Player killer = victim.getKiller();
            if (killer != null) {
                if (random.nextDouble() < MONSTER_KILL_CHANCE) {
                    soulpoints.getSouls().addSoulpoints(killer.getUniqueId(), soulpoints.getConfigManager().getPvpSoul());
                    killer.sendMessage(Color.colorize("&f[&a+&f] &a" + soulpoints.getConfigManager().getPvpSoul() + " soul"));
                }
            }
        }
    }
}
