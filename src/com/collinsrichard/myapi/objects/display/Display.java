package com.collinsrichard.myapi.objects.display;

import com.collinsrichard.myapi.Helper;
import com.collinsrichard.myapi.Settings;
import com.collinsrichard.myapi.objects.MetaClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

public class Display extends MetaClass {
    // ========== STATIC METHODS =========== //


    public static HashMap<UUID, Display> displays = new HashMap<UUID, Display>();
    public static HashMap<Display, BukkitTask> autoupdaters = new HashMap<Display, BukkitTask>();

    public static boolean has(String name) {
        return has(Bukkit.getPlayerExact(name));
    }

    public static boolean has(Player player) {
        if (player == null) {
            return false;
        }

        return has(player.getUniqueId());
    }

    public static boolean has(UUID id) {
        return displays.containsKey(id);
    }

    public static void set(String name, Display display) {
        set(Bukkit.getPlayerExact(name), display);
    }

    public static void set(Player player, Display display) {
        if (player != null) {
            set(player.getUniqueId(), display);
        }
    }

    public static void set(UUID id, Display display) {
        displays.put(id, display);
    }

    public static void remove(String name) {
        remove(Bukkit.getPlayerExact(name));
    }

    public static void remove(Player player) {
        if (player == null) {
            return;
        }

        remove(player.getUniqueId());
    }

    public static void remove(UUID id) {
        displays.remove(id);
    }

    public static Display get(String name) {
        return get(Bukkit.getPlayerExact(name));
    }

    public static Display get(Player player) {
        if (player == null) {
            return null;
        }

        return get(player.getUniqueId());
    }

    public static Display get(UUID id) {
        return displays.get(id);
    }

    // =========== END STATIC ============= //

    private ArrayList<DisplayItem> items = new ArrayList<DisplayItem>();
    private String displayName = "Display";
    private UUID owner = null;
    private boolean visible = true;

    private HashMap<Integer, String> lines = new HashMap<Integer, String>();

    public Display(Player owner, String displayName) {
        this.owner = owner.getUniqueId();
        this.displayName = displayName;

        set(owner, this);

        resetDisplay();
    }

    public void resetDisplay() {
        try {
            Scoreboard scoreboard = getPlayer().getScoreboard();
            Objective objective = scoreboard.getObjective(Settings.DISPLAY_NAME);

            if (objective != null) {
                objective.unregister();
            }
        } catch (Exception e) {
        }
    }

    public Objective getObjective() {
        final Scoreboard scoreboard = getPlayer().getScoreboard();
        Objective obj = scoreboard.getObjective(Settings.DISPLAY_NAME);

        if (obj == null) {
            obj = scoreboard.registerNewObjective(Settings.DISPLAY_NAME, "dummy");

            obj.setDisplaySlot(DisplaySlot.SIDEBAR);

            update();
        }

        if (obj.getDisplayName() != getDisplayName()) {
            obj.setDisplayName(displayName);
        }

        return obj;
    }

    public void setLine(int line, String string) {
        if (lines.containsKey(line)) {
            resetLine(line);
        }

        Score set = getObjective().getScore(string);
        set.setScore(line);

        lines.put(line, string);
    }

    public void resetLine(int line) {
        try {
            getPlayer().getScoreboard().resetScores(lines.get(line));
            lines.remove(line);
        } catch (Exception e) {

        }
    }

    public void update() {
        new BukkitRunnable() {
            public void run() {
                if (owner == null || getPlayer() == null || !getPlayer().isOnline()) {
                    return;
                }

                if (!isVisible()) {
                    return;
                }

                if (getPlayer().getScoreboard() == null) {
                    resetDisplay();
                }

                for (DisplayItem item : getSortedInfo()) {
                    if (item.getHandler() != null) {
                        try {
                            item.getHandler().update();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                ArrayList<DisplayItem> parse = getSortedInfo();

                int count = 15;

                for (DisplayItem item : parse) {
                    if (count <= 0) {
                        return;
                    }

                    count = item.getHandler().display(count);
                }
            }
        }.runTaskAsynchronously(Helper.getAPI());
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(owner);
    }

    public ArrayList<DisplayItem> getInfo() {
        return items;
    }

    public ArrayList<DisplayItem> getSortedInfo() {
        ArrayList<DisplayItem> parse = getInfo();

        ArrayList<DisplayItem> info = new ArrayList<DisplayItem>();

        for (DisplayItem i : parse) {
            if (i.isVisible()) {
                info.add(i);
            }
        }

        Collections.sort(info, new Comparator<DisplayItem>() {

            @Override
            public int compare(DisplayItem x, DisplayItem y) {
                int indexX = x.getIndex();
                int indexY = y.getIndex();

                return (indexX < indexY) ? -1 : (indexY > indexX) ? 1 : 0;
            }
        });

        return info;
    }

    public DisplayItem addInfo(DisplayItem i) {
        i.setDisplay(this);
        items.add(i);

        return i;
    }

    public String getDisplayName() {
        return displayName;
    }

    public final void setDisplayName(String displayName) {
        this.displayName = displayName;
        update();
    }

    public void destroy() {
        getPlayer().getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
        getPlayer().getScoreboard().getObjective(Settings.DISPLAY_NAME).unregister();
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void delayedUpdate(Long delay) {
        new BukkitRunnable() {

            @Override
            public void run() {
                update();
            }
        }.runTaskLater(Helper.getAPIPlugin(), delay);
    }
}
