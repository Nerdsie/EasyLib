package com.collinsrichard.myapi.handlers;

import com.collinsrichard.myapi.objects.custom.CustomItem;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class CustomItemHandler {
    private CustomItem parent = null;

    public final CustomItem getParent() {
        return parent;
    }

    public final void setParent(CustomItem parent) {
        this.parent = parent;
    }

    public void onInteract(PlayerInteractEvent e) {

    }

    public void onInteractWithEntity(PlayerInteractEntityEvent e) {

    }

    public void onInventoryClick(InventoryClickEvent e) {

    }

    public void onDrop(PlayerDropItemEvent e) {
        e.setCancelled(true);

        e.getPlayer().updateInventory();
    }

    public void onBlockBreak(BlockBreakEvent e) {
        e.setCancelled(true);

        e.getPlayer().updateInventory();
    }

    public void onBlockPlace(BlockPlaceEvent e) {
        e.setCancelled(true);

        e.getPlayer().updateInventory();
    }

    public void onProjectileLaunch(ProjectileLaunchEvent e) {
        e.setCancelled(true);
    }
}
