package com.collinsrichard.myapi.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NewPlayerEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private Player player;

	public NewPlayerEvent(Player p) {
		player = p;
	}

	public Player getPlayer() {
		return player;
	}

	public String getPlayerName() {
		return player.getName();
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
