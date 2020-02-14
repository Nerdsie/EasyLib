package com.collinsrichard.myapi.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerHurtPlayerEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private Player attacker;
	private Player victim;
	private EntityDamageByEntityEvent event;
	private boolean cancelled;

	public PlayerHurtPlayerEvent(Player k, Player d, EntityDamageByEntityEvent e) {
		attacker = k;
		victim = d;
		event = e;
	}

	public Player getAttacker() {
		return attacker;
	}

	public void setAttacker(Player attacker) {
		this.attacker = attacker;
	}

	public Player getVictim() {
		return victim;
	}

	public void setVictim(Player victim) {
		this.victim = victim;
	}

	public EntityDamageByEntityEvent getEvent() {
		return event;
	}

	public void setEvent(EntityDamageByEntityEvent event) {
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
