package com.collinsrichard.myapi.objects.menu;

import com.collinsrichard.myapi.Helper;
import com.collinsrichard.myapi.Settings;
import com.collinsrichard.myapi.events.menu.OutsideClickEvent;
import com.collinsrichard.myapi.handlers.ChestMenuHandler;
import com.collinsrichard.myapi.objects.MetaClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class ChestMenu extends MetaClass {
    // ===================== STATIC METHODS ================ //


    private static HashMap<UUID, ChestMenu> menusOpened = new HashMap<UUID, ChestMenu>();

    public static void removePlayer(String name) {
        name = name.toLowerCase();

        if (menusOpened.containsKey(name)) {
            menusOpened.remove(name);
        }
    }

    public static void removePlayer(Player player) {
        removePlayer(player.getName().toLowerCase());
    }

    public static ChestMenu get(Player p) {
        return getFromUUID(p.getUniqueId());
    }

    public static ChestMenu get(String name) {
        name = name.toLowerCase();

        return getFromUUID(Bukkit.getPlayerExact(name).getUniqueId());
    }

    public static ChestMenu getFromUUID(UUID id) {
        if (menusOpened.containsKey(id)) {
            return menusOpened.get(id);
        }

        return null;
    }

    public static ChestMenu put(Player p, ChestMenu m) {
        return putUsingUUID(p.getUniqueId(), m);
    }

    public static ChestMenu put(String name, ChestMenu m) {
        name = name.toLowerCase();

        return putUsingUUID(Bukkit.getPlayerExact(name).getUniqueId(), m);
    }

    public static ChestMenu putUsingUUID(UUID uuid, ChestMenu m) {
        return menusOpened.put(uuid, m);
    }

    public static List<ChestMenu> getFromMeta(String key, String value) {
        ArrayList<ChestMenu> menus = new ArrayList<ChestMenu>();

        for (Iterator<ChestMenu> m = menusOpened.values().iterator(); m.hasNext(); ) {
            ChestMenu menu = m.next();

            if (menu.hasMeta(key, value)) {
                menus.add(menu);
            }
        }

        return menus;
    }

    public static List<ChestMenu> getFromMeta(String key) {
        ArrayList<ChestMenu> menus = new ArrayList<ChestMenu>();

        for (Iterator<ChestMenu> m = menusOpened.values().iterator(); m.hasNext(); ) {
            ChestMenu menu = m.next();

            if (menu.hasMeta(key)) {
                menus.add(menu);
            }
        }

        return menus;
    }

    public static boolean hasOpen(Player p) {
        return hasOpen(p.getName().toLowerCase());
    }

    public static boolean hasOpen(String name) {
        name = name.toLowerCase();

        return menusOpened.containsKey(name);
    }

    public static boolean isChestMenu(Inventory i) {
        if (i.getName().toLowerCase().contains(Settings.CHEST_MENU_PREFIX)) {
            return true;
        }

        return false;
    }


    // =================== END STATIC ================== //

    private String name;
    private int size;
    private String owner;
    private boolean closeOnInteract = false;
    private boolean organizeBeforeOpen = false;
    private boolean organizeBeforeUpdate = false;
    private ChestMenuHandler handler = new ChestMenuHandler().setMenu(this);
    public ArrayList<ChestIcon> icons = new ArrayList<ChestIcon>();

    /**
     * Setup ChestMenu
     *
     * @param owner Owner of Chest
     * @param name  Name of Chest
     * @param size  Rows in Chest
     */
    public ChestMenu(String owner, String name, int size) {
        this.name = name;
        this.owner = owner;
        setSize(size);
    }

    /**
     * Setup ChestMenu
     *
     * @param owner Owner of Chest
     * @param name  Name of Chest
     * @param size  Rows in Chest
     */
    public ChestMenu(Player owner, String name, int size) {
        this.name = name;
        this.owner = owner.getName();
        setSize(size);
    }

    public final ChestMenuHandler getHandler() {
        return handler;
    }

    public final ChestMenu setHandler(ChestMenuHandler handler) {
        this.handler = handler;
        this.handler.setMenu(this);

        return this;
    }

    public final boolean isOrganizeBeforeUpdate() {
        return organizeBeforeUpdate;
    }

    public final void setOrganizeBeforeUpdate(boolean organizeBeforeUpdate) {
        this.organizeBeforeUpdate = organizeBeforeUpdate;
    }

    public final boolean isOrganizeBeforeOpen() {
        return organizeBeforeOpen;
    }

    public final void setOrganizeBeforeOpen(boolean organizeBeforeOpen) {
        this.organizeBeforeOpen = organizeBeforeOpen;
    }

    public final String getOwnerName() {
        return owner;
    }

    public final void setOwner(String owner) {
        this.owner = owner;
    }

    public final void setOwner(Player owner) {
        this.owner = owner.getName();
    }

    public final boolean isCloseOnInteract() {
        return closeOnInteract;
    }

    public final void setCloseOnInteract(boolean closeOnInteract) {
        this.closeOnInteract = closeOnInteract;
    }

    public final boolean isOwnerOnline() {
        Player player = Bukkit.getPlayerExact(getOwnerName());

        if (player != null && player.isOnline()) {
            return true;
        }

        return false;
    }

    public final Player getOwner() {
        if (isOwnerOnline()) {
            return Bukkit.getPlayerExact(getOwnerName());
        } else {
            return null;
        }
    }

    public final String getName() {
        return name;
    }

    public final void setName(String name) {
        this.name = name;
    }

    public final int getSize() {
        return size;
    }

    public final ChestIcon getIcon(int x, int y) {
        for (ChestIcon i : getIcons()) {
            if (i.getLocation().getX() == x) {
                if (i.getLocation().getY() == y) {
                    return i;
                }
            }
        }

        return null;
    }

    public final ChestIcon getIcon(int slot) {
        for (ChestIcon i : getIcons()) {
            if (i.getLocation().getSlot() == slot) {
                return i;
            }
        }

        return null;
    }

    public final ArrayList<ChestIcon> getIcons() {
        return icons;
    }

    public final ArrayList<ChestIcon> getVisibleIcons() {
        ArrayList<ChestIcon> icos = new ArrayList<ChestIcon>();

        for (ChestIcon i : getIcons()) {
            if (i != null && i.isVisible()) {
                icos.add(i);
            }
        }

        return icos;
    }

    public final void setIcons(ArrayList<ChestIcon> icons) {
        for (ChestIcon i : icons) {
            i.setParent(this);
        }

        this.icons = icons;
    }

    public final void setSize(int size) {
        this.size = size;

        if (size > 0 && size <= 6) {
            this.size *= 9;
        }
    }

    public final ChestMenu addIcon(ChestIcon i) {
        i.setParent(this);

        icons.add(i);

        return this;
    }

    public final void organize() {
        int vis = getVisibleIcons().size();

        int index = 0;

        int rows = vis / 5;
        int lowest = vis % 5;

        int req = rows;
        if (lowest == 0) {
            req = rows - 1;
        }

        for (int i = 0; i <= req; i++) {
            if (i == rows && lowest != 0) {
                for (int x = 0; x < lowest; x++) {
                    ChestIcon icon = getVisibleIcons().get(index);

                    if (lowest % 2 == 0) {
                        int disX = (5 - lowest) + (x * 2);

                        if (icon.getLocation().getX() != disX || icon.getLocation().getY() != i) {
                            icon.setLocation(disX, i);
                        }

                    } else {
                        int disX = (5 - lowest) + x * 2;

                        if (icon.getLocation().getX() != disX || icon.getLocation().getY() != i) {
                            icon.setLocation(disX, i);
                        }
                    }

                    index++;
                }
            } else {
                for (int x = 0; x < 5; x++) {
                    int disX = x * 2;

                    ChestIcon icon = getVisibleIcons().get(index);

                    if (icon.getLocation().getX() != disX || icon.getLocation().getY() != i) {
                        icon.setLocation(disX, i);
                    }

                    index++;
                }
            }
        }

        if (lowest != 0) {
            setSize(rows + 1);
        } else {
            setSize(rows);
        }
    }

    public final void open() {
        final ChestMenu put = this;
        new BukkitRunnable() {

            @Override
            public void run() {
                if (organizeBeforeOpen) {
                    organize();
                }

                Player player = getOwner();

                String title = Settings.CHEST_MENU_PREFIX + name;
                Inventory inventory = Bukkit.createInventory(player, size, title.substring(0, Math.min(32, title.length())));

                for (ChestIcon i : icons) {
                    int pos = i.getLocation().getSlot();

                    if (i.isVisible()) {
                        inventory.setItem(pos, i.getItemStack());
                    }
                }

                boolean close = Helper.safeInventoryClose(player);

                final Player p = player;
                final Inventory i = inventory;

                put(p, put);

                if (close) {
                    if (p.getOpenInventory() != null) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                updateAllIcons();

                                new BukkitRunnable() {

                                    @Override
                                    public void run() {
                                        p.openInventory(i);
                                    }
                                }.runTask(Helper.getAPIPlugin());
                            }
                        }.runTaskLaterAsynchronously(Helper.getAPIPlugin(), 1L);
                    }
                } else {
                    updateAllIcons();

                    p.openInventory(i);
                }
            }
        }.runTaskAsynchronously(Helper.getAPIPlugin());
    }

    public final void open(Player player) {
        setOwner(player);

        open();
    }

    public final void updateOpened(Player player) {
        setOwner(player);
        updateOpened();
    }

    public final void updateOpened() {
        Player player = getOwner();

        for (ChestIcon i : icons) {
            if (i.getOldLocation() != null) {
                Inventory toFix = player.getOpenInventory().getTopInventory();

                toFix.setItem(i.getOldLocation().getSlot(), null);
            }
        }

        for (ChestIcon i : icons) {
            if (i.getLocation() != null) {
                Inventory toFix = player.getOpenInventory().getTopInventory();
                if (i.isVisible()) {

                    toFix.setItem(i.getLocation().getSlot(), i.getItemStack());

                    i.setOldLocation(null);
                } else {
                    toFix.setItem(i.getLocation().getSlot(), null);
                    i.setOldLocation(null);
                }
            }
        }

        put(player, this);
    }

    public final void updateAllIcons() {
        for (ChestIcon icon : getIcons()) {
            icon.getHandler().update();
        }
    }

    public final void updateOthers(ChestIcon exclude) {
        for (ChestIcon icon : getIcons()) {
            if (exclude != icon) {
                icon.getHandler().update();
            }
        }
    }

    public final void open(HumanEntity player) {
        setOwner((Player) player);

        open();
    }

    public final void updateIcon(ChestIcon chestIcon) {
        Player player = getOwner();

        if (isChestMenu(player.getOpenInventory().getTopInventory())) {
            if (get(player).getName().equalsIgnoreCase(getName())) {
                if (organizeBeforeOpen) {
                    organize();
                }

                Inventory toFix = player.getOpenInventory().getTopInventory();

                if (chestIcon.isVisible()) {
                    toFix.setItem(chestIcon.getLocation().getSlot(), chestIcon.getItemStack());
                } else {
                    toFix.setItem(chestIcon.getLocation().getSlot(), null);
                }
            }
        }

        put(player, this);
    }

    public final void onOutsideClick(OutsideClickEvent e) {
        e.setCloseInventory(isCloseOnInteract());
    }
}
