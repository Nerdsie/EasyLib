package com.collinsrichard.myapi.events.menu;

import com.collinsrichard.myapi.objects.menu.ChestMenu;
import com.collinsrichard.myapi.objects.menu.InventoryLocation;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class OutsideClickEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private Inventory inventory;
    private boolean closeInventory = true;
    private ChestMenu newMenu = null;
    private ChestMenu chestMenu;
    private InventoryLocation location = null;
    private InventoryClickEvent event = null;

    public OutsideClickEvent(InventoryClickEvent e, Player p, int slot, Inventory inv) {
        player = p;
        inventory = inv;
        event = e;

        int x = slot % 9;
        int y = (slot - x) / 9;

        ChestMenu iM = ChestMenu.get(p);
        chestMenu = iM;

        location = new InventoryLocation(getChestMenu(), x, y);
    }

    public InventoryClickEvent getOriginalEvent() {
        return event;
    }

    public void setOriginalEvent(InventoryClickEvent event) {
        this.event = event;
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

    public boolean closeInventory() {
        return closeInventory;
    }

    public void setCloseInventory(boolean closeInventory) {
        this.closeInventory = closeInventory;
    }

    public ChestMenu getChestMenu() {
        return chestMenu;
    }

    public void setChestMenu(ChestMenu chestMenu) {
        this.chestMenu = chestMenu;
    }

    public InventoryLocation getLocation() {
        return location;
    }

    public void setLocation(InventoryLocation location) {
        this.location = location;
    }

    public ChestMenu getNewMenu() {
        return newMenu;
    }

    public void setNewMenu(ChestMenu newMenu) {
        this.newMenu = newMenu;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}