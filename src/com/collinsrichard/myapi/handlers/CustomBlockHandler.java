package com.collinsrichard.myapi.handlers;

import com.collinsrichard.myapi.objects.custom.CustomBlock;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class CustomBlockHandler {
    private CustomBlock block = null;

    public CustomBlock getBlock() {
        return block;
    }

    public CustomBlockHandler setBlock(CustomBlock block) {
        this.block = block;
        return this;
    }

    public void onBlockBreak(BlockBreakEvent e) {

    }

    public void onInteract(PlayerInteractEvent e) {

    }
}
