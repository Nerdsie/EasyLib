package com.collinsrichard.myapi.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDeathEvent;

public class PlayerKillPlayerEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private EntityDeathEvent event;
    private Player killer;
    private Player dead;
    private PlayerHurtPlayerEvent e;
    private boolean direct = true;

    public PlayerKillPlayerEvent(Player k, Player d, EntityDeathEvent e, PlayerHurtPlayerEvent ev, boolean direct) {
        killer = k;
        this.e = ev;
        dead = d;
        event = e;
        this.direct = direct;
    }

    public PlayerHurtPlayerEvent getPlayeHurtEvent() {
        return e;
    }

    public void setPlayeHurtEvent(PlayerHurtPlayerEvent e) {
        this.e = e;
    }

    public boolean isDirect() {
        return direct;
    }

    public void setDirect(boolean direct) {
        this.direct = direct;
    }

    public Player getKiller() {
        return killer;
    }

    public void setKiller(Player killer) {
        this.killer = killer;
    }

    public Player getDead() {
        return dead;
    }

    public void setDead(Player dead) {
        this.dead = dead;
    }

    public EntityDeathEvent getEvent() {
        return event;
    }

    public void setEvent(EntityDeathEvent event) {
        this.event = event;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
