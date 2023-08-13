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

package it.zs0bye.bettersecurity.bungee.modules.tabcomplete;

import it.zs0bye.bettersecurity.bungee.BetterSecurityBungee;
import it.zs0bye.bettersecurity.bungee.files.readers.Tab;
import lombok.Getter;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TabGroup {

    private final BetterSecurityBungee plugin;
    private final String name;
    private final ProxiedPlayer player;
    private final TabComplete tab;
    private final String server;
    private final String path;
    private final String methodPath;

    public TabGroup(final BetterSecurityBungee plugin, final String name, final ProxiedPlayer player, final TabComplete tab) {
        this.plugin = plugin;
        this.name = name;
        this.player = player;
        this.tab = tab;
        this.server = player.getServer().getInfo().getName();
        this.path = Tab.GROUPS_MODE_GROUPS.getPath() + "." + this.name;
        this.methodPath = this.path + Tab.GROUPS_MODE_GROUPS_METHOD.getPath();
    }

    protected String getPermission() {
        final String path = this.path + Tab.GROUPS_MODE_GROUPS_REQUIRED_PERMISSION.getPath();
        if(!Tab.INSTANCE.contains(path)) return "";
        return Tab.INSTANCE.getString(path);
    }

    protected boolean getRequiredPermission() {
        if(this.getPermission().isEmpty()) return false;
        return !this.player.hasPermission(this.getPermission());
    }

    protected boolean getRequiredServers() {
        final String path = this.path + Tab.GROUPS_MODE_GROUPS_REQUIRED_SERVERS.getPath();
        if(!Tab.INSTANCE.contains(path)) return false;
        final List<String> servers = Tab.INSTANCE.getStringList(path);
        return !servers.contains(this.server.toLowerCase());
    }

    protected boolean getRequiredPlayers() {
        final String path = this.path + Tab.GROUPS_MODE_GROUPS_REQUIRED_PLAYERS.getPath();
        if(!Tab.INSTANCE.contains(path)) return false;
        final List<String> players = Tab.INSTANCE.getStringList(path);
        return !players.contains(this.player.getName()) && !players.contains(this.player.getUniqueId().toString());
    }

    protected boolean getIgnoreServers() {
        final String path = this.path + Tab.GROUPS_MODE_GROUPS_IGNORE_SERVERS.getPath();
        if(!Tab.INSTANCE.contains(path)) return false;
        final List<String> servers = Tab.INSTANCE.getStringList(path);
        return servers.contains(this.server.toLowerCase());
    }

    protected int getPriority() {
        final String path = this.path + Tab.GROUPS_MODE_GROUPS_PRIORITY.getPath();
        if(!Tab.INSTANCE.contains(path)) return 0;
        final String number = String.valueOf(Tab.INSTANCE.getObject(path));
        if(!NumberUtils.isDigits(number)) {
            this.plugin.getLogger().warning("The priority (" + number + ") you set to group \"" + this.name + "\" does not have a valid number!");
            return 0;
        }
        return Tab.INSTANCE.getInt(path);
    }

    protected List<String> getSuggestions() {
        final String path = this.path + Tab.GROUPS_MODE_GROUPS_SUGGESTIONS.getPath();
        final List<String> suggestions = new ArrayList<>();
        if(!Tab.INSTANCE.contains(path)) {
            this.plugin.getLogger().warning("The suggestions of group \"" + this.name + "\", have not been set! The list can't be empty, so add suggestions to it.");
            return suggestions;
        }
        if(this.getRequiredPermission() || this.getRequiredPlayers() || this.getRequiredServers() || this.getIgnoreServers()) return suggestions;
        suggestions.addAll(Tab.INSTANCE.getStringList(path));
        return suggestions;
    }

}
