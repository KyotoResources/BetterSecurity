package it.zs0bye.bettersecurity.common.utils;

public class VersionUtils {

    public static boolean checkVersion(final double current, final double... versions) {
        for (double version : versions) {
            if (version == current) return true;
        }
        return false;
    }

    public static boolean legacy(final double current) {
        return checkVersion(current, 1.8, 1.9, 1.10, 1.11, 1.12);
    }

}
