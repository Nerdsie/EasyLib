package com.collinsrichard.myapi.objects.custom;

import com.collinsrichard.myapi.handlers.CustomItemHandler;
import com.collinsrichard.myapi.objects.MetaClass;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomItem extends MetaClass {
    // ============ STATIC METHODS =============== //

    public static ArrayList<CustomItem> items = new ArrayList<CustomItem>();

    public static boolean isCustomItem(ItemStack i) {
        if (i == null) {
            return false;
        }

        for (CustomItem item : items) {
            if (item.isMatch(i)) {
                return true;
            }
        }

        return false;
    }


    public static CustomItem getCustomItem(ItemStack i) {
        if (i == null) {
            return null;
        }

        for (CustomItem item : items) {
            if (item.isMatch(i)) {
                return item;
            }
        }

        return null;
    }

    // =========== END STATIC ============== //

    private Material type = Material.STONE;
    private int amount = 1;
    private byte data = 0;
    private boolean glow = false;
    private String displayName = "";
    private ArrayList<String> lore = new ArrayList<String>();
    private boolean useDisplayName = true;
    private boolean useLore = true;
    private HashMap<Enchantment, Integer> enchants = new HashMap<Enchantment, Integer>();
    private CustomItemHandler handler = new CustomItemHandler();

    public CustomItem() {
        items.add(this);
    }

    public ItemStack getStack() {
        ItemStack toRet = new ItemStack(type, amount, data);

        if (glow) {
            toRet.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 42);
        }

        for (Enchantment e : enchants.keySet()) {
            toRet.addUnsafeEnchantment(e, enchants.get(e));
        }

        ItemMeta meta = toRet.getItemMeta();

        if (useDisplayName && displayName != null) {
            meta.setDisplayName(getDisplayName());
        }

        if (useLore) {
            meta.setLore(getLore());
        }

        toRet.setItemMeta(meta);

        return toRet;
    }

    public CustomItemHandler getHandler() {
        return handler;
    }

    public CustomItem setHandler(CustomItemHandler handler) {
        this.handler = handler;
        handler.setParent(this);

        return this;
    }

    public boolean isMatch(ItemStack i) {

        if (i == null) {
            return false;
        }

        return isMatch(i, true, false, false, false, false);
    }

    public boolean isMatch(ItemStack i, boolean matchData, boolean matchAmount, boolean matchEnchants, boolean matchLore, boolean matchGlow) {
        if (i == null) {
            return false;
        }

        if (i.getType() == type) {
            if (!matchData || i.getDurability() == data) {
                if (!matchAmount || i.getAmount() == amount) {
                    if (!matchEnchants || enchantsMatch(i)) {
                        if (!matchLore || i.getItemMeta().getLore().equals(getLore())) {
                            if (!matchGlow || (glow && i.getEnchantmentLevel(Enchantment.SILK_TOUCH) == 42) || (!glow && !i.getEnchantments().containsKey(Enchantment.SILK_TOUCH))) {
                                if (useDisplayName) {
                                    ItemMeta meta = i.getItemMeta();

                                    if ((meta == null || meta.getDisplayName() == null || meta.getDisplayName().equals("")) && getDisplayName().equalsIgnoreCase("")) {
                                        return true;
                                    }

                                    if (meta.getDisplayName().equalsIgnoreCase(getDisplayName())) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    public HashMap<Enchantment, Integer> getEnchants() {
        return enchants;
    }

    public boolean enchantsMatch(ItemStack i) {
        ItemStack check = i.clone();

        for (Enchantment e : getEnchants().keySet()) {
            int level = getEnchants().get(e);

            if (check.getEnchantments().containsKey(e)) {
                if (check.getEnchantmentLevel(e) == level) {
                    check.removeEnchantment(e);
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }


        return true;
    }

    public Material getType() {
        return type;
    }

    public CustomItem setType(Material type) {
        this.type = type;

        return this;
    }

    public int getAmount() {
        return amount;
    }

    public CustomItem setAmount(int amount) {
        this.amount = amount;

        return this;
    }

    public byte getData() {
        return data;
    }

    public CustomItem setData(byte data) {
        this.data = data;

        return this;
    }

    public CustomItem setData(int data) {
        this.data = (byte) data;

        return this;
    }

    public boolean isGlow() {
        return glow;
    }

    public CustomItem setGlow(boolean glow) {
        this.glow = glow;

        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public CustomItem setDisplayName(String displayName) {
        this.displayName = displayName;

        return this;
    }

    public ArrayList<String> getLore() {
        return lore;
    }

    public CustomItem setLore(ArrayList<String> lore) {
        this.lore = lore;

        return this;
    }

    public CustomItem addLore(String... lore) {
        for (String p : lore) {
            this.lore.add(p);
        }

        return this;
    }

    public CustomItem removeLore(String lore) {
        String args[] = lore.split("\n");

        for (String p : args) {
            if (this.lore.contains(p)) {
                this.lore.remove(p);
            }
        }

        return this;
    }

    public boolean isUseDisplayName() {
        return useDisplayName;
    }

    public CustomItem setUseDisplayName(boolean useDisplayName) {
        this.useDisplayName = useDisplayName;

        return this;
    }

    public boolean isUseLore() {
        return useLore;
    }

    public CustomItem setUseLore(boolean useLore) {
        this.useLore = useLore;

        return this;
    }
}
