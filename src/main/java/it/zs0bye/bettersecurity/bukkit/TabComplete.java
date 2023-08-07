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

package it.zs0bye.bettersecurity.bukkit;

import it.zs0bye.bettersecurity.bukkit.files.enums.Config;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class TabComplete {

    private final String bypass_method;
    private final List<String> bypass_players;

    private Player player;
    private Permission permission;

    public TabComplete() {
        this.bypass_method = Config.BLOCK_TAB_COMPLETE_BYPASS_METHOD.getString();
        this.bypass_players = Config.BLOCK_TAB_COMPLETE_BYPASS_PLAYERS.getStringList();
        this.initPermissions();
    }

    public TabComplete(final Player player) {
        this.player = player;
        this.bypass_method = Config.BLOCK_TAB_COMPLETE_BYPASS_METHOD.getString();
        this.bypass_players = Config.BLOCK_TAB_COMPLETE_BYPASS_PLAYERS.getStringList();
    }

    public boolean bypass() {
        if(this.bypass_method.equals("PERMISSION")) return player.hasPermission("bettersecurity.bypass.antitab");
        if(this.bypass_method.equals("PLAYERS")) return this.bypass_players.contains(player.getName())
                || this.bypass_players.contains(player.getUniqueId().toString());
        return false;
    }

    private void initPermissions() {
        if(this.enabledGroups().isEmpty()) return;
        this.enabledGroups().forEach(group -> {
            final String path = Config.BLOCK_TAB_COMPLETE_WHITELISTED_SUGGESTIONS_GROUPS.getPath() + "." + group;
            final String permissionPath = path + Config.BLOCK_TAB_COMPLETE_WHITELISTED_SUGGESTIONS_GROUPS_REQUIRED_PERMISSION.getPath();
            if(!Config.CUSTOM.contains(permissionPath)) return;
            this.permission = new Permission(Config.CUSTOM.getString(permissionPath), PermissionDefault.OP);
            this.permission.addParent("bettersecurity.tab.*", true);
        });
    }

    private boolean checkPermission(final String path) {
        if(!Config.CUSTOM.contains(path)) return false;
        if(this.permission == null) return !this.player.hasPermission(Config.CUSTOM.getString(path));
        return !this.player.hasPermission(this.permission);
    }

    private Collection<String> enabledGroups() {
        final Collection<String> groups = new ArrayList<>();
        final List<String> enabled_groups = Config.BLOCK_TAB_COMPLETE_WHITELISTED_SUGGESTIONS_ENABLED_GROUPS.getStringList();
        if(enabled_groups.contains("*")) {
            groups.addAll(Config.BLOCK_TAB_COMPLETE_WHITELISTED_SUGGESTIONS_GROUPS.getConfigurationSection());
            return groups;
        }
        groups.addAll(enabled_groups);
        return groups;
    }

    private List<String> groupsCompletions() {
        final List<String> completions = new ArrayList<>();
        if(this.enabledGroups().isEmpty()) return completions;
        for(final String group : this.enabledGroups()) {
            final String path = Config.BLOCK_TAB_COMPLETE_WHITELISTED_SUGGESTIONS_GROUPS.getPath() + "." + group;
            final String permissionPath = path + Config.BLOCK_TAB_COMPLETE_WHITELISTED_SUGGESTIONS_GROUPS_REQUIRED_PERMISSION.getPath();
            final String playersPath = path + Config.BLOCK_TAB_COMPLETE_WHITELISTED_SUGGESTIONS_GROUPS_REQUIRED_PLAYERS.getPath();
            if(this.checkPermission(permissionPath)) continue;
            if(Config.CUSTOM.contains(playersPath) && !Config.CUSTOM.getStringList(playersPath).contains(player.getName())) continue;
            completions.addAll(Config.CUSTOM.getStringList(path + Config.BLOCK_TAB_COMPLETE_WHITELISTED_SUGGESTIONS_GROUPS_SUGGESTIONS.getPath()));
        }

        return completions;
    }

    private List<String> initCompletions(final List<String> completions, final boolean legacy) {
        final List<String> newCompletions = new ArrayList<>();
        final String first = legacy ? "/" : "";
        for(final String command : completions) newCompletions.add(first + command);
        return newCompletions;
    }

    public List<String> getCompletions(final boolean legacy) {
        final List<String> completions = new ArrayList<>();
        if(!Config.BLOCK_TAB_COMPLETE_WHITELISTED_SUGGESTIONS_ENABLED.getBoolean()) return completions;
        completions.addAll(this.initCompletions(this.groupsCompletions(), legacy));
        return completions;
    }

}
