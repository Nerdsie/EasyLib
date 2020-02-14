package com.collinsrichard.myapi.objects.chat.dynamic;

import com.collinsrichard.myapi.Helper;
import org.bukkit.Achievement;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.Statistic.Type;
import org.bukkit.craftbukkit.libs.com.google.gson.stream.JsonWriter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class DynamicMessage {

    private final List<MessagePart> messageParts;
    private boolean inheritColors = true;
    private String jsonString;
    private boolean dirty;

    private ArrayList<ChatColor> latestColors = new ArrayList<ChatColor>();
    private ArrayList<ChatColor> nextColors = new ArrayList<ChatColor>();

    private Class<?> nmsChatSerializer = Reflection.getNMSClass("ChatSerializer");
    private Class<?> nmsTagCompound = Reflection.getNMSClass("NBTTagCompound");
    private Class<?> nmsPacketPlayOutChat = Reflection.getNMSClass("PacketPlayOutChat");
    private Class<?> nmsAchievement = Reflection.getNMSClass("Achievement");
    private Class<?> nmsStatistic = Reflection.getNMSClass("Statistic");
    private Class<?> nmsItemStack = Reflection.getNMSClass("ItemStack");

    private Class<?> obcStatistic = Reflection.getOBCClass("CraftStatistic");
    private Class<?> obcItemStack = Reflection.getOBCClass("inventory.CraftItemStack");

    public DynamicMessage(final String firstPartText) {
        messageParts = new ArrayList<MessagePart>();
        messageParts.add(new MessagePart(firstPartText));
        jsonString = null;
        dirty = false;
    }

    public boolean isInheritColors() {
        return inheritColors;
    }

    public DynamicMessage setInheritColors(boolean inheritColors) {
        this.inheritColors = inheritColors;

        return this;
    }

    public DynamicMessage clearColors() {
        latestColors.clear();

        return this;
    }

    public DynamicMessage color(final ChatColor color) {
        if (isInheritColors()) {
            latestColors.add(color);
        } else {
            latestColors.clear();
        }

        if (!color.isColor()) {
            return style(color);
        }
        latest().color = color;
        dirty = true;

        return this;
    }

    public DynamicMessage style(final ChatColor... styles) {

        for (final ChatColor style : styles) {
            if (isInheritColors()) {
                latestColors.add(style);
            } else {
                latestColors.clear();
            }

            if (!style.isFormat()) {
                throw new IllegalArgumentException(style.name() + " is not a style");
            }
        }
        latest().styles = styles;
        dirty = true;
        return this;
    }

    public DynamicMessage file(final String path) {
        onClick("open_file", path);
        return this;
    }

    public DynamicMessage link(final String url) {
        onClick("open_url", url);
        return this;
    }

    public DynamicMessage suggest(final String command) {
        onClick("suggest_command", command);
        return this;
    }

    public DynamicMessage command(final String command) {
        onClick("run_command", command);
        return this;
    }

    public DynamicMessage achievementTooltip(final String name) {
        onHover("show_achievement", "achievement." + name);
        return this;
    }

    public DynamicMessage achievementTooltip(final Achievement which) {
        try {
            Object achievement = Reflection.getMethod(obcStatistic, "getNMSAchievement").invoke(null, which);
            return achievementTooltip((String) Reflection.getField(nmsAchievement, "name").get(achievement));
        } catch (Exception e) {
            e.printStackTrace();
            return this;
        }
    }

    public DynamicMessage statisticTooltip(final Statistic which) {
        Type type = which.getType();
        if (type != Type.UNTYPED) {
            throw new IllegalArgumentException("That statistic requires an additional " + type + " parameter!");
        }
        try {
            Object statistic = Reflection.getMethod(obcStatistic, "getNMSStatistic").invoke(null, which);
            return achievementTooltip((String) Reflection.getField(nmsStatistic, "name").get(statistic));
        } catch (Exception e) {
            e.printStackTrace();
            return this;
        }
    }

    public DynamicMessage statisticTooltip(final Statistic which, Material item) {
        Type type = which.getType();
        if (type == Type.UNTYPED) {
            throw new IllegalArgumentException("That statistic needs no additional parameter!");
        }
        if ((type == Type.BLOCK && item.isBlock()) || type == Type.ENTITY) {
            throw new IllegalArgumentException("Wrong parameter type for that statistic - needs " + type + "!");
        }
        try {
            Object statistic = Reflection.getMethod(obcStatistic, "getMaterialStatistic").invoke(null, which, item);
            return achievementTooltip((String) Reflection.getField(nmsStatistic, "name").get(statistic));
        } catch (Exception e) {
            e.printStackTrace();
            return this;
        }
    }

    public DynamicMessage statisticTooltip(final Statistic which, EntityType entity) {
        Type type = which.getType();
        if (type == Type.UNTYPED) {
            throw new IllegalArgumentException("That statistic needs no additional parameter!");
        }
        if (type != Type.ENTITY) {
            throw new IllegalArgumentException("Wrong parameter type for that statistic - needs " + type + "!");
        }
        try {
            Object statistic = Reflection.getMethod(obcStatistic, "getEntityStatistic").invoke(null, which, entity);
            return achievementTooltip((String) Reflection.getField(nmsStatistic, "name").get(statistic));
        } catch (Exception e) {
            e.printStackTrace();
            return this;
        }
    }

    public DynamicMessage itemTooltip(final String itemJSON) {
        onHover("show_item", itemJSON);
        return this;
    }

    public DynamicMessage itemTooltip(final ItemStack itemStack) {
        try {
            Object nmsItem = Reflection.getMethod(obcItemStack, "asNMSCopy", ItemStack.class).invoke(null, itemStack);
            return itemTooltip(Reflection.getMethod(nmsItemStack, "save").invoke(nmsItem, nmsTagCompound.newInstance()).toString());
        } catch (Exception e) {
            e.printStackTrace();
            return this;
        }
    }

    public DynamicMessage tooltip(final String text) {
        return tooltip(text.split("\\n"));
    }

    public DynamicMessage tooltip(final List<String> lines) {
        return tooltip((String[]) lines.toArray());
    }

    public DynamicMessage tooltip(final String... lines) {
        if (lines.length == 1) {
            onHover("show_text", lines[0]);
        } else {
            itemTooltip(makeMultilineTooltip(lines));
        }
        return this;
    }

    public DynamicMessage add(final Object obj) {
        messageParts.add(new MessagePart(obj.toString()));
        dirty = true;

        if (!nextColors.isEmpty()) {
            for (ChatColor c : nextColors) {
                color(c);
            }

            nextColors.clear();
        }

        return this;
    }

    public DynamicMessage addAndFormat(final Object obj) {
        List<String> colors = new ArrayList<String>();
        List<String> text = new ArrayList<String>();
        String s = (String) obj;

        s = Helper.parse(s);

        boolean b = false;
        for (int i = 0; i < s.length(); i++) {
            try {
                if (s.charAt(i) == '§') continue;
                if (i > 0 && s.charAt(i - 1) == '§') {
                    if (b) {
                        int index = colors.size() - 1;
                        String element = colors.get(index);
                        element += "," + s.charAt(i);
                        colors.set(index, element);
                    } else {
                        colors.add("" + s.charAt(i));
                        b = true;
                    }
                } else {
                    StringBuilder sb = new StringBuilder();
                    int index = s.indexOf('§', i);
                    if (index == -1) index = s.length();
                    while (i < index) {
                        sb.append(s.charAt(i));
                        i++;
                    }
                    text.add(sb.toString());
                    b = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < text.size(); i++) {
            String string = text.get(i);
            String codes = colors.get(i);

            ArrayList<ChatColor> cs = new ArrayList<ChatColor>();

            for (String parse : codes.split(",")) {
                cs.add(Helper.toChatColor(parse));
            }

            add(string);

            for (ChatColor col : cs) {
                this.color(col);
            }
        }

        if (colors.size() > text.size()) {
            int index = colors.size() - (colors.size() - text.size());

            for (int i = index; i < colors.size(); i++) {
                String codes = colors.get(i);

                ArrayList<ChatColor> cs = new ArrayList<ChatColor>();

                for (String parse : codes.split(",")) {
                    cs.add(Helper.toChatColor(parse));
                }

                for (ChatColor col : cs) {
                    nextColors.add(col);
                }
            }
        }

        return this;
    }

    public DynamicMessage then(final Object obj) {
        return add(obj);
    }

    public String toJSONString() {
        if (!dirty && jsonString != null) {
            return jsonString;
        }
        StringWriter string = new StringWriter();
        JsonWriter json = new JsonWriter(string);
        try {
            if (messageParts.size() == 1) {
                latest().writeJson(json);
            } else {
                json.beginObject().name("text").value("").name("extra").beginArray();
                for (final MessagePart part : messageParts) {
                    part.writeJson(json);
                }
                json.endArray().endObject();
                json.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("invalid message");
        }
        jsonString = string.toString();
        dirty = false;
        return jsonString;
    }

    public void send(Player player) {
        try {
            Object handle = Reflection.getHandle(player);
            Object connection = Reflection.getField(handle.getClass(), "playerConnection").get(handle);
            Object serialized = Reflection.getMethod(nmsChatSerializer, "a", String.class).invoke(null, toJSONString());
            Object packet = nmsPacketPlayOutChat.getConstructor(Reflection.getNMSClass("IChatBaseComponent")).newInstance(serialized);
            Reflection.getMethod(connection.getClass(), "sendPacket").invoke(connection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(Player player, String s) {
        try {
            Object handle = Reflection.getHandle(player);
            Object connection = Reflection.getField(handle.getClass(), "playerConnection").get(handle);
            Object serialized = Reflection.getMethod(nmsChatSerializer, "a", String.class).invoke(null, s);
            Object packet = nmsPacketPlayOutChat.getConstructor(Reflection.getNMSClass("IChatBaseComponent")).newInstance(serialized);
            Reflection.getMethod(connection.getClass(), "sendPacket").invoke(connection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(final Iterable<Player> players) {
        String s = this.toJSONString();

        for (final Player player : players) {
            send(player, s);
        }
    }

    public void send(final List<Player> players) {
        String s = this.toJSONString();

        for (final Player player : players) {
            send(player, s);
        }
    }

    private MessagePart latest() {
        return messageParts.get(messageParts.size() - 1);
    }

    private String makeMultilineTooltip(final String[] lines) {
        StringWriter string = new StringWriter();
        JsonWriter json = new JsonWriter(string);
        try {
            json.beginObject().name("id").value(1);
            json.name("tag").beginObject().name("displayItem").beginObject();
            json.name("Name").value("\\u00A7f" + lines[0].replace("\"", "\\\""));
            json.name("Lore").beginArray();
            for (int i = 1; i < lines.length; i++) {
                final String line = lines[i];
                json.value(line.isEmpty() ? " " : line.replace("\"", "\\\""));
            }
            json.endArray().endObject().endObject().endObject();
            json.close();
        } catch (Exception e) {
            throw new RuntimeException("invalid tooltip");
        }
        return string.toString();
    }

    private void onClick(final String name, final String data) {
        final MessagePart latest = latest();
        latest.clickActionName = name;
        latest.clickActionData = data;
        dirty = true;
    }

    private void onHover(final String name, final String data) {
        final MessagePart latest = latest();
        latest.hoverActionName = name;
        latest.hoverActionData = data;
        dirty = true;
    }
}
