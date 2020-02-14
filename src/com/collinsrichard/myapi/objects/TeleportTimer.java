package com.collinsrichard.myapi.objects;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TeleportTimer implements Runnable {
    public Player player = null;
    public Location to = null;

    public int id = 0;

    public boolean active = false;

    public TeleportTimer(Player p, Location to) {
        player = p;
        this.to = to;
    }

    @Override
    public void run() {
        active = true;

        player.teleport(to);
        player.sendMessage(ChatColor.GOLD + "Teleportation commencing...");
    }

}
