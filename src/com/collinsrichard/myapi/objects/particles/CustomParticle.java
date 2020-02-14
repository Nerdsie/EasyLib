package com.collinsrichard.myapi.objects.particles;

import com.collinsrichard.myapi.Helper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class CustomParticle {
    private ParticleType type = ParticleType.HEART;
    private Location location = null;
    private Float xSpread = 0F;
    private Float ySpread = 0F;
    private Float zSpread = 0F;
    private Float speed = 0F;
    private int amount = 1;

    public CustomParticle(ParticleType t, Location l) {
        type = t;
        location = l;
    }

    public ParticleType getType() {
        return type;
    }

    public CustomParticle type(ParticleType type) {
        this.type = type;

        return this;
    }

    public Location getLocation() {
        return location;
    }

    public CustomParticle location(Location location) {
        this.location = location;

        return this;
    }


    public CustomParticle allSpread(Float spread) {
        xSpread(spread);
        ySpread(spread);
        zSpread(spread);

        return this;
    }

    public Float getXSpread() {
        return xSpread;
    }

    public CustomParticle xSpread(Float xSpread) {
        this.xSpread = xSpread;

        return this;
    }

    public Float getYSpread() {
        return ySpread;
    }

    public CustomParticle ySpread(Float ySpread) {
        this.ySpread = ySpread;

        return this;
    }

    public Float getZSpread() {
        return zSpread;
    }

    public CustomParticle zSpread(Float zSpread) {
        this.zSpread = zSpread;

        return this;
    }

    public Float getSpeed() {
        return speed;
    }

    public CustomParticle speed(Float speed) {
        this.speed = speed;

        return this;
    }

    public int getAmount() {
        return amount;
    }

    public CustomParticle amount(int amount) {
        this.amount = amount;

        return this;
    }

    public CustomParticle show(Player... players) {
        getType().display(getLocation(), getXSpread(), getYSpread(), getZSpread(), getSpeed(), getAmount(), players);

        return this;
    }

    public CustomParticle showAll() {
        show(Helper.toList(Bukkit.getOnlinePlayers()));

        return this;
    }

    public CustomParticle showWhoCanSee(Player check) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getUniqueId().equals(check.getUniqueId()) || player.canSee(check)) {
                show(player);
            }
        }

        return this;
    }

    public CustomParticle show(List<Player> p) {
        Player[] players = new Player[p.size()];

        for (int i = 0; i < p.size(); i++) {
            players[i] = p.get(i);
        }

        show(players);

        return this;
    }

    public CustomParticle show(double radius) {
        show(Helper.getPlayers(getLocation(), radius));

        return this;
    }

    public CustomParticle show() {
        show(ParticleType.DEFAULT_RANGE);

        return this;
    }
}