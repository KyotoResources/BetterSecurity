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

import com.mojang.brigadier.tree.CommandNode;
import it.zs0bye.bettersecurity.bungee.BetterSecurityBungee;
import it.zs0bye.bettersecurity.bungee.files.readers.Tab;
import it.zs0bye.bettersecurity.bungee.modules.tabcomplete.groups.TabGroupsMode;
import it.zs0bye.bettersecurity.common.methods.Method;
import it.zs0bye.bettersecurity.common.methods.MethodType;
import it.zs0bye.bettersecurity.common.utils.CStringUtils;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Cancellable;

import java.util.*;

public class TabHandler extends TabProviders {

    private final BetterSecurityBungee plugin;
    private final ProxiedPlayer player;

    private final boolean bypass_enabled;
    private final String bypass_method;
    private final List<String> bypass_players;
    private final List<String> suggestions;

    private final TabGroupsMode groupsMode;

    public TabHandler(final BetterSecurityBungee plugin, final ProxiedPlayer player) {
        this.plugin = plugin;
        this.player = player;
        this.bypass_enabled = Tab.GLOBAL_BYPASS_ENABLED.getBoolean();
        this.bypass_method = Tab.GLOBAL_BYPASS_METHOD.getString();
        this.bypass_players = Tab.GLOBAL_BYPASS_PLAYERS.getStringList();
        this.suggestions = Tab.SUGGESTIONS.getStringList();
        this.groupsMode = new TabGroupsMode(this.player, this);
    }

    public MethodType getMethodType(final String path, String group, String permission) {
        group = group.isEmpty() ? group : "." + group;
        permission = permission.isEmpty() ? "bettersecurity.tab" + group : permission;
        for(final MethodType type : MethodType.values()) {
            if(!this.player.hasPermission(permission + "." + type.getName())) continue;
            return type;
        }
        final String configType = Tab.INSTANCE.getString(path).toUpperCase();
        if (!MethodType.getTypes().contains(configType)) {
            this.plugin.getLogger().warning("There was a problem in this path \"" + path + "\"! It can only be either WHITELIST or BLACKLIST. By default it was set to WHITELIST.");
            return MethodType.WHITELIST;
        }
        final MethodType type = MethodType.valueOf(configType);
        if(this.player.hasPermission(permission + ".INVERT")) {
            if(type == MethodType.BLACKLIST) return MethodType.WHITELIST;
            return MethodType.BLACKLIST;
        }
        return type;
    }

    private MethodType getMethodType() {
        return this.getMethodType(Tab.METHOD.getPath(), "", "");
    }

    private boolean bypass() {
        if (!this.bypass_enabled) return false;
        if (this.bypass_method.equals("PERMISSION")) return this.player.hasPermission("bettersecurity.bypass.tab");
        if (this.bypass_method.equals("PLAYERS")) return this.bypass_players.contains(this.player.getName())
                || this.bypass_players.contains(this.player.getUniqueId().toString());
        return false;
    }

    public void applyTabLegacy(final List<String> commands, final String completion, final Cancellable cancelled) {
        if(this.bypass()) return;
        if (Tab.GROUPS_MODE_ENABLED.getBoolean()) {
            this.groupsMode.initTabLegacy(commands, completion, cancelled);
            return;
        }
        this.initTabLegacy(commands, completion, cancelled);
    }

    public void applyTabWaterfall(final Set<String> suggestions) {
        if(this.bypass()) return;
        if (Tab.GROUPS_MODE_ENABLED.getBoolean()) {
            this.groupsMode.initTabWaterfall(suggestions);
            return;
        }
        this.initTabWaterfall(suggestions);
    }

    public void applyTabChildren(final Collection<CommandNode<?>> childrens) {
        if(this.bypass()) return;
        if (Tab.GROUPS_MODE_ENABLED.getBoolean()) {
            this.groupsMode.initTabChildren(childrens);
            return;
        }
        this.initTabChildren(childrens);
    }

    private void initTabLegacy(final List<String> commands, final String completion, final Cancellable cancelled) {

        cancelled.setCancelled(true);
        final Set<String> suggestions = this.legacy(this.getMethodType(), completion, new HashSet<>(), this.suggestions, commands, cancelled);

        commands.addAll(Tab.PARTIAL_MATCHES.getBoolean() ?
                CStringUtils.copyPartialMatches(completion, suggestions) :
                suggestions);

        if(completion.contains(" ") || !suggestions.isEmpty()) return;
        commands.clear();
    }

    private void initTabWaterfall(final Set<String> suggestions) {

        final Set<String> newsuggestions = this.childrens(new Method(this.getMethodType(), this.suggestions, null), new HashSet<>(), new ArrayList<>(suggestions));

        final Iterator<String> iterator = suggestions.iterator();
        while (iterator.hasNext()) {
            final String command = iterator.next();
            if(!newsuggestions.isEmpty() && newsuggestions.contains(command)) continue;
            iterator.remove();
        }
    }

    private void initTabChildren(final Collection<CommandNode<?>> childrens) {

        final List<String> childrensName = new ArrayList<>();
        for(final CommandNode<?> node : childrens) childrensName.add(node.getName());
        final Set<String> suggestions = this.childrens(new Method(this.getMethodType(), this.suggestions, null), new HashSet<>(), childrensName);

        childrens.removeIf(node -> {
            for(final String suggestion : suggestions) {
                if(node.getName().equalsIgnoreCase(suggestion)) return false;
            }
            return true;
        });
    }

}
