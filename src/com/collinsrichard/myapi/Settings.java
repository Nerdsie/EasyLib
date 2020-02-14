package com.collinsrichard.myapi;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Settings {
    public static String basePerms = "myapi";
    public static final String CHEST_MENU_PREFIX = "\u2740 ";
    public static HashMap<String, String> items = new HashMap<String, String>();

    public static final String DISPLAY_NAME = "CUSTOM_DISPLAY";

    public void load() {
        String str = "";
        StringBuffer buf = new StringBuffer();
        InputStream is = Helper.getAPI().getResource("materials.yml");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        int count = 0;

        try {
            if (is != null) {
                while ((str = reader.readLine()) != null) {
                    count++;

                    String split[] = str.split(",");
                    items.put(split[0] + ":" + split[1], split[2].substring(1, split[2].length() - 1));
                }
                is.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}