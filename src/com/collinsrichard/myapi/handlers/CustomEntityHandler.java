package com.collinsrichard.myapi.handlers;

import com.collinsrichard.myapi.objects.custom.CustomEntity;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.player.PlayerUnleashEntityEvent;
import org.spigotmc.event.entity.EntityDismountEvent;
import org.spigotmc.event.entity.EntityMountEvent;

public class CustomEntityHandler {
    private CustomEntity entity = null;

    public final CustomEntity getEntity() {
        return entity;
    }

    public final CustomEntityHandler setEntity(CustomEntity entity) {
        this.entity = entity;
        return this;
    }

    public void onEntityDamage(EntityDamageEvent e) {

    }

    public void onEntityDeath(EntityDeathEvent e) {

    }

    public void onEntityTeleport(EntityTeleportEvent e) {

    }

    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {

    }

    public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {

    }

    public void onProjectileLaunch(ProjectileLaunchEvent e) {

    }

    public void onProjectileHit(ProjectileHitEvent e) {

    }

    public void onEntityDamageEntity(EntityDamageByEntityEvent e) {

    }

    public void onPlayerUnleashEntity(PlayerUnleashEntityEvent e) {

    }

    public void onPlayerShearEntity(PlayerShearEntityEvent e) {
    }

    public void onPlayerLeashEntity(PlayerLeashEntityEvent e) {
    }

    public void onEntityUnleash(EntityUnleashEvent e) {
    }

    public void onEntityTargetLivingEntity(EntityTargetLivingEntityEvent e) {
    }

    public void onEntityTarget(EntityTargetEvent e) {
    }

    public void onEntityTame(EntityTameEvent e) {
    }

    public void onEntitySpawn(EntitySpawnEvent e) {
    }

    public void onEntityShootBow(EntityShootBowEvent e) {
    }

    public void onEntityRegainHealth(EntityRegainHealthEvent e) {
    }

    public void onEntityPortalExit(EntityPortalExitEvent e) {
    }

    public void onEntityPortal(EntityPortalEvent e) {
    }

    public void onEntityPortalEnter(EntityPortalEnterEvent e) {
    }

    public void onEntityMount(EntityMountEvent e) {
    }

    public void onEntityExplode(EntityExplodeEvent e) {
    }

    public void onEntityDismount(EntityDismountEvent e) {
    }

    public void onEntityDamageByBlock(EntityDamageByBlockEvent e) {
    }

    public void onEntityCreatePortal(EntityCreatePortalEvent e) {
    }

    public void onEntityCombuest(EntityCombustEvent e) {
    }

    public void onEntityCombustEntity(EntityCombustByEntityEvent e) {
    }

    public void onEntityCombustByEntity(EntityCombustByEntityEvent e) {
    }

    public void onEntityCombustByBlock(EntityCombustByBlockEvent e) {
    }

    public void onEntityChangeBlock(EntityChangeBlockEvent e) {
    }

    public void onEntityBreakDoor(EntityBreakDoorEvent e) {
    }

    public void onEntityBlockForm(EntityBlockFormEvent e) {
    }
}