package com.collinsrichard.myapi.objects.custom;

import com.collinsrichard.myapi.handlers.CustomBlockHandler;
import com.collinsrichard.myapi.objects.MetaClass;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.HashMap;

public class CustomBlock extends MetaClass {
    // ========== STATIC METHODS ================= //

    public static HashMap<Location, CustomBlock> blocks = new HashMap<Location, CustomBlock>();

    public static boolean isCustomBlock(Block clickedBlock) {
        return blocks.containsKey(clickedBlock.getLocation());
    }

    public static CustomBlock getCustomBlock(Block clickedBlock) {
        if (isCustomBlock(clickedBlock)) {
            return blocks.get(clickedBlock.getLocation());
        }

        return null;
    }

    // ========== END STATIC ===================//


    private CustomBlockHandler handler = new CustomBlockHandler();
    private Location location = null;

    public CustomBlock(Location location) {
        this.location = location;
        blocks.put(getLocation(), this);
    }

    public CustomBlockHandler getHandler() {
        return handler;
    }

    public CustomBlock setHandler(CustomBlockHandler handler) {
        this.handler = handler;
        handler.setBlock(this);

        return this;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
