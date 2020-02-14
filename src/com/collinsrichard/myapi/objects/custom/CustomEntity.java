package com.collinsrichard.myapi.objects.custom;

import com.collinsrichard.myapi.handlers.CustomEntityHandler;
import com.collinsrichard.myapi.objects.MetaClass;
import org.bukkit.entity.Entity;

import java.util.HashMap;
import java.util.UUID;

public class CustomEntity extends MetaClass {
    // ========== STATIC METHODS ================= //

    public static HashMap<UUID, CustomEntity> entites = new HashMap<UUID, CustomEntity>();

    public static boolean isCustomEntity(Entity entity) {
        try {
            return entites.containsKey(entity.getUniqueId());
        } catch (Exception e) {
            return false;
        }
    }

    public static CustomEntity getCustomEntity(Entity entity) {
        if (isCustomEntity(entity)) {
            return entites.get(entity.getUniqueId());
        }

        return null;
    }

    // ========== END STATIC ===================//

    private CustomEntityHandler handler = new CustomEntityHandler();
    private Entity entity = null;

    public CustomEntity(Entity entity) {
        this.entity = entity;
        entites.put(getEntity().getUniqueId(), this);
    }

    public CustomEntityHandler getHandler() {
        return handler;
    }

    public CustomEntity setHandler(CustomEntityHandler handler) {
        this.handler = handler;
        handler.setEntity(this);

        return this;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }
}
