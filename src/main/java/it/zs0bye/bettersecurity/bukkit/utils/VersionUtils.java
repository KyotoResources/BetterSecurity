/*
 * Security plugin for your server - https://github.com/KyotoResources/BetterSecurity
 * Copyright (C) 2023 KyotoResources
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
