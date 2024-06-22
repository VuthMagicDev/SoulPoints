package org.firestorm.soulpoints.listener;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.firestorm.soulpoints.Soulpoints;
import org.firestorm.soulpoints.manager.Color;
import org.firestorm.soulpoints.manager.MonsterTypes;

import java.util.Random;
import java.util.UUID;

public class MobKill implements Listener{
    private Soulpoints soulpoints;
    private final MonsterTypes monsterTypes = new MonsterTypes();
    private final Random random = new Random();

    public MobKill(Soulpoints plugin) {
        this.soulpoints = plugin;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (soulpoints.getConfigManager().getMonster()) {
            double MONSTER_KILL_CHANCE = soulpoints.getConfigManager().getMonsterChance() / 100.0;

            if (event.getEntity().getKiller() != null) {
                Player player = event.getEntity().getKiller();
                UUID playerUUID = player.getUniqueId();
                if (monsterTypes.isMonster(event.getEntity())) {
                    if (random.nextDouble() < MONSTER_KILL_CHANCE) {
                        soulpoints.getSouls().addSoulpoints(playerUUID, soulpoints.getConfigManager().getMonsterSoul());
                        player.sendMessage(Color.colorize("&f[&a+&f] &a" + soulpoints.getConfigManager().getMonsterSoul() + " soul"));
                    }
                }
            } else if (event.getEntity().getLastDamageCause() instanceof Projectile) {
                Projectile projectile = (Projectile) event.getEntity().getLastDamageCause();
                if (projectile.getShooter() instanceof Player) {
                    Player player = (Player) projectile.getShooter();
                    UUID playerUUID = player.getUniqueId();

                    if (monsterTypes.isMonster(event.getEntity())) {
                        if (random.nextDouble() < MONSTER_KILL_CHANCE) {
                            soulpoints.getSouls().addSoulpoints(playerUUID, soulpoints.getConfigManager().getMonsterSoul());
                            player.sendMessage(Color.colorize("&f[&a+&f] &a" + soulpoints.getConfigManager().getMonsterSoul() + " soul"));
                        }
                    }
                }
            }
        }
    }
}
