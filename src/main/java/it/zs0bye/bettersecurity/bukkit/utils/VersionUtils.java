package it.zs0bye.bettersecurity.bukkit.utils;

import org.bukkit.Bukkit;

public class VersionUtils {

    private static double getVersion() {
        final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        final String[] parts = version.split("_");
        return Double.parseDouble(parts[0].substring(1) + "." + parts[1]);
    }

    public static boolean checkVersion(final double... versions) {
        return it.zs0bye.bettersecurity.common.utils.VersionUtils.checkVersion(getVersion(), versions);
    }

    public static boolean legacy() {
        return it.zs0bye.bettersecurity.common.utils.VersionUtils.legacy(getVersion());
    }

}
