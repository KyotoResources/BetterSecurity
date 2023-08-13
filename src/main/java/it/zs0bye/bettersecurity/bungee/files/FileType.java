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

package it.zs0bye.bettersecurity.bungee.files;

import it.zs0bye.bettersecurity.bungee.files.readers.Command;
import it.zs0bye.bettersecurity.bungee.files.readers.Config;
import it.zs0bye.bettersecurity.bungee.files.readers.Lang;
import it.zs0bye.bettersecurity.bungee.files.readers.Tab;
import it.zs0bye.bettersecurity.bungee.modules.Module;
import lombok.Getter;

import java.lang.module.Configuration;

@Getter
public enum FileType {
    CONFIG(Config.values(), "config", null),
    LANG(Lang.values(), defaultLocale(), "languages"),
    COMMAND(Command.values(), "tabcomplete", "modules"),
    TAB(Tab.values(), "commands", "modules");

    private final ConfigReader[] reader;
    private final String name, directory;

    FileType(final ConfigReader[] reader, final String name, final String directory) {
        this.reader = reader;
        this.name = name;
        this.directory = directory;
    }

    private static String defaultLocale() {
        if(Config.SETTINGS_LOCALE.getConfig() == null) return "en_US";
        return Config.SETTINGS_LOCALE.getString();
    }

}
