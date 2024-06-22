package org.firestorm.soulpoints.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.firestorm.soulpoints.Soulpoints;
import org.firestorm.soulpoints.manager.Color;

import java.util.*;

public class Died implements Listener {
    final Soulpoints soulpoints;

    public Died(Soulpoints plugin) {
        this.soulpoints = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        UUID playerUUID = player.getUniqueId();

        if (soulpoints.getConfigManager().getDeathScreen()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(soulpoints, () -> player.spigot().respawn(), 1);
        }

        soulpoints.getSouls().decreaseSoulpoints(playerUUID);
        int souls = soulpoints.getSouls().getSoulpoints(playerUUID);

        if (souls <= soulpoints.getConfigManager().getLostAllItemSoul()) {
            if (soulpoints.getConfigManager().getLostAllItem()) {
                dropAllItems(player, event);
            }
            player.sendMessage(Color.colorize(soulpoints.getConfigManager().getPrefix() + "&cYou lost all items due to having 0 soulpoints!"));
        } else if (souls <= soulpoints.getConfigManager().getMinToLose()) {
            if (soulpoints.getConfigManager().getMinToLoseDrop()) {
                dropRandomItem(player, event);
            }
            player.sendMessage(Color.colorize(soulpoints.getConfigManager().getPrefix() + "&cYou lost a item due to having " + souls + " soulpoints!"));
        } else {
            event.setKeepInventory(true);
            event.getDrops().clear();
        }
    }

    private void dropRandomItem(Player player, PlayerDeathEvent event) {
        PlayerInventory inventory = player.getInventory();
        List<ItemStack> nonNullItems = new ArrayList<>();
        for (ItemStack item : inventory.getContents()) {
            if (item != null) {
                nonNullItems.add(item);
            }
        }

        ItemStack[] armorContents = inventory.getArmorContents();
        for (ItemStack armor : armorContents) {
            if (armor != null) {
                nonNullItems.add(armor);
            }
        }
        ItemStack offHand = inventory.getItemInOffHand();
        if (offHand != null) {
            nonNullItems.add(offHand);
        }

        if (!nonNullItems.isEmpty()) {
            int randomIndex = (int) (Math.random() * nonNullItems.size());
            ItemStack itemToDrop = nonNullItems.get(randomIndex);
            player.getWorld().dropItemNaturally(player.getLocation(), itemToDrop);
            if (itemToDrop.equals(offHand)) {
                inventory.setItemInOffHand(null);
            } else if (Arrays.asList(armorContents).contains(itemToDrop)) {
                removeArmorPiece(inventory, itemToDrop);
            } else {
                inventory.remove(itemToDrop);
            }
            event.setKeepInventory(true);
            event.getDrops().clear();
        }
    }

    private void removeArmorPiece(PlayerInventory inventory, ItemStack armorPiece) {
        ItemStack[] armorContents = inventory.getArmorContents();
        for (int i = 0; i < armorContents.length; i++) {
            if (armorContents[i] != null && armorContents[i].equals(armorPiece)) {
                armorContents[i] = null; // Remove the specific armor piece
                break;
            }
        }
        inventory.setArmorContents(armorContents); // Update the armor contents
    }

    private void dropAllItems(Player player, PlayerDeathEvent event) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) {
                player.getWorld().dropItemNaturally(player.getLocation(), item);
            }
        }
        player.getInventory().clear();
        event.getDrops().clear();
    }
}
