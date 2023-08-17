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

package it.zs0bye.bettersecurity.bungee.modules.tabcomplete.modes.advanced;

import it.zs0bye.bettersecurity.bungee.BetterSecurityBungee;
import it.zs0bye.bettersecurity.bungee.files.readers.Tab;
import it.zs0bye.bettersecurity.bungee.modules.tabcomplete.TabHandler;
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
    private final TabHandler tab;
    private final String server;
    private final String path;
    private final String methodPath;

    public TabGroup(final BetterSecurityBungee plugin, final String name, final ProxiedPlayer player, final TabHandler tab) {
        this.plugin = plugin;
        this.name = name;
        this.player = player;
        this.tab = tab;
        this.server = player.getServer().getInfo().getName();
        this.path = Tab.ADV_MODE_GROUPS.getPath() + "." + this.name;
        this.methodPath = this.path + Tab.ADV_MODE_GROUPS_METHOD.getPath();
    }

    private boolean allStartWithExclamation(final List<String> list) {
        final List<String> contains = new ArrayList<>();
        for(final String value : list) {
            if(!value.startsWith("!")) continue;
            contains.add(value);
        }
        return contains.size() == list.size();
    }

    private String getStartWithServer(final List<String> list) {
        String target = "bs-disable:";
        for(final String value : list) {
            if(!value.startsWith("#") && !value.startsWith("!#")) continue;
            target = value.split("#")[1];
        }
        return target;
    }

    protected String getPermission() {
        final String path = this.path + Tab.ADV_MODE_GROUPS_REQUIRED_PERMISSION.getPath();
        if(!Tab.INSTANCE.contains(path)) return "";
        return Tab.INSTANCE.getString(path);
    }

    protected int getPriority() {
        final String path = this.path + Tab.ADV_MODE_GROUPS_PRIORITY.getPath();
        if(!Tab.INSTANCE.contains(path)) return 0;
        final String number = String.valueOf(Tab.INSTANCE.getObject(path));
        if(!NumberUtils.isDigits(number)) {
            this.plugin.getLogger().warning("The priority (" + number + ") you set to group \"" + this.name + "\" does not have a valid number!");
            return 0;
        }
        return Tab.INSTANCE.getInt(path);
    }

    protected List<String> getSuggestions() {
        final String path = this.path + Tab.ADV_MODE_GROUPS_LIST.getPath();
        final List<String> suggestions = new ArrayList<>();
        if(!Tab.INSTANCE.contains(path)) {
            this.plugin.getLogger().warning("The suggestions of group \"" + this.name + "\", have not been set! The list can't be empty, so add suggestions to it.");
            return suggestions;
        }
        if(this.hasNotRequiredPermission() || this.hasNotRequiredProperty() || this.hasNotTargetServers()) return suggestions;
        suggestions.addAll(Tab.INSTANCE.getStringList(path));
        return suggestions;
    }

    private boolean hasNotTargetServers() {
        final String path = this.path + Tab.ADV_MODE_GROUPS_TARGET_SERVERS.getPath();
        if(!Tab.INSTANCE.contains(path)) return false;
        final List<String> servers = Tab.INSTANCE.getStringList(path);
        final String swserver = this.getStartWithServer(servers);

        if(servers.contains("#" + swserver)) return !this.server.startsWith(swserver);
        if(servers.contains("!#" + swserver)) return this.server.startsWith(swserver);

        if(servers.contains("!" + this.server.toLowerCase())) return true;
        if(this.allStartWithExclamation(servers)) return false;
        return !(servers.contains("*") || servers.contains(this.server.toLowerCase()));
    }

    private boolean hasNotRequiredPermission() {
        if(this.getPermission().isEmpty()) return false;
        return !this.player.hasPermission(this.getPermission());
    }

    private boolean hasNotRequiredProperty() {
        final String path = this.path + Tab.ADV_MODE_GROUPS_REQUIRED_PROPERTY.getPath();
        if(!Tab.INSTANCE.contains(path)) return false;
        final List<String> properties = Tab.INSTANCE.getStringList(path);
        String groupName = "g:bs-notperm";
        for(final String property : properties) {
            if(!property.startsWith("g:")) continue;
            final String[] group = property.split("g:");
            if(group.length != 2) {
                this.plugin.getLogger().warning("In the properties list of the \"" + this.name + "\" group, there is an unnamed group. It must be set as in the example: \"g:owner\", or \"g:default\", etc..");
                break;
            }
            if(!this.player.hasPermission("group." + group[1])) continue;
            groupName = "g:" + group[1];
        }

        return !properties.contains(this.player.getName())
                && !properties.contains(this.player.getUniqueId().toString())
                && !properties.contains(groupName);
    }

}
