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

package it.zs0bye.bettersecurity.bukkit.files;

import it.zs0bye.bettersecurity.bukkit.BetterSecurityBukkit;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

@Getter
public class SpigotFile {

    private final BetterSecurityBukkit plugin;
    private final File file;

    public SpigotFile(final BetterSecurityBukkit plugin) {
        this.plugin = plugin;
        this.file = new File(this.plugin.getDataFolder(), "../../spigot.yml");
    }

    public FileConfiguration getConfig() {
        return YamlConfiguration.loadConfiguration(this.file);
    }

}
