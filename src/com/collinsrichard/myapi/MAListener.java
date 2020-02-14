package com.collinsrichard.myapi;

import com.collinsrichard.myapi.events.NewPlayerEvent;
import com.collinsrichard.myapi.events.PlayerHurtEvent;
import com.collinsrichard.myapi.events.PlayerHurtPlayerEvent;
import com.collinsrichard.myapi.events.PlayerKillPlayerEvent;
import com.collinsrichard.myapi.events.menu.*;
import com.collinsrichard.myapi.managers.TeleportManager;
import com.collinsrichard.myapi.objects.TeleportTimer;
import com.collinsrichard.myapi.objects.custom.CustomBlock;
import com.collinsrichard.myapi.objects.custom.CustomEntity;
import com.collinsrichard.myapi.objects.custom.CustomItem;
import com.collinsrichard.myapi.objects.display.Display;
import com.collinsrichard.myapi.objects.menu.ChestMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.event.entity.EntityDismountEvent;
import org.spigotmc.event.entity.EntityMountEvent;

import java.util.HashMap;
import java.util.UUID;

public class MAListener implements Listener {
    public MyAPI plugin;

    public MAListener(MyAPI pl) {
        plugin = pl;
    }

    public HashMap<UUID, PlayerHurtPlayerEvent> hurts = new HashMap<UUID, PlayerHurtPlayerEvent>();

    @EventHandler
    public void on(ProjectileHitEvent e) {
        if (CustomEntity.isCustomEntity(e.getEntity())) {
            CustomEntity.getCustomEntity(e.getEntity()).getHandler().onProjectileHit(e);
        }
    }

