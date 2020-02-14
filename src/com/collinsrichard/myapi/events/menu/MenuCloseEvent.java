package com.collinsrichard.myapi.events.menu;

import com.collinsrichard.myapi.objects.menu.ChestMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class MenuCloseEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private Inventory inventory;
    private ChestMenu chestMenu;
    private InventoryCloseEvent event = null;
    private boolean canceled = false;

    public MenuCloseEvent(InventoryCloseEvent e, Player p, Inventory inv) {
        player = p;
        event = e;
        inventory = inv;

        ChestMenu iM = ChestMenu.get(p);
        chestMenu = iM;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public InventoryCloseEvent getOriginalEvent() {
        return event;
    }

    public void setOriginalEvent(InventoryCloseEvent e) {
        event = e;
    }


    public Player getPlayer() {
        return player;
    }

    public String getPlayerName() {
        return player.getName();
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public ChestMenu getChestMenu() {
        return chestMenu;
    }

    public void setChestMenu(ChestMenu chestMenu) {
        this.chestMenu = chestMenu;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}