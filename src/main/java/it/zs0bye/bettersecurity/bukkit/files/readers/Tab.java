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

package it.zs0bye.bettersecurity.bukkit.files.readers;

import it.zs0bye.bettersecurity.bukkit.BetterSecurityBukkit;
import it.zs0bye.bettersecurity.bukkit.files.FileConfigReader;
import it.zs0bye.bettersecurity.bukkit.files.FileType;
import it.zs0bye.bettersecurity.common.BetterUser;
import it.zs0bye.bettersecurity.common.config.ConfigReader;
import lombok.Getter;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Tab implements ConfigReader {
    INSTANCE(""),
    PARTIAL_MATCHES("partial_matches"),
    GLOBAL_BYPASS_ENABLED("global_bypass.enabled"),
    GLOBAL_BYPASS_METHOD("global_bypass.method"),
    GLOBAL_BYPASS_PLAYERS("global_bypass.players"),
    BASIC_MODE_METHOD("basic_mode.method"),
    BASIC_MODE_LIST("basic_mode.list"),
    ADV_MODE_ENABLED("advanced_mode.enabled"),
    ADV_MODE_ENABLED_GROUPS("advanced_mode.enabled_groups"),
    ADV_MODE_GROUPS("advanced_mode.groups"),
    ADV_MODE_GROUPS_PRIORITY(".priority"),
    ADV_MODE_GROUPS_METHOD(".method"),
    ADV_MODE_GROUPS_LIST(".list"),
    ADV_MODE_GROUPS_TARGET_WORLDS(".target_worlds"),
    ADV_MODE_GROUPS_REQUIRED_PERMISSION(".required_permission"),
    ADV_MODE_GROUPS_REQUIRED_PROPERTY(".required_property");

    private final FileConfigReader reader;

    @Getter
    private final String path;

    @Getter
    private final FileType type;

    Tab(final String path) {
        this.reader = new FileConfigReader(BetterSecurityBukkit.getInstance(), path, FileType.TAB);
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
