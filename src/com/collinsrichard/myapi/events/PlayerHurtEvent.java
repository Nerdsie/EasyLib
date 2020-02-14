package com.collinsrichard.myapi.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerHurtEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private Player player;
	private EntityDamageEvent event;
	private boolean cancelled;

	public PlayerHurtEvent(Player p, EntityDamageEvent e) {
		player = p;
		event = e;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public EntityDamageEvent getEvent() {
		return event;
	}

	public void setEvent(EntityDamageEvent event) {
		this.event = event;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}
