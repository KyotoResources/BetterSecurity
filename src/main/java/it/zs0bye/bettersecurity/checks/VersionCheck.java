package it.zs0bye.bettersecurity.checks;

import org.bukkit.Bukkit;

public class VersionCheck {

    public static boolean getV1_8() {
        if (Bukkit.getVersion().contains("1.8")) {
            return true;
        } else if (Bukkit.getVersion().contains("1.8.3")) {
            return true;
        } else if (Bukkit.getVersion().contains("1.8.4")) {
            return true;
        } else if (Bukkit.getVersion().contains("1.8.5")) {
            return true;
        } else if (Bukkit.getVersion().contains("1.8.6")) {
            return true;
        } else if (Bukkit.getVersion().contains("1.8.7")) {
            return true;
        } else return Bukkit.getVersion().contains("1.8.8");
    }

    public static boolean getV1_9() {
        if (Bukkit.getVersion().contains("1.9")) {
            return true;
        } else if (Bukkit.getVersion().contains("1.9.2")) {
            return true;
        } else return (Bukkit.getVersion().contains("1.9.4"));
    }

    public static boolean getV1_10() {
        if(Bukkit.getVersion().contains("1.10")) {
            return true;
        } else return(Bukkit.getVersion().contains("1.10.2"));
    }

    public static boolean getV1_11() {
        if(Bukkit.getVersion().contains("1.11")) {
            return true;
        } else if(Bukkit.getVersion().contains("1.11.1")) {
            return true;
        } else return(Bukkit.getVersion().contains("1.11.2"));
    }

    public static boolean getV1_12() {
        if(Bukkit.getVersion().contains("1.12")) {
            return true;
        } else if(Bukkit.getVersion().contains("1.12.1")) {
            return true;
        } else return(Bukkit.getVersion().contains("1.12.2"));
    }

    public static boolean legacy() {
        if (!VersionCheck.getV1_8()
                && !VersionCheck.getV1_9()
                && !VersionCheck.getV1_10()
                && !VersionCheck.getV1_11()
                && !VersionCheck.getV1_12()) return false;

        return VersionCheck.getV1_8()
                || VersionCheck.getV1_9()
                || VersionCheck.getV1_10()
                || VersionCheck.getV1_11()
                || VersionCheck.getV1_12();
    }

}
