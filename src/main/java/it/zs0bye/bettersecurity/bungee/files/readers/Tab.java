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

import it.zs0bye.bettersecurity.bungee.files.FileHandler;
import it.zs0bye.bettersecurity.bungee.files.ConfigReader;
import it.zs0bye.bettersecurity.bungee.files.FileType;
import it.zs0bye.bettersecurity.bungee.modules.Module;
import it.zs0bye.bettersecurity.bungee.utils.StringUtils;
import it.zs0bye.bettersecurity.common.utils.CStringUtils;
import lombok.Getter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.config.Configuration;

import java.util.*;

public enum Tab implements ConfigReader {
    INSTANCE(""),
    IGNORE_WATERFALL_WARNING("ignore_waterfall_warning"),
    IGNORE_1_13_TAB_COMPLETE("ignore_1_13_tab_complete"),
    PARTIAL_MATCHES("partial_matches"),
    GLOBAL_BYPASS_ENABLED("global_bypass.enabled"),
    GLOBAL_BYPASS_METHOD("global_bypass.method"),
    GLOBAL_BYPASS_PLAYERS("global_bypass.players"),
    SUGGESTIONS("suggestions"),
    METHOD("method"),
    GROUPS_MODE_ENABLED("groups_mode.enabled"),
    GROUPS_MODE_ENABLED_GROUPS("groups_mode.enabled_groups"),
    GROUPS_MODE_GROUPS("groups_mode.groups"),
    GROUPS_MODE_GROUPS_PRIORITY(".priority"),
    GROUPS_MODE_GROUPS_METHOD(".method"),
    GROUPS_MODE_GROUPS_SUGGESTIONS(".suggestions"),
    GROUPS_MODE_GROUPS_REQUIRED_SERVERS(".required_servers"),
    GROUPS_MODE_GROUPS_REQUIRED_PERMISSION(".required_permission"),
    GROUPS_MODE_GROUPS_REQUIRED_PLAYERS(".required_players"),
    GROUPS_MODE_GROUPS_IGNORE_SERVERS(".ignore_servers");

    @Getter
    private final String path;
    @Getter
    private final FileType type;
    private Configuration config;

    Tab(final String path) {
        this.path = path;
        this.type = Module.TAB_COMPLETE.getType();
        this.reloadConfig();
    }

    @Override
    public String variables(final String... var) {
        StringBuilder builder = new StringBuilder();
        for(String texts : var) builder.append(texts);
        builder.append(path);
        return builder.toString();
    }

    @Override
    public void reloadConfig() {
        final Configuration config = FileHandler.getConfig(this.type);
        if(config == null) return;
        this.config = config;
    }


    public Object getObject(final String... var) {
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
    public String getCustomString(String replace, final String... var) {
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
        return this.getCustomString(this.getString(var), var);
    }

    @Override
    public void send(final CommandSender sender, final String... var) {
        this.send(sender, new HashMap<>(), var);
    }

    @Override
    public void send(final CommandSender sender, final Map<String, String> placeholders, final String... var) {
        String message = this.getCustomString(this.getString(var), var);
        if (message.isEmpty()) return;
        for (String key : placeholders.keySet()) message = message.replace(key, placeholders.get(key));
        StringUtils.send(sender, message);
    }

    @Override
    public void sendList(final CommandSender sender, final String... var) {
        this.sendList(sender, new HashMap<>(), var);
    }

    @Override
    public void sendList(final CommandSender sender, final Map<String, String> placeholders, final String... var) {
        if (this.getStringList(var).isEmpty()) return;
        this.getStringList(var).forEach(msg -> {
            for (String key : placeholders.keySet()) msg = msg.replace(key, placeholders.get(key));
            sender.sendMessage(TextComponent.fromLegacyText(this.getCustomString(msg, var)));
        });
    }

    @Override
    public Collection<String> getSection(final String... var) {
        return this.config.getSection(this.variables(var)).getKeys();
    }

}
