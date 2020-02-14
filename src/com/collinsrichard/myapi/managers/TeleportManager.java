package com.collinsrichard.myapi.managers;

import com.collinsrichard.myapi.objects.TeleportTimer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class TeleportManager {

    private static HashMap<String, TeleportTimer> timers = new HashMap<String, TeleportTimer>();

    public static void teleportPlayer(Player player, Location location, int delay) {
        if (delay == 0) {
            player.teleport(location);
            return;
        }

        player.sendMessage(ChatColor.GOLD + "Teleportation will commence in " + ChatColor.RED + delay + " seconds" + ChatColor.GOLD + ". Don't move.");

        if (isTeleporting(player)) {
            stopTeleporting(player);
        }

        TeleportTimer timer = new TeleportTimer(player, location);
        timer.id = Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("MyAPI"), timer, 20L * delay);

        timers.put(player.getName().toLowerCase(), timer);
    }

    public static boolean isTeleporting(Player p) {
        if (timers.containsKey(p.getName().toLowerCase())) {
            return true;
        }

        return false;
    }

    public static TeleportTimer getTeleport(Player p) {
        if (isTeleporting(p)) {
            TeleportTimer timer = timers.get(p.getName().toLowerCase());

            return timer;
        }

        return null;
    }

    public static void stopTeleporting(Player p) {
        if (isTeleporting(p)) {
            TeleportTimer timer = timers.get(p.getName().toLowerCase());

            Bukkit.getScheduler().cancelTask(timer.id);
            timers.remove(p.getName().toLowerCase());
        }
    }
}
