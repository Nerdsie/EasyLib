package com.collinsrichard.myapi.objects.firework;


import com.collinsrichard.myapi.Helper;
import com.collinsrichard.myapi.entities.CustomEntityFirework;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * FireworkEffectPlayer v1.0
 * <p/>
 * FireworkEffectPlayer provides a thread-safe and (reasonably) version independant way to instantly explode a FireworkEffect at a given location.
 * You are welcome to use, redistribute, modify and destroy your own copies of this source with the following conditions:
 * <p/>
 * 1. No warranty is given or implied.
 * 2. All damage is your own responsibility.
 * 3. You provide credit publicly to the original source should you release the plugin.
 *
 * @author codename_B
 */
public class CustomFirework {
    // internal references, performance improvements
    private static Method world_getHandle = null;
    private static Method nms_world_broadcastEntityEffect = null;
    private static Method firework_getHandle = null;

    private boolean trail = false;
    private boolean flicker = false;
    private ArrayList<Color> colors = new ArrayList<Color>();
    private ArrayList<Color> fadeColors = new ArrayList<Color>();
    private FireworkEffect.Type type = FireworkEffect.Type.BALL;

    public CustomFirework() {
    }

    public boolean isTrail() {
        return trail;
    }

    public CustomFirework trail(boolean trail) {
        this.trail = trail;

        return this;
    }

    public boolean isFlicker() {
        return flicker;
    }

    public CustomFirework flicker(boolean flicker) {
        this.flicker = flicker;

        return this;
    }

    public CustomFirework addColor(Color... cs) {
        for (Color c : cs) {
            if (!colors.contains(c))
                colors.add(c);
        }

        return this;
    }

    public CustomFirework removeColor(Color... cs) {
        for (Color c : cs) {
            if (colors.contains(c))
                colors.remove(c);
        }

        return this;
    }

    public CustomFirework resetColors() {
        colors.clear();

        return this;
    }

    public CustomFirework clearColors() {
        colors.clear();

        return this;
    }

    public boolean hasColor(Color c) {
        return colors.contains(c);
    }

    public ArrayList<Color> getColors() {
        return colors;
    }

    public CustomFirework addFadeColor(Color... cs) {
        for (Color c : cs) {
            if (!fadeColors.contains(c))
                fadeColors.add(c);
        }

        return this;
    }

    public CustomFirework removeFadeColor(Color... cs) {
        for (Color c : cs) {
            if (fadeColors.contains(c))
                fadeColors.remove(c);
        }

        return this;
    }

    public CustomFirework resetFadeColors() {
        fadeColors.clear();

        return this;
    }

    public CustomFirework clearFadeColors() {
        fadeColors.clear();

        return this;
    }

    public boolean hasFadeColor(Color c) {
        return fadeColors.contains(c);
    }

    public ArrayList<Color> getFadeColors() {
        return colors;
    }

    public FireworkEffect.Type getType() {
        return type;
    }

    public CustomFirework type(FireworkEffect.Type type) {
        this.type = type;

        return this;
    }

    public FireworkEffect getEffect() {
        if (getColors().size() == 0) {
            addColor(Color.RED);
        }

        FireworkEffect.Builder builder = FireworkEffect.builder();
        return builder.flicker(flicker).trail(trail).with(type).withColor(colors).withFade(fadeColors).build();
    }

    public CustomFirework effect(FireworkEffect effect) {
        flicker(effect.hasFlicker());
        trail(effect.hasTrail());
        type(effect.getType());

        clearColors();
        clearFadeColors();

        for (Color c : effect.getColors()) {
            addColor(c);
        }

        for (Color c : effect.getFadeColors()) {
            addFadeColor(c);
        }

        return this;
    }

    public CustomFirework show(Location location) {
        spawn(location);

        return this;
    }

    public CustomFirework play(Location location) {
        spawn(location);

        return this;
    }

    public CustomFirework spawn(Location location, Player... players) {
        playFirework(location, getEffect(), players);

        return this;
    }

    /**
     * Play a pretty firework at the location with the FireworkEffect when called
     *
     * @param loc
     * @param fe
     * @throws Exception
     */
    public static void playFirework(final Location loc, final FireworkEffect fe, final Player... players) {
        new BukkitRunnable() {
            public void run() {
                CustomEntityFirework.spawn(loc, fe, players);
            }
        }.runTask(Helper.getAPIPlugin());
    }

    /**
     * Internal method, used as shorthand to grab our method in a nice friendly manner
     *
     * @param cl
     * @param method
     * @return Method (or null)
     */
    private static Method getMethod(Class<?> cl, String method) {
        for (Method m : cl.getMethods()) {
            if (m.getName().equals(method)) {
                return m;
            }
        }
        return null;
    }

}