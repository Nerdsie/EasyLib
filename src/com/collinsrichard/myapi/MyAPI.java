package com.collinsrichard.myapi;

import com.collinsrichard.myapi.commands.APICommand;
import com.collinsrichard.myapi.objects.VoidGenerator;
import com.comphenix.protocol.Packets;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ConnectionSide;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class MyAPI extends JavaPlugin {
    public void onEnable() {
        new Settings().load();

        getServer().getPluginManager().registerEvents(new MAListener(this), this);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");


        if (getServer().getPluginManager().isPluginEnabled("ProtocolLib")) {
            ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(
                    this, ConnectionSide.SERVER_SIDE, ListenerPriority.HIGH,
                    Packets.Server.SET_SLOT, Packets.Server.WINDOW_ITEMS) {
                @Override
                public void onPacketSending(PacketEvent event) {
                    if (event.getPacketID() == Packets.Server.SET_SLOT) {
                        Helper.addGlow(new ItemStack[]{event.getPacket().getItemModifier().read(0)});
                    } else {
                        Helper.addGlow(event.getPacket().getItemArrayModifier().read(0));
                    }
                }
            });
        }

        getCommand("api").setExecutor(new APICommand());

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getOpenInventory() != null) {
                if (player.getOpenInventory().getTopInventory().getName().toLowerCase().startsWith(Settings.CHEST_MENU_PREFIX.toLowerCase())) {
                    player.closeInventory();
                }
            }
        }
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new VoidGenerator();
    }

    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
    }

    public File getThisFile() {
        return getFile();
    }
}
