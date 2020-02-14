package com.collinsrichard.myapi.handlers;

import com.collinsrichard.myapi.objects.display.DisplayItem;
import org.bukkit.ChatColor;

public class DisplayItemHandler {
    public DisplayItem displayItem = null;

    public final DisplayItem getDisplayItem() {
        return displayItem;
    }

    public final DisplayItemHandler setDisplayItem(DisplayItem parent) {
        this.displayItem = parent;

        return this;
    }

    public void update() {

    }

    public int display(int line) {
        if (getDisplayItem() == null || getDisplayItem().getDisplay() == null) {
            return line;
        }

        getDisplayItem().getDisplay().setLine(line, ChatColor.values()[line] + "");
        getDisplayItem().getDisplay().setLine(line - 1, getDisplayItem().getDisplayName());
        getDisplayItem().getDisplay().setLine(line - 2, getDisplayItem().getValue());

        return line - 3;
    }
}
