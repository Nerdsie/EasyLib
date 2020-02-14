package com.collinsrichard.myapi.objects.menu;

public class InventoryLocation {
    private int x = 0;
    private int y = 0;
    private ChestMenu menu = null;

    public InventoryLocation(ChestMenu m, int x, int y) {
        this.x = x;
        this.y = y;

        menu = m;
    }

    public InventoryLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public InventoryLocation(int slot) {
        fromSlot(slot);
    }

    public InventoryLocation(ChestMenu m, int slot) {
        fromSlot(slot);

        menu = m;
    }

    public ChestMenu getMenu() {
        return menu;
    }

    public void setMenu(ChestMenu menu) {
        this.menu = menu;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void fromSlot(int slot) {
        x = slot % 9;
        y = (slot - x) / 9;
    }

    public int getSlot() {
        int pos = getX() + (9 * getY());

        return pos;
    }
}
