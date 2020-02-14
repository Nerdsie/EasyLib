package com.collinsrichard.myapi.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;

public class GeoHelper {
    public static ArrayList<Location> getCircumference(double radius, int locations) {
        ArrayList<Location> list = new ArrayList<Location>();

        double rPer = Math.toRadians(360.0 / locations);

        for (int i = 0; i < locations; i++) {
            double totalR = rPer * i;

            double x = Math.sin(totalR) * radius;
            double z = Math.cos(totalR) * radius;

            list.add(new Location(Bukkit.getWorlds().get(0), x, 0.0, z));
        }

        return list;
    }

    public static ArrayList<Location> getCylinder(double radius, int rows, double spacing, int locationsPerCircle) {
        ArrayList<Location> list = getCircumference(radius, locationsPerCircle);

        for (int i = 1; i < rows; i++) {
            ArrayList<Location> parse = (ArrayList<Location>) list.clone();


            for (Location l : parse) {
                list.add(l.clone().add(0.0, spacing, 0.0));
            }
        }

        return list;
    }

    public static ArrayList<Location> getCylinder(double radius, int rows, int locationsPerCircle) {
        return getCylinder(radius, rows, 0.5, locationsPerCircle);
    }
}
