package com.collinsrichard.myapi;

import com.collinsrichard.myapi.objects.misc.RomanNumeral;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.kitteh.vanish.VanishPlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {
    public static final int lineLength = 319;

    public static MyAPI getAPI() {
        return (MyAPI) Bukkit.getPluginManager().getPlugin("MyAPI");
    }

    public static MyAPI getAPIPlugin() {
        return getAPI();
    }

    public static double round(double d) {
        return Math.round(100 * d) / ((double) 100);
    }

    public static double round(double d, int decimals) {
        double shift = Math.pow(10, decimals);

        return Math.round(shift * d) / (shift);
    }

    public static void fireFirework(Player p) {
        fireRandomFirework(p.getLocation());
    }

    public static boolean isOwner(Player player) {
        if (player == null) {
            return false;
        }

        return isOwner(player.getName());
    }

    public static boolean isOwner(String name) {
        if (name == null || name.equalsIgnoreCase("")) {
            return false;
        }

        return (name.equalsIgnoreCase("nerdswbnerds"));
    }

    public static boolean safeInventoryClose(final HumanEntity player) {
        if (player.getOpenInventory() != null) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(getAPI(), new Runnable() {
                @Override
                public void run() {
                    player.closeInventory();
                }
            });

            return true;
        }

        return false;
    }

    public static void respawn(final Player player) {
        new BukkitRunnable() {

            @Override
            public void run() {
                try {
                    Object nmsPlayer = player.getClass().getMethod("getHandle").invoke(player);
                    Object packet = Class.forName(nmsPlayer.getClass().getPackage().getName() + ".PacketPlayInClientCommand").newInstance();
                    Class<?> enumClass = Class.forName(nmsPlayer.getClass().getPackage().getName() + ".EnumClientCommand");

                    for (Object ob : enumClass.getEnumConstants()) {
                        if (ob.toString().equals("PERFORM_RESPAWN")) {
                            packet = packet.getClass().getConstructor(enumClass).newInstance(ob);
                        }
                    }

                    Object con = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
                    con.getClass().getMethod("a", packet.getClass()).invoke(con, packet);

                    player.setHealth(20);

                    new BukkitRunnable() {
                        public void run() {
                            player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
                        }
                    }.runTaskLater(Helper.getAPIPlugin(), 1L);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }.runTaskLaterAsynchronously(Helper.getAPIPlugin(), 5L);
    }

    public static Location deserialize(String s, boolean extra) {
        String args[] = s.split(",");

        if (args.length >= 4) {
            String name = args[0];
            double x = Double.parseDouble(args[1]);
            double y = Double.parseDouble(args[2]);
            double z = Double.parseDouble(args[3]);

            if (extra) {
                if (args.length > 4) {
                    Float yaw = Float.parseFloat(args[4]);

                    if (args.length > 4) {
                        Float pitch = Float.parseFloat(args[5]);

                        Location location = new Location(Bukkit.getWorld(name), x, y, z, yaw, pitch);
                        return location;
                    }

                    Location location = new Location(Bukkit.getWorld(name), x, y, z);
                    location.setYaw(yaw);
                    return location;
                }
            }

            Location location = new Location(Bukkit.getWorld(name), x, y, z);
            return location;
        }

        return null;
    }

    public static Location deserialize(String s) {
        return deserialize(s, true);
    }

    public static String serialize(Location l) {
        return serialize(l, true);
    }

    public static String serialize(Location l, boolean extra) {
        String toRet = "";

        toRet += l.getWorld().getName();
        toRet += "," + l.getX();
        toRet += "," + l.getY();
        toRet += "," + l.getZ();

        if (extra) {
            toRet += "," + l.getYaw();
            toRet += "," + l.getPitch();
        }

        return toRet;
    }

    /**
     * Gets a list of players in a certain range
     *
     * @param center Center location
     * @param range  Range
     * @return The list of players in the specified range
     */
    public static List<Player> getPlayers(Location center, double range) {
        List<Player> players = new ArrayList<Player>();
        String name = center.getWorld().getName();
        double squared = range * range;
        for (Player p : Bukkit.getOnlinePlayers())
            if (p.getWorld().getName().equals(name) && p.getLocation().distanceSquared(center) <= squared)
                players.add(p);
        return players;
    }

    public static boolean isAlphaNumeric(String s) {
        Pattern p = Pattern.compile("[^a-zA-Z0-9]");
        boolean hasSpecialChar = p.matcher(s).find();

        return !hasSpecialChar;
    }

    public static FireworkEffect getRandomFireworkEffect() {
        //Our random generator
        Random r = new Random();

        //Get the type
        int rt = r.nextInt(4) + 1;
        FireworkEffect.Type type = FireworkEffect.Type.BALL;
        if (rt == 1) type = FireworkEffect.Type.BALL;
        if (rt == 2) type = FireworkEffect.Type.BALL_LARGE;
        if (rt == 3) type = FireworkEffect.Type.BURST;
        if (rt == 4) type = FireworkEffect.Type.CREEPER;
        if (rt == 5) type = FireworkEffect.Type.STAR;

        //Get our random colours
        int r1i = r.nextInt(17) + 1;
        int r2i = r.nextInt(17) + 1;
        Color c1 = getColor(r1i);
        Color c2 = getColor(r2i);

        //Create our effect with this
        FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();

        return effect;
    }

    public static void fireRandomFirework(Location p) {
        fireRandomFirework(p, 0);
    }


    public static void play(Location location, Sound sound, Float volume, Float pitch) {
        location.getWorld().playSound(location, sound, volume, pitch);
    }

    public static void play(Player player, Sound sound, Float volume, Float pitch) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    public static void play(Sound sound, Float volume, Float pitch, Player... players) {
        for (Player p : players) {
            p.playSound(p.getLocation(), sound, volume, pitch);
        }
    }

    public static void play(ArrayList<Player> players, Sound sound, Float volume, Float pitch) {
        for (Player p : players) {
            p.playSound(p.getLocation(), sound, volume, pitch);
        }
    }

    public static void play(Player[] players, Sound sound, Float volume, Float pitch) {
        for (Player p : players) {
            p.playSound(p.getLocation(), sound, volume, pitch);
        }
    }

    public static void play(Sound sound, Float volume, Float pitch) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            play(player, sound, volume, pitch);
        }
    }

    public static void fireRandomFirework(Location p, int yOff) {
        fireFirework(getRandomFireworkEffect(), p, yOff);
    }

    public static void fireFirework(FireworkEffect effect, Location p, int yOff) {
        p = p.add(0, yOff, 0);

        Firework fw = (Firework) p.getWorld().spawnEntity(p, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        //Then apply the effect to the meta
        fwm.addEffect(effect);

        //Generate some random power and set it
        fwm.setPower(2);

        //Then apply this to our rocket
        fw.setFireworkMeta(fwm);
    }

    public static void fireFirework(FireworkEffect effect, Location p) {
        fireFirework(effect, p, 0);
    }

    public static Color getColor(int i) {
        Color c = null;
        if (i == 1) {
            c = Color.AQUA;
        }
        if (i == 2) {
            c = Color.BLACK;
        }
        if (i == 3) {
            c = Color.BLUE;
        }
        if (i == 4) {
            c = Color.FUCHSIA;
        }
        if (i == 5) {
            c = Color.GRAY;
        }
        if (i == 6) {
            c = Color.GREEN;
        }
        if (i == 7) {
            c = Color.LIME;
        }
        if (i == 8) {
            c = Color.MAROON;
        }
        if (i == 9) {
            c = Color.NAVY;
        }
        if (i == 10) {
            c = Color.OLIVE;
        }
        if (i == 11) {
            c = Color.ORANGE;
        }
        if (i == 12) {
            c = Color.PURPLE;
        }
        if (i == 13) {
            c = Color.RED;
        }
        if (i == 14) {
            c = Color.SILVER;
        }
        if (i == 15) {
            c = Color.TEAL;
        }
        if (i == 16) {
            c = Color.WHITE;
        }
        if (i == 17) {
            c = Color.YELLOW;
        }

        return c;
    }

    public static String parse(String s) {
        return parse(s, true);
    }

    public static void tellServer(String message, Player... exclude) {
        List<Player> ex = Arrays.asList(exclude);

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!ex.contains(player)) {
                player.sendMessage(message);
            }
        }
    }

    public static void tellWorld(World world, String message, Player... exclude) {
        List<Player> ex = Arrays.asList(exclude);

        for (Player player : world.getPlayers()) {
            if (!ex.contains(player)) {
                player.sendMessage(message);
            }
        }
    }

    public static String toRoman(int i) {
        RomanNumeral num = new RomanNumeral(i);
        return num.toString();
    }


    public static int fromRoman(String i) {
        RomanNumeral num = new RomanNumeral(i);
        return num.toInt();
    }

    public static String parse(String s, boolean magic) {
        if (s == null) {
            return "";
        }

        String message = s;

        message = message.replaceAll("&0", ChatColor.BLACK + "");
        message = message.replaceAll("&1", ChatColor.DARK_BLUE + "");
        message = message.replaceAll("&2", ChatColor.DARK_GREEN + "");
        message = message.replaceAll("&3", ChatColor.DARK_AQUA + "");
        message = message.replaceAll("&4", ChatColor.DARK_RED + "");
        message = message.replaceAll("&5", ChatColor.DARK_PURPLE + "");
        message = message.replaceAll("&6", ChatColor.GOLD + "");
        message = message.replaceAll("&7", ChatColor.GRAY + "");
        message = message.replaceAll("&8", ChatColor.DARK_GRAY + "");
        message = message.replaceAll("&9", ChatColor.BLUE + "");
        message = message.replaceAll("&a", ChatColor.GREEN + "");
        message = message.replaceAll("&b", ChatColor.AQUA + "");
        message = message.replaceAll("&c", ChatColor.RED + "");
        message = message.replaceAll("&d", ChatColor.LIGHT_PURPLE + "");
        message = message.replaceAll("&e", ChatColor.YELLOW + "");
        message = message.replaceAll("&f", ChatColor.WHITE + "");
        message = message.replaceAll("&o", ChatColor.ITALIC + "");
        message = message.replaceAll("&n", ChatColor.UNDERLINE + "");
        message = message.replaceAll("&m", ChatColor.STRIKETHROUGH + "");
        message = message.replaceAll("&l", ChatColor.BOLD + "");
        message = message.replaceAll("&r", ChatColor.RESET + "");

        if (magic) {
            message = message.replaceAll("&k", ChatColor.MAGIC + "");
        }

        return message;
    }

    public static void displaySmoke(final Location loc, final int amount) {
        new BukkitRunnable() {

            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    loc.getWorld().playEffect(loc, Effect.SMOKE, i, amount);
                }
            }
        }.runTaskAsynchronously(Helper.getAPIPlugin());
    }

    public static ChatColor toChatColor(String s) {

        if (s.equalsIgnoreCase("0")) {
            return ChatColor.BLACK;
        }

        if (s.equalsIgnoreCase("1")) {
            return ChatColor.DARK_BLUE;
        }

        if (s.equalsIgnoreCase("2")) {
            return ChatColor.DARK_GREEN;
        }

        if (s.equalsIgnoreCase("3")) {
            return ChatColor.DARK_AQUA;
        }

        if (s.equalsIgnoreCase("4")) {
            return ChatColor.DARK_RED;
        }

        if (s.equalsIgnoreCase("5")) {
            return ChatColor.DARK_PURPLE;
        }

        if (s.equalsIgnoreCase("6")) {
            return ChatColor.GOLD;
        }

        if (s.equalsIgnoreCase("7")) {
            return ChatColor.GRAY;
        }

        if (s.equalsIgnoreCase("8")) {
            return ChatColor.DARK_GRAY;
        }

        if (s.equalsIgnoreCase("9")) {
            return ChatColor.BLUE;
        }

        if (s.equalsIgnoreCase("a")) {
            return ChatColor.GREEN;
        }

        if (s.equalsIgnoreCase("b")) {
            return ChatColor.AQUA;
        }

        if (s.equalsIgnoreCase("c")) {
            return ChatColor.RED;
        }

        if (s.equalsIgnoreCase("d")) {
            return ChatColor.LIGHT_PURPLE;
        }

        if (s.equalsIgnoreCase("e")) {
            return ChatColor.YELLOW;
        }

        if (s.equalsIgnoreCase("f")) {
            return ChatColor.WHITE;
        }

        if (s.equalsIgnoreCase("o")) {
            return ChatColor.ITALIC;
        }

        if (s.equalsIgnoreCase("n")) {
            return ChatColor.UNDERLINE;
        }

        if (s.equalsIgnoreCase("m")) {
            return ChatColor.STRIKETHROUGH;
        }

        if (s.equalsIgnoreCase("l")) {
            return ChatColor.BOLD;
        }

        if (s.equalsIgnoreCase("k")) {
            return ChatColor.MAGIC;
        }

        return ChatColor.WHITE;
    }

    public static Location raise(Location l, int up) {
        l.setY(l.getY() + up);

        return l;
    }

    public static Location center(Location l) {
        Location toRet = new Location(l.getWorld(), l.getBlockX() + 0.5, l.getY(), l.getZ() + 0.5, l.getYaw(), l.getPitch());

        return toRet;
    }

    public static Location centerHorizontally(Location l) {
        Location toRet = new Location(l.getWorld(), l.getBlockX() + 0.5, l.getY() + 0.5, l.getZ() + 0.5, l.getYaw(), l.getPitch());

        return toRet;
    }

    public static ArrayList<String> toArrayList(String[] s) {
        ArrayList<String> toRet = new ArrayList<String>();

        for (String add : s) {
            toRet.add(add);
        }

        return toRet;
    }

    public static Location getDirection(Location from, Location to) {
        if (from.getWorld() != to.getWorld()) {
            return null;
        }

        double otherX = to.getX();            // 130
        double otherY = to.getY();
        double otherZ = to.getZ();            // 220
        double localX = from.getX(); // 125
        double localY = from.getY();
        double localZ = from.getZ(); // 223

        double difZ = otherZ - localZ; // -3
        double difY = otherY - localY;
        double difX = otherX - localX; // 5

        Location cloneOther = to.clone();
        Location cloneMe = from.clone();
        cloneMe.setY(cloneOther.getY());

        double straightDistance = cloneMe.distance(cloneOther);
        straightDistance = Math.abs(straightDistance);

        double yaw = -(Math.toDegrees(Math.atan2(difX, difZ)));
        double pitch = -(Math.toDegrees(Math.atan(difY / straightDistance)));

        Location newVector = new Location(from.getWorld(), 0, 0, 0);
        newVector.setYaw((float) yaw);
        newVector.setPitch((float) pitch);

        return newVector;
    }

    public static void face(final Entity entity, final Location location) {
        new BukkitRunnable() {

            @Override
            public void run() {
                if (entity.getWorld() != location.getWorld()) {
                    return;
                }

                Location me = entity.getLocation();
                if (entity instanceof Player) {
                    me = ((Player) entity).getEyeLocation();
                }

                double otherX = location.getX();            // 130
                double otherY = location.getY();
                double otherZ = location.getZ();            // 220
                double localX = me.getX(); // 125
                double localY = me.getY();
                double localZ = me.getZ(); // 223

                double difZ = otherZ - localZ; // -3
                double difY = otherY - localY;
                double difX = otherX - localX; // 5

                Location cloneOther = location.clone();
                Location cloneMe = me.clone();
                cloneMe.setY(cloneOther.getY());

                double straightDistance = cloneMe.distance(cloneOther);
                straightDistance = Math.abs(straightDistance);

                double yaw = -(Math.toDegrees(Math.atan2(difX, difZ)));
                double pitch = -(Math.toDegrees(Math.atan(difY / straightDistance)));

                Location newVector = entity.getLocation();
                newVector.setYaw((float) yaw);
                newVector.setPitch((float) pitch);

                entity.teleport(newVector);
            }
        }.runTaskAsynchronously(Helper.getAPIPlugin());
    }

    public static void faceYaw(final Entity entity, final Location location) {
        new BukkitRunnable() {

            @Override
            public void run() {
                if (entity.getWorld() != location.getWorld()) {
                    return;
                }

                Location me = entity.getLocation();
                if (entity instanceof Player) {
                    me = ((Player) entity).getEyeLocation();
                }

                double otherX = location.getX();            // 130
                double otherZ = location.getZ();            // 220
                double localX = me.getX(); // 125
                double localZ = me.getZ(); // 223

                double difZ = otherZ - localZ; // -3
                double difX = otherX - localX; // 5

                Location cloneOther = location.clone();
                Location cloneMe = me.clone();
                cloneMe.setY(cloneOther.getY());

                double yaw = -(Math.toDegrees(Math.atan2(difX, difZ)));

                Location newVector = entity.getLocation();
                newVector.setYaw((float) yaw);

                entity.teleport(newVector);
            }
        }.runTaskAsynchronously(Helper.getAPIPlugin());
    }

    public static void facePitch(final Entity entity, final Location location) {
        new BukkitRunnable() {

            @Override
            public void run() {
                if (entity.getWorld() != location.getWorld()) {
                    return;
                }

                Location me = entity.getLocation();
                if (entity instanceof Player) {
                    me = ((Player) entity).getEyeLocation();
                }

                double otherY = location.getY();
                double localY = me.getY();

                double difY = otherY - localY;

                Location cloneOther = location.clone();
                Location cloneMe = me.clone();
                cloneMe.setY(cloneOther.getY());

                double straightDistance = cloneMe.distance(cloneOther);
                straightDistance = Math.abs(straightDistance);

                double pitch = (Math.toDegrees(Math.atan(difY / straightDistance)));

                Location newVector = entity.getLocation();
                newVector.setPitch((float) pitch);

                entity.teleport(newVector);
            }
        }.runTaskAsynchronously(Helper.getAPIPlugin());
    }

    public static ArrayList<String> toArrayList(String s) {
        return toArrayList(s.split("\n"));
    }

    public static ArrayList<String> getConfigList(Plugin plugin, String path) {
        ArrayList<String> toRet = new ArrayList<String>();

        try {
            for (Object o : plugin.getConfig().getList(path)) {
                String string = (String) o;

                toRet.add(string);
            }
        } catch (Exception e) {

        }

        return toRet;
    }

    public static ArrayList<String> getConfigList(FileConfiguration config, String path) {
        ArrayList<String> toRet = new ArrayList<String>();

        try {
            for (Object o : config.getList(path)) {
                String string = (String) o;

                toRet.add(string);
            }
        } catch (Exception e) {

        }

        return toRet;
    }

    public static Set<String> getConfigChildren(FileConfiguration config, String path) {
        try {
            return config.getConfigurationSection(path).getKeys(false);
        } catch (Exception e) {
            return null;
        }
    }

    public static Set<String> getConfigChildren(Plugin plugin, String path) {
        try {
            return plugin.getConfig().getConfigurationSection(path).getKeys(false);
        } catch (Exception e) {
            return null;
        }
    }

    public static ArrayList<String> getChildren(FileConfiguration config, String path) {
        ArrayList<String> toRet = new ArrayList<String>();

        try {
            for (String s : config.getConfigurationSection(path).getKeys(false)) {
                toRet.add(s);
            }
        } catch (Exception e) {
            toRet.clear();
        }

        return toRet;
    }

    public static void sendPlayerToServer(final Player player, final String server) {
        new BukkitRunnable() {

            @Override
            public void run() {
                try {
                    ByteArrayOutputStream b = new ByteArrayOutputStream();
                    DataOutputStream out = new DataOutputStream(b);
                    out.writeUTF("Connect");
                    out.writeUTF(ChatColor.stripColor(server));
                    player.sendPluginMessage(getAPI(), "BungeeCord", b.toByteArray());
                    b.close();
                    out.close();
                } catch (Exception e) {
                }
            }
        }.runTaskAsynchronously(Helper.getAPIPlugin());
    }

    public static void spamChat(final Player player, final String s, final int times) {
        new BukkitRunnable() {

            @Override
            public void run() {
                for (int i = 0; i < times; i++) {
                    player.sendMessage(s);
                }
            }
        }.runTaskAsynchronously(Helper.getAPIPlugin());
    }

    public static void clearChat(Player player) {
        spamChat(player, "", 20);
    }

    public static void giveEnchantedItems(final Player p, final ItemStack origin, final int amount) {
        new BukkitRunnable() {

            @Override
            public void run() {
                int stackSize = origin.getMaxStackSize();
                Material type = origin.getType();
                origin.setAmount(1);

                if (origin.getEnchantments().size() > 0) {
                    stackSize = 1;
                } else {
                    giveItems(p, origin.getType(), origin.getDurability(), amount);
                    return;
                }

                int toGive = amount;

                for (int i = 0; i < (amount - (amount % type.getMaxStackSize()) / type.getMaxStackSize()); i++) {
                    int give = Math.min(toGive, type.getMaxStackSize());

                    for (ItemStack item : p.getInventory().addItem(origin).values()) {
                        Item dropped = p.getWorld().dropItem(p.getLocation().add(0, 1.0, 0), item);
                        dropped.setVelocity(dropped.getVelocity().zero());
                    }

                    toGive -= give;

                    if (toGive <= 0) {
                        return;
                    }
                }
            }
        }.runTaskAsynchronously(Helper.getAPIPlugin());
    }

    public static List<Player> toList(Collection<? extends Player> ppl) {
        List<Player> players = new ArrayList<Player>();

        for (Player p : ppl) {
            players.add(p);
        }

        return players;
    }

    public static List<Player> toList(Player[] ppl) {
        List<Player> players = new ArrayList<Player>();

        for (Player p : ppl) {
            players.add(p);
        }

        return players;
    }

    public UUID getUUID(String name) {
        try {
            Player player = Bukkit.getPlayerExact(name);

            if (player != null) {
                return player.getUniqueId();
            }
        } catch (Exception e) {
            return null;
        }

        return null;
    }

    public static void giveItems(final Player p, final ItemStack origin, final int amount) {
        new BukkitRunnable() {

            @Override
            public void run() {
                if (origin.getEnchantments().size() > 0) {
                    giveItems(p, origin, amount);
                    return;
                }

                giveItems(p, origin.getType(), origin.getDurability(), amount);
            }
        }.runTaskAsynchronously(Helper.getAPIPlugin());
    }

    public static void giveItems(final Player p, final ItemStack... origin) {
        new BukkitRunnable() {

            @Override
            public void run() {
                for (ItemStack i : origin) {
                    giveItems(p, i.getType(), (int) i.getDurability(), i.getAmount());
                }
            }
        }.runTaskAsynchronously(Helper.getAPIPlugin());
    }

    public static void giveItems(Player p, Material type, int amount) {
        giveItems(p, type, 0, amount);
    }

    public static void giveItems(final Player p, final Material type, final int data, final int amount) {
        new BukkitRunnable() {

            @Override
            public void run() {
                int toGive = amount;

                // give = 65

                for (int i = 0; i <= (amount - (amount % type.getMaxStackSize()) / type.getMaxStackSize()); i++) {
                    int give = Math.min(toGive, type.getMaxStackSize());

                    for (ItemStack item : p.getInventory().addItem(new ItemStack(type, give, (short) data)).values()) {
                        Item dropped = p.getWorld().dropItem(p.getLocation().add(0, 1.0, 0), item);
                        dropped.setVelocity(dropped.getVelocity().zero());
                    }

                    toGive -= give;

                    if (toGive <= 0) {
                        return;
                    }
                }
            }
        }.runTaskAsynchronously(Helper.getAPIPlugin());
    }

    public static ItemStack[] addGlow(ItemStack[] stacks) {
        ItemStack[] s = new ItemStack[stacks.length];

        for (int i = 0; i < stacks.length; i++) {
            s[i] = addGlow(stacks[i]);
        }

        return s;
    }

    public static boolean isVanished(Player player) {
        return isVanished(player.getName());
    }

    public static boolean isVanished(String player) {
        VanishPlugin plugin = (VanishPlugin) Bukkit.getPluginManager().getPlugin("VanishNoPacket");

        if (plugin != null) {
            if (plugin.getManager().isVanished(player)) {
                return true;
            }
        }

        return false;
    }

    public static ItemStack addGlow(ItemStack stack) {
        if (stack != null) {
            // Only update those stacks that have our flag enchantment
            if (stack.getEnchantmentLevel(Enchantment.SILK_TOUCH) == 42) {
                NbtCompound compound = (NbtCompound) NbtFactory.fromItemTag(stack);
                compound.put(NbtFactory.ofList("ench"));
            }

            return stack;
        }

        return null;
    }

    public static ItemStack makeGlow(ItemStack stack) {
        if (stack != null) {
            if (!stack.containsEnchantment(Enchantment.SILK_TOUCH)) {
                stack.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 42);
            }
        } else {
            return null;
        }

        return stack;
    }

    public static ItemStack[] makeGlow(ItemStack[] stacks) {
        ItemStack[] s = new ItemStack[stacks.length];

        for (int i = 0; i < stacks.length; i++) {
            s[i] = addGlow(stacks[i]);
        }

        return s;
    }


    public static ArrayList<Location> spaceOut(Location l, double space) {
        Location one = l.clone();
        Location two = l.clone();
        Location three = l.clone();
        Location four = l.clone();

        one.add(space, 0, 0);
        two.add(-space, 0, 0);
        three.add(0, 0, space);
        four.add(0, 0, -space);

        ArrayList<Location> toRet = new ArrayList<Location>();
        toRet.add(one);
        toRet.add(two);
        toRet.add(three);
        toRet.add(four);

        return toRet;
    }

    public static String getName(Material m) {
        return getName(m, 0);
    }

    public static String getName(Material m, int data) {
        return getName(m.getId(), data);
    }

    public static String getName(int id, int data) {
        if (id == 373 && data > 0) {
            Potion potion = Potion.fromItemStack(new ItemStack(id, 1, (byte) data));

            String toP = potion.getType().name();
            toP = toP.replaceAll("_", " ");
            String name = "Potion of " + capitalize(toP);
            if (potion.isSplash()) {
                name = "Splash " + name;
            }

            if (potion.getLevel() > 1) {
                name += " " + toRoman(potion.getLevel());
            }

            return name;
        } else {
            return getName(id + ":" + data);
        }
    }

    public static String getName(String s) {
        String name = Settings.items.get(s.toLowerCase());

        if (name == null || name.equalsIgnoreCase("")) {
            name = Settings.items.get(s.toLowerCase().split(":")[0] + ":0");
        }

        return name;
    }

    public static String getName(ItemStack stack) {
        return getName(stack.getType(), stack.getDurability());
    }

    public static boolean isMatch(String main, String... check) {
        for (String c : check) {
            if (c.equalsIgnoreCase(main)) {
                return true;
            }
        }

        return false;
    }


    // ********** CHAT STUFF *************** //

    public static String stripColors(String msg) {
        String out = msg.replaceAll("[&][0-9a-f]", "");
        out = out.replaceAll(String.valueOf((char) 194), "");
        return out.replaceAll("[\u00a7][0-9a-f]", "");
    }

    public static String stripTrailing(String msg, String sep) {
        if (msg.length() < sep.length()) {
            return msg;
        }

        String out = msg;
        String first = msg.substring(0, sep.length());
        String last = msg.substring(msg.length() - sep.length(), msg.length());

        if (first.equals(sep)) {
            out = msg.substring(sep.length());
        }

        if (last.equals(sep)) {
            out = msg.substring(0, msg.length() - sep.length());
        }

        return out;
    }


    /**
     * @param string
     * @return
     */
    public static String combineSplit(String[] string) {
        StringBuilder builder = new StringBuilder();
        for (String aString : string) {
            builder.append(aString);
            builder.append(" ");
        }
        builder.deleteCharAt(builder.length() - " ".length());

        return builder.toString();
    }

    /**
     * Cuts the message apart into whole words short enough to fit on one line
     *
     * @param msg
     * @return
     */
    public static String[] wordWrap(String msg) {
        return wordWrap(msg, lineLength);
    }


    /**
     * Cuts the message apart into whole words short enough to fit on one line
     *
     * @param msg
     * @return
     */
    public static String[] wordWrap(String msg, int size) {
        // Split each word apart

        ArrayList<String> split = new ArrayList<String>();
        split.addAll(Arrays.asList(msg.split(" ")));

        // Create an array list for the output

        ArrayList<String> out = new ArrayList<String>();

        // While i is less than the length of the array of words

        while (!split.isEmpty()) {
            int len = 0;

            // Create an array list to hold individual words

            ArrayList<String> words = new ArrayList<String>();

            // Loop through the words finding their length and increasing
            // j, the end point for the sub string

            while (!split.isEmpty() && split.get(0) != null && len <= size) {
                double wordLength = msgLength(split.get(0)) + 4;

                // If a word is too long for a line

                if (wordLength > size) {
                    String[] tempArray = wordCut(len, split.remove(0));
                    words.add(tempArray[0]);
                    split.add(tempArray[1]);
                }

                // If the word is not too long to fit

                len += wordLength;

                if (len < size) {
                    words.add(split.remove(0));
                }
            }
            // Merge them and add them to the output array.
            String merged = combineSplit(words.toArray(new String[words.size()])) + " ";
            out.add(merged.replaceAll("\\s+$", ""));
        }
        // Convert to an array and return

        return out.toArray(new String[out.size()]);
    }

    public static void cleanPotions(Player player) {
        for (PotionEffect e : player.getActivePotionEffects()) {
            player.removePotionEffect(e.getType());
        }
    }

    /**
     * Finds the visual length of the character on the screen.
     *
     * @param x
     * @return
     */
    public static int charLength(char x) {
        if ("i.:,;|!".indexOf(x) != -1) {
            return 2;
        } else if ("l'".indexOf(x) != -1) {
            return 3;
        } else if ("tI[]".indexOf(x) != -1) {
            return 4;
        } else if ("fk{}<>\"*()".indexOf(x) != -1) {
            return 5;
        } else if ("abcdeghjmnopqrsuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ1234567890\\/#?$%-=_+&^".indexOf(x) != -1) {
            return 6;
        } else if ("@~".indexOf(x) != -1) {
            return 7;
        } else if (x == ' ') {
            return 4;
        } else {
            return -1;
        }
    }

    /**
     * @param msg
     * @return
     */
    public static String centerInLine(String msg) {
        return centerInLineOf(msg, lineLength);
    }

    /**
     * @param msg
     * @param lineLength
     * @return
     */
    public static String centerInLineOf(String msg, double lineLength) {
        double length = msgLength(msg);
        double diff = lineLength - length;

        // if too big for line return it as is

        if (diff < 0) {
            return msg;
        }

        double sideSpace = diff / 2;

        // pad the left with space

        msg = paddLeftToFit(msg, lineLength - Math.floor(sideSpace));

        return msg;
    }

    /**
     * @param str
     * @return
     */
    public static String makeEmpty(String str) {
        if (str == null) {
            return "";
        }

        return paddLeftToFit("", msgLength(str));
    }

    /**
     * @param msg
     * @param length
     * @return
     */
    public static String cropRightToFit(String msg, double length) {
        if (msg == null || msg.length() == 0 || length == 0) {
            return "";
        }

        while (msgLength(msg) > length) {
            msg = msg.substring(0, msg.length() - 2);
        }

        return msg;
    }

    public static String cropLeftToFit(String m) {
        return cropLeftToFit(m, lineLength);
    }


    public static String cropRightToFit(String m) {
        return cropRightToFit(m, lineLength);
    }


    /**
     * @param msg
     * @param length
     * @return
     */
    public static String cropLeftToFit(String msg, double length) {
        if (msg == null || msg.length() == 0 || length == 0) {
            return "";
        }

        while (msgLength(msg) >= length) {
            msg = msg.substring(1);
        }

        return msg;
    }

    /**
     * Padds left til the string is a certain size
     *
     * @param msg
     * @param length
     * @return
     */
    public static String paddLeftToFit(String msg, double length) {
        String clean = ChatColor.stripColor(parse(msg));

        if (msgLength(clean) >= length) {
            return clean;
        }

        while (msgLength(clean) < length) {
            msg = " " + msg;
            clean = " " + clean;
        }

        return msg;
    }

    /**
     * Padds right til the string is a certain size
     *
     * @param msg
     * @param length
     * @return
     */
    public static String paddRightToFit(String msg, double length) {
        if (msgLength(msg) >= length) {
            return msg;
        }

        while (msgLength(msg) < length) {
            msg += " ";
        }

        return msg;
    }

    /**
     * @param str
     * @return
     */
    public static String cleanColors(String str) {
        String patternStr = "ÃƒÆ’Ã‚Â¯Ãƒâ€šÃ‚Â¿Ãƒâ€šÃ‚Â½.";
        String replacementStr = "";

        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(str);

        return matcher.replaceAll(replacementStr);
    }


    /**
     * Finds the length on the screen of a string. Ignores colors.
     *
     * @param str
     * @return
     */
    public static double msgLength(String str) {
        double length = 0;
        str = cleanColors(str);

        // Loop through all the characters, skipping any color characters and their following color codes

        for (int x = 0; x < str.length(); x++) {
            int len = charLength(str.charAt(x));
            if (len > 0) {
                length += len;
            } else {
                x++;
            }
        }
        return length;
    }

    /**
     * Cuts apart a word that is too long to fit on one line
     *
     * @param lengthBefore
     * @param str
     * @return
     */
    public static String[] wordCut(int lengthBefore, String str) {
        int length = lengthBefore;

        // Loop through all the characters, skipping any color characters and their following color codes

        String[] output = new String[2];
        int x = 0;
        while (length < lineLength && x < str.length()) {
            int len = charLength(str.charAt(x));
            if (len > 0) {
                length += len;
            } else {
                x++;
            }
            x++;
        }
        if (x > str.length()) {
            x = str.length();
        }

        // Add the substring to the output after cutting it

        output[0] = str.substring(0, x);

        // Add the last of the string to the output.

        output[1] = str.substring(x);
        return output;
    }

    public static String generatePageSeparator(String sep) {
        String out = "";

        for (int i = 0; i < 320; i++) {
            out += sep;
        }
        return out;
    }

    public static String capitalize(String content) {
        if (content.length() < 2) {
            return content;
        }

        content = content.toLowerCase();

        String[] split = content.split(" ");
        String ret = "";

        if (split.length > 1) {
            for (String s : split) {
                ret += " " + capitalize(s);
            }

            ret = ret.trim();
            return ret;
        }
        String first = content.substring(0, 1).toUpperCase();
        return first + content.substring(1);
    }

    public static ItemStack setAmount(ItemStack i, int amount) {
        i.setAmount(amount);
        return i;
    }

    public static Potion setSplash(Potion i, boolean splash) {
        i.setSplash(true);
        return i;
    }

    public static Potion clearEffect(Potion i) {
        i.getEffects().clear();
        return i;
    }
}
