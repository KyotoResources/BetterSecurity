/*
 * Security plugin for your server - https://github.com/KyotoResources/BetterSecurity
 * Copyright (c) 2023 KyotoResources
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
import it.zs0bye.bettersecurity.bukkit.files.readers.Config;
import it.zs0bye.bettersecurity.bukkit.utils.StringUtils;
import it.zs0bye.bettersecurity.common.BetterUser;
import it.zs0bye.bettersecurity.common.config.ConfigReader;
import it.zs0bye.bettersecurity.common.utils.CStringUtils;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

@Getter
public class FileConfigReader implements ConfigReader {

    private final BetterSecurityBukkit plugin;
    private final String path;
    private final FileType type;
    private FileConfiguration config;

    public FileConfigReader(final BetterSecurityBukkit plugin, final String path, final FileType type) {
        this.plugin = plugin;
        this.path = path;
        this.type = type;
        this.reloadConfig();
    }


    @Override
    public String variables(final String... var) {
        final StringBuilder builder = new StringBuilder();
        for(String texts : var) builder.append(texts);
        builder.append(path);
        return builder.toString();
    }

    @Override
    public void reloadConfig() {
        this.config = this.plugin.getConfig(this.type);
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public Object getObject(String... var) {
        return this.config.get(this.variables(var));
    }

    @Override
    public String getString(final String... var) {
        return StringUtils.colorize(this.config.getString(this.variables(var)));
    }

    @Override
    public List<String> getStringList(final String... var) {
        List<String> list = new ArrayList<>();
        for (String setList : this.config.getStringList(this.variables(var))) {
            list.add(StringUtils.colorize(setList));
        }
        return list;
    }

    @Override
    public boolean getBoolean(final String... var) {
        return this.config.getBoolean(this.variables(var));
    }

    @Override
    public boolean contains(final String... var) {
        return this.config.contains(this.variables(var));
    }

    @Override
    public int getInt(final String... var) {
        return this.config.getInt(this.variables(var));
    }

    @Override
    public String getCustomString(String replace) {
        if (replace.startsWith("%prefix%")) {
            replace = replace.replace("%prefix%", Config.SETTINGS_PREFIX.getString());
            if (replace.startsWith(Config.SETTINGS_PREFIX.getString() + "%center%")) {
                replace = replace.replace("%center%", "");
                return CStringUtils.center(replace);
            }
            return replace;
        }

        if(replace.startsWith("%center%")) {
            replace = replace.replace("%center%", "");
            return CStringUtils.center(replace);
        }
        return replace;
    }

    @Override
    public String getCustomString(final String... var) {
        return this.getCustomString(this.getString(var));
    }

    @Override
    public void send(final BetterUser user, final String... var) {
        this.send(user, new HashMap<>(), var);
    }

    @Override
    public void send(final BetterUser user, final Map<String, String> placeholders, final String... var) {
        String message = this.getCustomString(this.getString(var));
        if (message.isEmpty()) return;
        for (String key : placeholders.keySet()) message = message.replace(key, placeholders.get(key));
        ((CommandSender) user.getSender()).sendMessage(message);
    }

    @Override
    public void sendList(final BetterUser user, final String... var) {
        this.sendList(user, new HashMap<>(), var);
    }

    @Override
    public void sendList(final BetterUser user, final Map<String, String> placeholders, final String... var) {
        if (this.getStringList(var).isEmpty()) return;
        this.getStringList(var).forEach(msg -> {
            for (String key : placeholders.keySet()) msg = msg.replace(key, placeholders.get(key));
            ((CommandSender) user.getSender()).sendMessage(this.getCustomString(msg));
        });
    }

    @SuppressWarnings("all")
    @Override
    public Set<String> getSection(final String... var) {
        return this.config.getConfigurationSection(this.variables(var)).getKeys( false);
    }

}
