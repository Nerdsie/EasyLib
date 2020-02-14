package com.collinsrichard.myapi.objects;

import java.util.HashMap;

public class MetaClass {
    private HashMap<String, Object> meta = new HashMap<String, Object>();

    public final boolean hasMeta(String key) {
        if (meta.containsKey(key.toLowerCase())) {
            return true;
        }

        return false;
    }

    public final boolean hasMeta(String key, Object value) {
        if (hasMeta(key)) {
            if (getMeta(key).equals(value)) {
                return true;
            }
        }

        return false;
    }

    public final Object getMeta(String key) {
        if (hasMeta(key)) {
            return meta.get(key.toLowerCase());
        }

        return null;
    }

    public final String getMetaString(String key) {
        return "" + getMeta(key);
    }

    public final String getMetaString(String key, String def) {
        if (hasMeta(key)) {
            return "" + getMeta(key);
        }

        return def;
    }

    public final boolean getMetaBoolean(String key, boolean def) {
        if (hasMeta(key)) {
            return Boolean.parseBoolean(getMetaString(key));
        } else {
            return def;
        }
    }

    public final int getMetaInteger(String key, int def) {
        if (hasMeta(key)) {
            return Integer.parseInt(getMetaString(key));
        }

        return def;
    }

    public final double getMetaDouble(String key, double def) {
        if (hasMeta(key)) {
            return Double.parseDouble(getMetaString(key));
        }

        return def;
    }

    public final float getMetaFloat(String key, Float def) {
        if (hasMeta(key)) {
            return Float.parseFloat(getMetaString(key));
        }

        return def;
    }

    public final void setMeta(String key, Object value) {
        meta.put(key, value);
    }
}
