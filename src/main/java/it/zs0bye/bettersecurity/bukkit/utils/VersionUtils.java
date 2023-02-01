package it.zs0bye.bettersecurity.bukkit.utils;

import org.bukkit.Bukkit;

public class VersionUtils {

    public static double getVersion() {
        final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        final String[] parts = version.split("_");
        return Double.parseDouble(parts[0].substring(1) + "." + parts[1]);
    }

    public static boolean checkVersion(final double... versions) {
        for (double version : versions) {
            if (version == getVersion()) return true;
        }
        return false;
    }

    public static boolean legacy() {
        return checkVersion(1.8, 1.9, 1.10, 1.11, 1.12);
    }

}
