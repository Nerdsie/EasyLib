package com.collinsrichard.myapi.objects.menu;

import com.collinsrichard.myapi.handlers.ChestIconHandler;
import com.collinsrichard.myapi.objects.MetaClass;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class ChestIcon extends MetaClass {
    private InventoryLocation location = null;
    private InventoryLocation oldLocation = null;
    private String name = "";
    private ArrayList<String> lore = new ArrayList<String>();
    private ItemStack itemStack;
    private boolean isVisible = true;
    private ChestMenu parent = null;
    private ChestIconHandler handler = new ChestIconHandler().setIcon(this);

    public ChestIconHandler getHandler() {
        return handler;
    }

    public ChestIcon setHandler(ChestIconHandler handler) {
        this.handler = handler;
        this.handler.setIcon(this);

        return this;
    }

    public final InventoryLocation getOldLocation() {
        return oldLocation;
    }

    public final void setOldLocation(final InventoryLocation oldLocation) {
        this.oldLocation = oldLocation;
    }

    public final void setAmount(int i) {
        itemStack.setAmount(i);
    }

    public final int getAmount() {
        return itemStack.getAmount();
    }

    public final Player getOwner() {
        return getParent().getOwner();
    }

    public final ChestMenu getParent() {
        return parent;
    }

    public final void setParent(ChestMenu parent) {
        this.parent = parent;
    }

    public ChestIcon(int xx, int yy, String n, ItemStack i) {
        setLocation(new InventoryLocation(getParent(), xx, yy));
        name = n;
        itemStack = i;
    }

    public ChestIcon(int slot, String n, ItemStack i) {
        setLocation(new InventoryLocation(getParent(), slot));
        name = n;
        itemStack = i;
    }

    public final ChestIcon addLore(String s) {
        lore.add(s);

        return this;
    }

    public final int getSlot() {
        int pos = getLocation().getSlot();

        return pos;
    }

    public final String getName() {
        return name;
    }

    public final void setName(String name) {
        this.name = name;
    }

    public final ArrayList<String> getLore() {
        return lore;
    }

    public final ItemStack getItemStack() {
        ItemStack toRet = itemStack.clone();

        ItemMeta meta = toRet.getItemMeta();

        meta.setDisplayName(getName());
        meta.setLore(getLore());

        toRet.setItemMeta(meta);

        return toRet;
    }

    public final void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;

        getParent().updateIcon(this);
    }

    public final void setLore(ArrayList<String> l) {
        lore = l;
    }

    public final boolean isVisible() {
        return isVisible;
    }

    public final InventoryLocation getLocation() {
        return location;
    }

    public final void setLocation(InventoryLocation location) {
        setOldLocation(getLocation());
        this.location = location;
    }

    public final void setLocation(int x, int y) {
        setLocation(new InventoryLocation(getParent(), x, y));
    }

    public final void setVisible(boolean is) {
        this.isVisible = is;

        getParent().updateIcon(this);
    }
}
