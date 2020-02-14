package com.collinsrichard.myapi.objects.particles;

import com.collinsrichard.myapi.Helper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public enum ParticleType {
    /**
     * @appearance Dark gray cracked hearts
     * @displayed when attacking a villager in a village
     */
    ANGRY_VILLAGER("Angry Villager Heart (Broken Heart)", "angryVillager"),
    /**
     * @appearance Transparent colored swirls
     * @displayed by beacon effect
     */
    MOB_SPELL_AMBIENT("Beacon Effect", "mobSpellAmbient"),
    /**
     * @appearance Little white parts
     * @displayed by cracking snowballs and eggs
     */
    SNOWBALL_POOF("Breaking Snowball", "snowballpoof"),
    /**
     * @appearance Bubbles
     * @displayed in water
     */
    BUBBLE("Bubble", "bubble"),
    /**
     * @appearance Large white clouds
     * @displayed on mob death
     */
    CLOUD("Cloud", "cloud"),
    /**
     * @appearance Light brown crosses
     * @displayed by critical hits
     */
    CRIT("Critical Hits", "crit"),
    /**
     * @appearance Orange drips
     * @displayed by blocks below a lava source
     */
    DRIP_LAVA("Dripping Lava", "dripLava"),
    /**
     * @appearance Blue drips
     * @displayed by blocks below a water source
     */
    DRIP_WATER("Dripping Water", "dripWater"),
    /**
     * @appearance Cyan stars
     * @displayed by hits with an enchanted weapon
     */
    MAGIC_CRIT("Enchanted Critical Hit", "magicCrit"),
    /**
     * @appearance: White letters
     * @displayed by enchantment tables that are near bookshelves
     */
    ENCHANTMENT_TABLE("Enchantment Table Letters", "enchantmenttable"),
    /**
     * @appearance White clouds
     */
    EXPLODE("Explosion", "explode"),
    /**
     * @appearance Little white sparkling stars
     * @displayed by Fireworks
     */
    FIREWORKS_SPARK("Firework Spark", "fireworksSpark"),
    /**
     * @appearance Little flames
     * @displayed by torches, furnaces, magma cubes and monster spawners
     */
    FIRE("Fire", "flame"),
    /**
     * @appearance Blue droplets
     * @displayed on water when fishing
     */
    WAKE("Fishing Splash", "wake"),
    /**
     * @appearance Gray transparent squares
     */
    FOOTSTEP("Footstep", "footstep"),
    /**
     * @appearance Green stars
     * @displayed by bone meal and when trading with a villager
     */
    HAPPY_VILLAGER("Happy Villager Star (Bone Meal)", "happyVillager"),
    /**
     * @appearance Red hearts
     * @displayed when breeding
     */
    HEART("Heart", "heart"),
    /**
     * @appearance Huge explosions
     * @displayed by TNT and creepers
     */
    HUGE_EXPLOSION("Huge Explosion", "hugeexplosion"),
    /**
     * @appearance Colored crosses
     * @displayed by instant splash potions (instant health/instant damage)
     */
    INSTANT_SPELL("Instant Splash Potion", "instantSpell"),
    /**
     * @appearance Smaller explosions
     * @displayed by TNT and creepers
     */
    LARGE_EXPLODE("Large Explosion", "largeexplode"),
    /**
     * @appearance Black/Gray clouds
     * @displayed by fire, minecarts with furance and blazes
     */
    LARGE_SMOKE("Large Smoke (Fire/Blaze)", "largesmoke"),
    /**
     * @appearance Little orange blobs
     * @displayed by lava
     */
    LAVA("Lava", "lava"),
    /**
     * @appearance Little gray dots
     * @displayed by Mycelium
     */
    MYCELIUM("Mycelium", "mycelium"),
    /**
     * @appearance Colored notes
     * @displayed by note blocks
     */
    NOTE("Note", "note"),
    /**
     * @appearance Little purple clouds
     * @displayed by nether portals, endermen, ender pearls, eyes of ender and ender chests
     */
    PORTAL("Portal", "portal"),
    /**
     * @appearance Colored swirls
     * @displayed by potion effects
     */
    MOB_SPELL("Potion Effect", "mobSpell"),
    /**
     * @appearance Little colored clouds
     * @displayed by active redstone wires and redstone torches
     */
    RED_DUST("Redstone Dust", "reddust"),
    /**
     * @appearance Little green parts
     * @displayed by slimes
     */
    SLIME("Slime", "slime"),
    /**
     * @appearance Little black/gray clouds
     * @displayed by torches, primed TNT and end portals
     */
    SMOKE("Smoke (Torches/TNT)", "smoke"),
    /**
     * @appearance White clouds
     */
    SNOW_SHOVEL("Snow Shovel", "snowshovel"),
    /**
     * @appearance Colored swirls
     * @displayed by splash potions
     */
    SPELL("Splash Potion", "Potion"),
    /**
     * @appearance Unknown
     */
    SUSPEND("Suspend (Unknown)", "suspend"),
    /**
     * @appearance Little gray dots
     * @displayed in the Void and water
     */
    DEPTH_SUSPEND("Void", "depthSuspend"),
    /**
     * @appearance Blue drops
     * @displayed by water, rain and shaking wolves
     */
    SPLASH("Water Splash", "splash"),
    /**
     * @appearance Colored crosses
     * @displayed by witches
     */
    WITCH_MAGIC("Witch Magic", "witchMagic");

    private static final Map<String, ParticleType> NAME_MAP = new HashMap<String, ParticleType>();
    //private static final double MAX_RANGE = 16;
    public static final double DEFAULT_RANGE = 64;
    private static Constructor<?> packetPlayOutWorldParticles;
    private static Method getHandle;
    private static Field playerConnection;
    private static Method sendPacket;
    private final String name;
    private final String displayName;
    private ItemStack itemStack = new ItemStack(Material.BOOK);

    static {
        for (ParticleType p : values())
            NAME_MAP.put(p.name, p);
        try {
            packetPlayOutWorldParticles = ReflectionHandler.getConstructor(ReflectionHandler.PacketType.PLAY_OUT_WORLD_PARTICLES.getPacket(), String.class, float.class, float.class, float.class, float.class, float.class,
                    float.class, float.class, int.class);
            getHandle = ReflectionHandler.getMethod("CraftPlayer", ReflectionHandler.SubPackageType.ENTITY, "getHandle");
            playerConnection = ReflectionHandler.getField("EntityPlayer", ReflectionHandler.PackageType.MINECRAFT_SERVER, "playerConnection");
            sendPacket = ReflectionHandler.getMethod(playerConnection.getType(), "sendPacket", ReflectionHandler.getClass("Packet", ReflectionHandler.PackageType.MINECRAFT_SERVER));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param name Name of this particle effect
     */
    private ParticleType(String dName, String name) {
        this.displayName = dName;
        this.name = name;
    }

    private ParticleType(String dName, String name, ItemStack i) {
        this.displayName = dName;
        this.name = name;
        this.itemStack = i;
    }

    private ParticleType(String dName, String name, int id) {
        this.displayName = dName;
        this.name = name;
        itemStack = new ItemStack(id, 1);
    }

    private ParticleType(String dName, String name, Material type) {
        this.displayName = dName;
        this.name = name;
        itemStack = new ItemStack(type, 1);
    }


    private ParticleType(String dName, String name, int id, int data) {
        this.name = name;
        this.displayName = dName;
        itemStack = new ItemStack(id, 1, (byte) data);
    }

    private ParticleType(String dName, String name, Material type, int data) {
        this.name = name;
        this.displayName = dName;
        itemStack = new ItemStack(type, 1, (byte) data);
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * @return The name of this particle effect
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets a particle effect from name
     *
     * @param name Name of the particle effect
     * @return The particle effect
     */
    public static ParticleType fromName(String name) {
        if (name != null)
            for (Map.Entry<String, ParticleType> e : NAME_MAP.entrySet())
                if (e.getKey().equalsIgnoreCase(name))
                    return e.getValue();
        return null;
    }

    public static ParticleType get(int i) {
        return values()[i];
    }

    /**
     * Instantiates a new @PacketPlayOutWorldParticles object through reflection
     *
     * @param center  Center location of the effect
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param speed   Display speed of the particles
     * @param amount  Amount of particles
     * @return The packet object
     * @throws #PacketInstantiationException if the amount is lower than 1 or if the @PacketPlayOutWorldParticles has changed its name or constructor parameters
     */
    private static Object instantiatePacket(String name, Location center, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
        if (amount < 1)
            throw new PacketInstantiationException("Amount cannot be lower than 1");
        try {
            return packetPlayOutWorldParticles.newInstance(name, (float) center.getX(), (float) center.getY(), (float) center.getZ(), offsetX, offsetY, offsetZ, speed, amount);
        } catch (Exception e) {
            throw new PacketInstantiationException("Packet instantiation failed", e);
        }
    }

    /**
     * Instantiates a new @PacketPlayOutWorldParticles object through reflection especially for the "iconcrack" effect
     *
     * @param id      Id of the icon
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param speed   Display speed of the particles
     * @param amount  Amount of particles
     * @return The packet object
     * @throws #PacketInstantiationException if the amount is lower than 1 or if the @PacketPlayOutWorldParticles has changed its name or constructor parameters
     * @see #instantiatePacket
     */
    private static Object instantiateIconCrackPacket(int id, Location center, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
        return instantiatePacket("iconcrack_" + id, center, offsetX, offsetY, offsetZ, speed, amount);
    }

    /**
     * Instantiates a new @PacketPlayOutWorldParticles object through reflection especially for the "blockcrack" effect
     *
     * @param id      Id of the block
     * @param data    Data value
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param amount  Amount of particles
     * @return The packet object
     * @throws #PacketInstantiationException if the amount is lower than 1 or if the @PacketPlayOutWorldParticles has changed its name or constructor parameters
     * @see #instantiatePacket
     */
    private static Object instantiateBlockCrackPacket(int id, byte data, Location center, float offsetX, float offsetY, float offsetZ, int amount) {
        return instantiatePacket("blockcrack_" + id + "_" + data, center, offsetX, offsetY, offsetZ, 0, amount);
    }

    /**
     * Instantiates a new @PacketPlayOutWorldParticles object through reflection especially for the "blockdust" effect
     *
     * @param id      Id of the block
     * @param data    Data value
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param speed   Display speed of the particles
     * @param amount  Amount of particles
     * @return The packet object
     * @throws #PacketInstantiationException if the amount is lower than 1 or if the name or the constructor of @PacketPlayOutWorldParticles have changed
     * @see #instantiatePacket
     */
    private static Object instantiateBlockDustPacket(int id, byte data, Location center, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
        return instantiatePacket("blockdust_" + id + "_" + data, center, offsetX, offsetY, offsetZ, speed, amount);
    }

    /**
     * Sends a packet through reflection to a player
     *
     * @param p      Receiver of the packet
     * @param packet Packet that is sent
     * @throws #PacketSendingException if the packet is null or some methods which are accessed through reflection have changed
     */
    private static void sendPacket(Player p, Object packet) {
        try {
            sendPacket.invoke(playerConnection.get(getHandle.invoke(p)), packet);
        } catch (Exception e) {
            throw new PacketSendingException("Failed to send a packet to player '" + p.getName() + "'", e);
        }
    }

    /**
     * Sends a packet through reflection to a collection of players
     *
     * @param players Receivers of the packet
     * @param packet  Packet that is sent
     * @throws #PacketSendingException if the sending to a single player fails
     * @see #sendPacket
     */
    private static void sendPacket(Collection<Player> players, Object packet) {
        for (Player p : players)
            sendPacket(p, packet);
    }

    /**
     * Displays a particle effect which is only visible for the specified players
     *
     * @param center  Center location of the effect
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param speed   Display speed of the particles
     * @param amount  Amount of particles
     * @param players Receivers of the effect
     * @see #sendPacket
     * @see #instantiatePacket
     */
    public void display(Location center, float offsetX, float offsetY, float offsetZ, float speed, int amount, Player... players) {
        sendPacket(Arrays.asList(players), instantiatePacket(name, center, offsetX, offsetY, offsetZ, speed, amount));
    }

    /**
     * Displays a particle effect which is only visible for all players within a certain range in the world of @param center
     *
     * @param center  Center location of the effect
     * @param range   Range of the visibility
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param speed   Display speed of the particles
     * @param amount  Amount of particles
     * @throws @IllegalArgumentException if the range is higher than 20
     * @see #sendPacket
     * @see #instantiatePacket
     */
    public void display(Location center, double range, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
        sendPacket(Helper.getPlayers(center, range), instantiatePacket(name, center, offsetX, offsetY, offsetZ, speed, amount));
    }

    /**
     * Displays a particle effect which is only visible for all players within a range of 20 in the world of @param center
     *
     * @param center  Center location of the effect
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param speed   Display speed of the particles
     * @param amount  Amount of particles
     * @see #display(Location, double, float, float, float, float, int)
     */
    public void display(Location center, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
        display(center, DEFAULT_RANGE, offsetX, offsetY, offsetZ, speed, amount);
    }

    /**
     * Displays a particle effect which is only visible for all players within a certain range in the world of @param center
     *
     * @param center  Center location of the effect
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param speed   Display speed of the particles
     * @param amount  Amount of particles
     * @param players Receivers of the effect
     * @throws @IllegalArgumentException if the range is higher than 20
     * @see #sendPacket
     * @see #instantiatePacket
     */
    public void display(Location center, float offsetX, float offsetY, float offsetZ, float speed, int amount, ArrayList<Player> players) {
        sendPacket(players, instantiatePacket(name, center, offsetX, offsetY, offsetZ, speed, amount));
    }

    /**
     * Displays an icon crack (item break) particle effect which is only visible for the specified players
     *
     * @param center  Center location of the effect
     * @param id      Id of the icon
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param speed   Display speed of the particles
     * @param amount  Amount of particles
     * @param players Receivers of the effect
     * @see #sendPacket
     * @see #instantiateIconCrackPacket
     */
    public static void displayIconCrack(Location center, int id, float offsetX, float offsetY, float offsetZ, float speed, int amount, Player... players) {
        sendPacket(Arrays.asList(players), instantiateIconCrackPacket(id, center, offsetX, offsetY, offsetZ, speed, amount));
    }

    /**
     * Displays an icon crack (item break) particle effect which is only visible for all players within a certain range in the world of @param center
     *
     * @param center  Center location of the effect
     * @param range   Range of the visibility
     * @param id      Id of the icon
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param speed   Display speed of the particles
     * @param amount  Amount of particles
     * @throws @IllegalArgumentException if the range is higher than 20
     * @see #sendPacket
     * @see #instantiateIconCrackPacket
     */
    public static void displayIconCrack(Location center, double range, int id, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
        sendPacket(Helper.getPlayers(center, range), instantiateIconCrackPacket(id, center, offsetX, offsetY, offsetZ, speed, amount));
    }

    /**
     * Displays an icon crack (item break) effect which is visible for all players whitin the maximum range of 20 blocks in the world of @param center
     *
     * @param center  Center location of the effect
     * @param id      Id of the icon
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param speed   Display speed of the particles
     * @param amount  Amount of particles
     * @see #displayIconCrack(Location, double, int, float, float, float, float, int)
     */
    public static void displayIconCrack(Location center, int id, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
        displayIconCrack(center, DEFAULT_RANGE, id, offsetX, offsetY, offsetZ, speed, amount);
    }

    /**
     * Displays a block crack (block break) particle effect which is only visible for the specified players
     *
     * @param center  Center location of the effect
     * @param id      Id of the block
     * @param data    Data value
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param amount  Amount of particles
     * @param players Receivers of the effect
     * @see #sendPacket
     * @see #instantiateBlockCrackPacket
     */
    public static void displayBlockCrack(Location center, int id, byte data, float offsetX, float offsetY, float offsetZ, int amount, Player... players) {
        sendPacket(Arrays.asList(players), instantiateBlockCrackPacket(id, data, center, offsetX, offsetY, offsetZ, amount));
    }

    /**
     * Displays a block crack (block break) particle effect which is only visible for all players within a certain range in the world of @param center
     *
     * @param center  Center location of the effect
     * @param range   Range of the visibility
     * @param id      Id of the block
     * @param data    Data value
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param amount  Amount of particles
     * @throws @IllegalArgumentException if the range is higher than 20
     * @see #sendPacket
     * @see #instantiateBlockCrackPacket
     */
    public static void displayBlockCrack(Location center, double range, int id, byte data, float offsetX, float offsetY, float offsetZ, int amount) {
        sendPacket(Helper.getPlayers(center, range), instantiateBlockCrackPacket(id, data, center, offsetX, offsetY, offsetZ, amount));
    }

    /**
     * Displays a block crack (block break) effect which is visible for all players whitin the maximum range of 20 blocks in the world of @param center
     *
     * @param center  Center location of the effect
     * @param id      Id of the block
     * @param data    Data value
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param amount  Amount of particles
     * @see #displayBlockCrack(Location, double, int, byte, float, float, float, int)
     */
    public static void displayBlockCrack(Location center, int id, byte data, float offsetX, float offsetY, float offsetZ, int amount) {
        displayBlockCrack(center, DEFAULT_RANGE, id, data, offsetX, offsetY, offsetZ, amount);
    }

    /**
     * Displays a block dust particle effect which is only visible for the specified players
     *
     * @param center  Center location of the effect
     * @param id      Id of the block
     * @param data    Data value
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param speed   Display speed of the particles
     * @param amount  Amount of particles
     * @param players Receivers of the effect
     * @see #sendPacket
     * @see #instantiateBlockDustPacket
     */
    public static void displayBlockDust(Location center, int id, byte data, float offsetX, float offsetY, float offsetZ, float speed, int amount, Player... players) {
        sendPacket(Arrays.asList(players), instantiateBlockDustPacket(id, data, center, offsetX, offsetY, offsetZ, speed, amount));
    }

    /**
     * Displays a block dust particle effect which is only visible for all players within a certain range in the world of @param center
     *
     * @param center  Center location of the effect
     * @param range   Range of the visibility
     * @param id      Id of the block
     * @param data    Data value
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param speed   Display speed of the particles
     * @param amount  Amount of particles
     * @throws @IllegalArgumentException if the range is higher than 20
     * @see #sendPacket
     * @see #instantiateBlockDustPacket
     */
    public static void displayBlockDust(Location center, double range, int id, byte data, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
        sendPacket(Helper.getPlayers(center, range), instantiateBlockDustPacket(id, data, center, offsetX, offsetY, offsetZ, speed, amount));
    }

    /**
     * Displays a block dust effect which is visible for all players whitin the maximum range of 20 blocks in the world of @param center
     *
     * @param center  Center location of the effect
     * @param id      Id of the block
     * @param data    Data value
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param speed   Display speed of the particles
     * @param amount  Amount of particles
     * @see #displayBlockDust(Location, double, int, byte, float, float, float, float, int)
     */
    public static void displayBlockDust(Location center, int id, byte data, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
        displayBlockDust(center, DEFAULT_RANGE, id, data, offsetX, offsetY, offsetZ, speed, amount);
    }

    /**
     * Represents a runtime exception that can be thrown upon packet instantiation
     */
    private static final class PacketInstantiationException extends RuntimeException {
        private static final long serialVersionUID = 3203085387160737484L;

        /**
         * @param message Message that will be logged
         */
        public PacketInstantiationException(String message) {
            super(message);
        }

        /**
         * @param message Message that will be logged
         * @param cause   Cause of the exception
         */
        public PacketInstantiationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Represents a runtime exception that can be thrown upon packet sending
     */
    private static final class PacketSendingException extends RuntimeException {
        private static final long serialVersionUID = 3203085387160737484L;

        /**
         * @param message Message that will be logged
         * @param cause   Cause of the exception
         */
        public PacketSendingException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static ParticleType getRandom() {
        Random random = new Random();

        int r = random.nextInt(values().length);

        return values()[r];
    }
}
