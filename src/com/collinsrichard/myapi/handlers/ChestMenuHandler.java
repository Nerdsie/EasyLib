package com.collinsrichard.myapi.handlers;

import com.collinsrichard.myapi.events.menu.EmptyClickEvent;
import com.collinsrichard.myapi.events.menu.ItemClickEvent;
import com.collinsrichard.myapi.events.menu.MenuCloseEvent;
import com.collinsrichard.myapi.events.menu.MenuOpenEvent;
import com.collinsrichard.myapi.objects.menu.ChestMenu;

public class ChestMenuHandler {
    private ChestMenu menu = null;

    public ChestMenu getMenu() {
        return menu;
    }

    public ChestMenuHandler setMenu(ChestMenu menu) {
        this.menu = menu;
        return this;
    }

    public void onBlankClick(EmptyClickEvent e) {
        e.setCloseInventory(getMenu().isCloseOnInteract());
    }

    public void onItemClick(ItemClickEvent e) {
        e.setCloseInventory(getMenu().isCloseOnInteract());
    }

    public void onOpen(final MenuOpenEvent event) {
    }

    public void onClose(MenuCloseEvent event) {

    }

    public void update() {
        getMenu().updateAllIcons();
        getMenu().updateOpened();
    }
}
