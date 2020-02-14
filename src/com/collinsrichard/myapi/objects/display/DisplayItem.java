package com.collinsrichard.myapi.objects.display;

import com.collinsrichard.myapi.handlers.DisplayItemHandler;
import com.collinsrichard.myapi.objects.MetaClass;

public class DisplayItem extends MetaClass{
    private String displayName = "bDisplayItem";
    private String value = "&cValue";
    private boolean visible = true;
    private Display display = null;
    private int index;
    private DisplayItemHandler handler = new DisplayItemHandler().setDisplayItem(this);

    public DisplayItem(String displayName, String value) {
        this.displayName = displayName;
        this.value = value;
    }

    public DisplayItem(int i, String displayName, String value) {
        index = i;
        this.displayName = displayName;
        this.value = value;
    }

    public DisplayItem(String displayName, String value, DisplayItemHandler handler) {
        this.displayName = displayName;
        this.value = value;
        setHandler(handler);
    }

    public DisplayItem(int i, String displayName, String value, DisplayItemHandler handler) {
        index = i;
        this.displayName = displayName;
        this.value = value;
        setHandler(handler);
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public DisplayItemHandler getHandler() {
        if (handler != null) {
            if (handler.getDisplayItem() == null) {
                handler.setDisplayItem(this);
            }
        }

        return handler;
    }

    public DisplayItem setHandler(DisplayItemHandler handler) {
        handler.setDisplayItem(this);
        this.handler = handler;

        return this;
    }

    public Display getDisplay() {
        return display;
    }

    public void setDisplay(Display display) {
        this.display = display;
    }
}
