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

package it.zs0bye.bettersecurity.bungee.files.readers;

import it.zs0bye.bettersecurity.bungee.BetterSecurityBungee;
import it.zs0bye.bettersecurity.bungee.files.FileConfigReader;
import it.zs0bye.bettersecurity.bungee.files.FileType;
import it.zs0bye.bettersecurity.common.BetterUser;
import it.zs0bye.bettersecurity.common.config.ConfigReader;
import lombok.Getter;

import java.util.*;

@Getter
public enum Command implements ConfigReader {
    INSTANCE(""),
    WARNING("warning"),
    METHOD("method"),
    FORCE_CHECK("force_check"),
    EXECUTORS("executors"),
    BLOCKS_COMMANDS("commands"),
    SERVER_MODE_ENABLED("server_mode.enabled"),
    SERVER_MODE_SERVERS("server_mode.servers"),
    SERVER_MODE_METHOD(".method"),
    SERVER_MODE_EXECUTORS(".executors"),
    SERVER_MODE_COMMANDS(".commands");

    private final FileConfigReader reader;

    @Getter
    private final String path;

    @Getter
    private final FileType type;

    Command(final String path) {
        this.reader = new FileConfigReader(BetterSecurityBungee.getInstance(), path, FileType.COMMAND);
        this.path = this.reader.getPath();
        this.type = this.reader.getType();
    }

    @Override
    public String variables(final String... var) {
        return this.reader.variables(var);
    }

    @Override
    public void reloadConfig() {
        this.reader.reloadConfig();
    }

    @Override
    public Object getObject(final String... var) {
        return this.reader.getObject(var);
    }

    @Override
    public String getString(final String... var) {
        return this.reader.getString(var);
    }

    @Override
    public List<String> getStringList(final String... var) {
        return this.reader.getStringList(var);
    }

    @Override
    public boolean getBoolean(final String... var) {
        return this.reader.getBoolean(var);
    }

    @Override
    public boolean contains(final String... var) {
        return this.reader.contains(var);
    }

    @Override
    public int getInt(final String... var) {
        return this.reader.getInt(var);
    }

    @Override
    public String getCustomString(final String replace) {
        return this.reader.getCustomString(replace);
    }

    @Override
    public String getCustomString(String... var) {
        return this.reader.getCustomString(var);
    }

    @Override
    public void send(final BetterUser user, final String... var) {
        this.reader.send(user, var);
    }

    @Override
    public void send(final BetterUser user, final Map<String, String> placeholders, final String... var) {
        this.reader.send(user, placeholders, var);
    }

    @Override
    public void sendList(final BetterUser user, final String... var) {
        this.reader.sendList(user, new HashMap<>(), var);
    }

    @Override
    public void sendList(final BetterUser user, final Map<String, String> placeholders, final String... var) {
        this.reader.sendList(user, placeholders, var);
    }

    @Override
    public Collection<String> getSection(final String... var) {
        return this.reader.getSection(var);
    }
}
