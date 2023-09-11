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
import it.zs0bye.bettersecurity.bukkit.files.readers.Command;
import it.zs0bye.bettersecurity.bukkit.files.readers.Config;
import it.zs0bye.bettersecurity.bukkit.files.readers.Lang;
import it.zs0bye.bettersecurity.bukkit.files.readers.Tab;
import it.zs0bye.bettersecurity.common.config.ConfigReader;
import lombok.Getter;
import lombok.SneakyThrows;

@Getter
public enum FileType {
    CONFIG(1, "config", null, Config.class),
    LANG(2, "en_US", "languages", Lang.class),
    TAB(3, "tabcomplete", "modules", Tab.class),
    COMMAND(4, "commands", "modules", Command.class);

    private final BetterSecurityBukkit plugin;
    private final int priority;
    private final String name, directory;
    private final Class<? extends ConfigReader> reader;

    FileType(final int priority, final String name, final String directory, final Class<? extends ConfigReader> reader) {
        this.plugin = BetterSecurityBukkit.getInstance();
        this.priority = priority;
        this.name = name;
        this.directory = directory;
        this.reader = reader;
    }

    public String getName() {
        if(this != FileType.LANG) return this.name;
        if(!this.plugin.getHandlers().containsKey(FileType.CONFIG)) return this.name;
        return Config.SETTINGS_LOCALE.getString();
    }

    @SneakyThrows
    public ConfigReader[] getReader() {
        return this.reader.getEnumConstants();
    }

}