    @EventHandler
    public void onProjectileLaunchEvent(ProjectileLaunchEvent e) {
        if (e.getEntity().getShooter() instanceof Player) {
            Player player = (Player) e.getEntity().getShooter();

            final ItemStack check = player.getItemInHand();

            try {
                if (CustomItem.isCustomItem(player.getItemInHand())) {
                    CustomItem.getCustomItem(player.getItemInHand()).getHandler().onProjectileLaunch(e);
                    e.setCancelled(true);
                    return;
                }

                if (CustomEntity.isCustomEntity(e.getEntity())) {
                    CustomEntity.getCustomEntity(e.getEntity()).getHandler().onProjectileLaunch(e);
                }

            } catch (Exception ex) {

            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        try {
            if (CustomItem.isCustomItem(e.getPlayer().getItemInHand())) {
                CustomItem.getCustomItem(e.getPlayer().getItemInHand()).getHandler().onDrop(e);
                return;
            }
        } catch (Exception ex) {

        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        try {
            if (CustomItem.isCustomItem(e.getItemInHand())) {
                CustomItem.getCustomItem(e.getItemInHand()).getHandler().onBlockPlace(e);
                return;
            }
        } catch (Exception ex) {

        }

    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        try {
            if (CustomItem.isCustomItem(e.getPlayer().getItemInHand())) {
                CustomItem.getCustomItem(e.getPlayer().getItemInHand()).getHandler().onBlockBreak(e);
            }
        } catch (Exception ex) {

        }

        if (CustomBlock.isCustomBlock(e.getBlock())) {
            CustomBlock.getCustomBlock(e.getBlock()).getHandler().onBlockBreak(e);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (CustomBlock.isCustomBlock(e.getClickedBlock())) {
                    CustomBlock.getCustomBlock(e.getClickedBlock()).getHandler().onInteract(e);
                }
            }

            try {
                if (CustomItem.isCustomItem(e.getPlayer().getItemInHand())) {
                    CustomItem.getCustomItem(e.getPlayer().getItemInHand()).getHandler().onInteract(e);
                    e.setCancelled(true);
                    return;
                }
            } catch (Exception ex) {

            }
        }
    }

    @EventHandler
    public void onInteractWithEntity(PlayerInteractEntityEvent e) {
        for (CustomItem item : CustomItem.items) {
            try {
                if (item.isMatch(e.getPlayer().getInventory().getItemInHand())) {
                    item.getHandler().onInteractWithEntity(e);
                    e.setCancelled(true);
                    return;
                }
            } catch (Exception ex) {

            }
        }

        if (CustomEntity.isCustomEntity(e.getRightClicked())) {
            CustomEntity.getCustomEntity(e.getRightClicked()).getHandler().onPlayerInteractEntity(e);
        }
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent e) {
        if (!e.getInventory().getName().startsWith(Settings.CHEST_MENU_PREFIX)) {
            ChestMenu.removePlayer(e.getPlayer().getName());
        } else {
            MenuOpenEvent event = new MenuOpenEvent(e, (Player) e.getPlayer(), e.getInventory());
            event.getChestMenu().getHandler().onOpen(event);

            Bukkit.getPluginManager().callEvent(event);

            if (event.isCanceled()) {
                e.setCancelled(true);
                ChestMenu.removePlayer(e.getPlayer().getName());
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (ChestMenu.hasOpen(e.getPlayer().getName())) {
            MenuCloseEvent event = new MenuCloseEvent(e, (Player) e.getPlayer(), e.getInventory());
            event.getChestMenu().getHandler().onClose(event);

            Bukkit.getPluginManager().callEvent(event);

            if (event.isCanceled()) {
                event.getChestMenu().open();
            } else {
                ChestMenu.removePlayer(e.getPlayer().getName());
            }
        }
    }

    @EventHandler
    public void onRespawn(final PlayerRespawnEvent e) {
        new BukkitRunnable() {

            @Override
            public void run() {
                if (hurts.containsKey(e.getPlayer().getUniqueId())) {
                    hurts.remove(e.getPlayer().getUniqueId());
                }
            }
        }.runTaskLater(Helper.getAPIPlugin(), 5L);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventory(InventoryClickEvent e) {
        if (e.getCurrentItem() != null) {
            try {
                if (CustomItem.isCustomItem(e.getCurrentItem())) {
                    CustomItem.getCustomItem(e.getCurrentItem()).getHandler().onInventoryClick(e);
                }
            } catch (Exception ex) {

            }
        }

        if (e.getInventory().getName().toLowerCase().contains(Settings.CHEST_MENU_PREFIX)) {
            e.setCancelled(true);

            final HumanEntity player = e.getWhoClicked();

            e.setCancelled(true);

            if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
                if (e.getInventory() == null) {
                    OutsideClickEvent event = new OutsideClickEvent(e, (Player) e.getWhoClicked(), e.getSlot(), e.getInventory());
                    event.getChestMenu().onOutsideClick(event);

                    Bukkit.getPluginManager().callEvent(event);

                    //Close inventory??
                    if (event.closeInventory()) {
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                player.closeInventory();
                            }
                        });
                    }
                } else {
                    EmptyClickEvent event = new EmptyClickEvent(e, (Player) e.getWhoClicked(), e.getSlot(), e.getInventory());
                    event.getChestMenu().getHandler().onBlankClick(event);

                    Bukkit.getPluginManager().callEvent(event);

                    if (event.getNewMenu() != null) {
                        final ChestMenu newInv = event.getNewMenu();

                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                newInv.open(player);
                            }
                        });
                    }
                }
            } else {
                int max = e.getInventory().getSize();

                if (e.getRawSlot() > max) {
                    ItemClickEvent event = new ItemClickEvent(e, (Player) e.getWhoClicked(), e.getCurrentItem(), e.getSlot(), e.getInventory());
                    event.getChestMenu().getHandler().onItemClick(event);

                    Bukkit.getPluginManager().callEvent(event);

                    //Close inventory??
                    if (event.closeInventory()) {
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                player.closeInventory();
                            }
                        });
                    }

                    if (event.getNewMenu() != null) {
                        final ChestMenu newInv = event.getNewMenu();

                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                newInv.open(player);
                            }
                        });
                    }
                } else {
                    IconClickEvent event = new IconClickEvent(e, (Player) e.getWhoClicked(), e.getCurrentItem(), e.getSlot(), e.getInventory());
                    event.getIcon().getHandler().onClick(event);

                    Bukkit.getPluginManager().callEvent(event);


                    //Close inventory??
                    if (event.closeInventory()) {
                        Helper.safeInventoryClose(player);
                    }

                    if (event.getNewMenu() != null) {
                        final ChestMenu newInv = event.getNewMenu();

                        newInv.open(player);
                    }
                }

                return;
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (!e.getPlayer().hasPlayedBefore()) {
            NewPlayerEvent event = new NewPlayerEvent(e.getPlayer());
            Bukkit.getPluginManager().callEvent(event);
        }

        if (Display.has(e.getPlayer())) {
            Display.get(e.getPlayer()).setVisible(true);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (Display.has(e.getPlayer())) {
            Display.remove(e.getPlayer());
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        if (Display.has(e.getPlayer())) {
            Display.remove(e.getPlayer());
        }
    }


    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();

        if (e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockY() != e.getTo().getBlockY() || e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {
            cancelTelport(player);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();

            cancelTelport(player);

            Player p = (Player) e.getEntity();

            PlayerHurtEvent event = new PlayerHurtEvent(p, e);
            Bukkit.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                e.setCancelled(true);
            }
        }

        if (CustomEntity.isCustomEntity(e.getEntity())) {
            CustomEntity.getCustomEntity(e.getEntity()).getHandler().onEntityDamage(e);
        }
    }

    public void cancelTelport(Player player) {
        if (TeleportManager.isTeleporting(player)) {
            TeleportTimer timer = TeleportManager.getTeleport(player);

            if (!timer.active) {
                TeleportManager.stopTeleporting(player);
                player.sendMessage(ChatColor.RED + "Pending teleport cancelled");
            }
        }
    }

    @EventHandler
    public void onEntityDeath(final EntityDeathEvent e) {
        if (e.getEntity() instanceof Player) {
            final Player victim = (Player) e.getEntity();

            Player attacker = null;

            EntityDamageEvent lastDamageCause = victim.getLastDamageCause();

            if (lastDamageCause instanceof EntityDamageByEntityEvent) {
                EntityDamageByEntityEvent entityEvent = (EntityDamageByEntityEvent) lastDamageCause;

                if (entityEvent.getDamager() instanceof Player) {
                    attacker = (Player) entityEvent.getDamager();
                } else if (entityEvent.getDamager() instanceof Arrow) {
                    Arrow arrow = (Arrow) entityEvent.getDamager();

                    if (arrow.getShooter() instanceof Player) {
                        attacker = (Player) arrow.getShooter();
                    }
                }

                if (attacker != null && victim != null) {
                    PlayerKillPlayerEvent event = new PlayerKillPlayerEvent(attacker, victim, e, hurts.get(victim.getUniqueId()), true);
                    Bukkit.getPluginManager().callEvent(event);

                    return;
                }
            }

            final Player finalAttacker = attacker;
            new BukkitRunnable() {

                @Override
                public void run() {
                    if (hurts.containsKey(victim.getUniqueId())) {
                        PlayerKillPlayerEvent event = new PlayerKillPlayerEvent(hurts.get(victim.getUniqueId()).getAttacker(), victim, e, hurts.get(victim.getUniqueId()), false);
                        Bukkit.getPluginManager().callEvent(event);

                        hurts.remove(victim.getUniqueId());
                    }
                }
            }.runTaskLater(Helper.getAPI(), 1L);
        }

        if (CustomEntity.isCustomEntity(e.getEntity())) {
            CustomEntity.getCustomEntity(e.getEntity()).getHandler().onEntityDeath(e);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            Player victim = (Player) e.getEntity();
            Player attacker = null;

            if (e.getDamager() instanceof Player) {
                attacker = (Player) e.getDamager();
            } else if (e.getDamager() instanceof Arrow) {
                Arrow arrow = (Arrow) e.getDamager();

                if (arrow.getShooter() instanceof Player) {
                    attacker = (Player) arrow.getShooter();
                }
            } else if (e.getDamager() instanceof ThrownPotion) {
                ThrownPotion pot = (ThrownPotion) e.getDamager();

                if (pot.getShooter() instanceof Player) {
                    attacker = (Player) pot.getShooter();
                }
            }

            if (attacker != null && victim != null) {
                PlayerHurtPlayerEvent event = new PlayerHurtPlayerEvent(attacker, victim, e);
                Bukkit.getPluginManager().callEvent(event);

                hurts.put(victim.getUniqueId(), event);

                if (event.isCancelled()) {
                    e.setCancelled(true);
                    return;
                }
            }
        }

        if (CustomEntity.isCustomEntity(e.getEntity())) {
            CustomEntity.getCustomEntity(e.getEntity()).getHandler().onEntityDamageByEntity(e);
        }

        if (CustomEntity.isCustomEntity(e.getDamager())) {
            CustomEntity.getCustomEntity(e.getDamager()).getHandler().onEntityDamageEntity(e);
        }
    }

    @EventHandler
    public void onEntity(EntityBlockFormEvent e) {
        if (CustomEntity.isCustomEntity(e.getEntity())) {
            CustomEntity.getCustomEntity(e.getEntity()).getHandler().onEntityBlockForm(e);
        }
    }

    @EventHandler
    public void onEntity(EntityBreakDoorEvent e) {
        if (CustomEntity.isCustomEntity(e.getEntity())) {
            CustomEntity.getCustomEntity(e.getEntity()).getHandler().onEntityBreakDoor(e);
        }
    }

    @EventHandler
    public void on(EntityChangeBlockEvent e) {
        if (CustomEntity.isCustomEntity(e.getEntity())) {
            CustomEntity.getCustomEntity(e.getEntity()).getHandler().onEntityChangeBlock(e);
        }
    }

    @EventHandler
    public void on(EntityCombustByBlockEvent e) {
        if (CustomEntity.isCustomEntity(e.getEntity())) {
            CustomEntity.getCustomEntity(e.getEntity()).getHandler().onEntityCombustByBlock(e);
        }
    }

    @EventHandler
    public void on(EntityCombustByEntityEvent e) {
        if (CustomEntity.isCustomEntity(e.getEntity())) {
            CustomEntity.getCustomEntity(e.getEntity()).getHandler().onEntityCombustByEntity(e);
        }

        if (CustomEntity.isCustomEntity(e.getCombuster())) {
            CustomEntity.getCustomEntity(e.getCombuster()).getHandler().onEntityCombustEntity(e);
        }
    }

    @EventHandler
    public void on(EntityCombustEvent e) {
        if (CustomEntity.isCustomEntity(e.getEntity())) {
            CustomEntity.getCustomEntity(e.getEntity()).getHandler().onEntityCombuest(e);
        }
    }

    @EventHandler
    public void on(EntityCreatePortalEvent e) {
        if (CustomEntity.isCustomEntity(e.getEntity())) {
            CustomEntity.getCustomEntity(e.getEntity()).getHandler().onEntityCreatePortal(e);
        }
    }

    @EventHandler
    public void on(EntityDamageByBlockEvent e) {
        if (CustomEntity.isCustomEntity(e.getEntity())) {
            CustomEntity.getCustomEntity(e.getEntity()).getHandler().onEntityDamageByBlock(e);
        }
    }

    @EventHandler
    public void on(EntityDismountEvent e) {
        if (CustomEntity.isCustomEntity(e.getEntity())) {
            CustomEntity.getCustomEntity(e.getEntity()).getHandler().onEntityDismount(e);
        }
    }

    @EventHandler
    public void on(EntityExplodeEvent e) {
        if (CustomEntity.isCustomEntity(e.getEntity())) {
            CustomEntity.getCustomEntity(e.getEntity()).getHandler().onEntityExplode(e);
        }
    }

    @EventHandler
    public void on(EntityMountEvent e) {
        if (CustomEntity.isCustomEntity(e.getEntity())) {
            CustomEntity.getCustomEntity(e.getEntity()).getHandler().onEntityMount(e);
        }
    }

    @EventHandler
    public void on(EntityPortalEnterEvent e) {
        if (CustomEntity.isCustomEntity(e.getEntity())) {
            CustomEntity.getCustomEntity(e.getEntity()).getHandler().onEntityPortalEnter(e);
        }
    }

    @EventHandler
    public void on(EntityPortalEvent e) {
        if (CustomEntity.isCustomEntity(e.getEntity())) {
            CustomEntity.getCustomEntity(e.getEntity()).getHandler().onEntityPortal(e);
        }
    }

    @EventHandler
    public void on(EntityPortalExitEvent e) {
        if (CustomEntity.isCustomEntity(e.getEntity())) {
            CustomEntity.getCustomEntity(e.getEntity()).getHandler().onEntityPortalExit(e);
        }
    }

    @EventHandler
    public void on(EntityRegainHealthEvent e) {
        if (CustomEntity.isCustomEntity(e.getEntity())) {
            CustomEntity.getCustomEntity(e.getEntity()).getHandler().onEntityRegainHealth(e);
        }
    }

    @EventHandler
    public void on(EntityShootBowEvent e) {
        if (CustomEntity.isCustomEntity(e.getEntity())) {
            CustomEntity.getCustomEntity(e.getEntity()).getHandler().onEntityShootBow(e);
        }
    }

    @EventHandler
    public void on(EntitySpawnEvent e) {
        if (CustomEntity.isCustomEntity(e.getEntity())) {
            CustomEntity.getCustomEntity(e.getEntity()).getHandler().onEntitySpawn(e);
        }
    }

    @EventHandler
    public void on(EntityTameEvent e) {
        if (CustomEntity.isCustomEntity(e.getEntity())) {
            CustomEntity.getCustomEntity(e.getEntity()).getHandler().onEntityTame(e);
        }
    }

    @EventHandler
    public void on(EntityTargetEvent e) {
        if (CustomEntity.isCustomEntity(e.getEntity())) {
            CustomEntity.getCustomEntity(e.getEntity()).getHandler().onEntityTarget(e);
        }
    }

    @EventHandler
    public void on(EntityTargetLivingEntityEvent e) {
        if (CustomEntity.isCustomEntity(e.getEntity())) {
            CustomEntity.getCustomEntity(e.getEntity()).getHandler().onEntityTargetLivingEntity(e);
        }
    }

    @EventHandler
    public void on(EntityUnleashEvent e) {
        if (CustomEntity.isCustomEntity(e.getEntity())) {
            CustomEntity.getCustomEntity(e.getEntity()).getHandler().onEntityUnleash(e);
        }
    }

    @EventHandler
    public void on(PlayerLeashEntityEvent e) {
        if (CustomEntity.isCustomEntity(e.getEntity())) {
            CustomEntity.getCustomEntity(e.getEntity()).getHandler().onPlayerLeashEntity(e);
        }
    }

    @EventHandler
    public void on(PlayerShearEntityEvent e) {
        if (CustomEntity.isCustomEntity(e.getEntity())) {
            CustomEntity.getCustomEntity(e.getEntity()).getHandler().onPlayerShearEntity(e);
        }
    }

    @EventHandler
    public void on(PlayerUnleashEntityEvent e) {
        if (CustomEntity.isCustomEntity(e.getEntity())) {
            CustomEntity.getCustomEntity(e.getEntity()).getHandler().onPlayerUnleashEntity(e);
        }
    }

    @EventHandler
    public void onTeleport(EntityTeleportEvent e) {
        if (CustomEntity.isCustomEntity(e.getEntity())) {
            CustomEntity.getCustomEntity(e.getEntity()).getHandler().onEntityTeleport(e);
        }
    }
}
