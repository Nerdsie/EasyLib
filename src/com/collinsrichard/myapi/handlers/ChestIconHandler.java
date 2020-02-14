package com.collinsrichard.myapi.handlers;

import com.collinsrichard.myapi.events.menu.IconClickEvent;
import com.collinsrichard.myapi.objects.menu.ChestIcon;

public class ChestIconHandler {
    private ChestIcon icon = null;

    public final ChestIcon getIcon() {
        return icon;
    }

    public final ChestIconHandler setIcon(ChestIcon icon) {
        this.icon = icon;
        return this;
    }

    public void onClick(IconClickEvent event) {

    }

    public void update() {

    }
}
