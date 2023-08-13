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
import it.zs0bye.bettersecurity.common.methods.Method;
import it.zs0bye.bettersecurity.common.methods.MethodType;
import it.zs0bye.bettersecurity.common.utils.CStringUtils;
import lombok.Getter;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Cancellable;

import java.util.*;

@Getter
public class TabGroupsMode extends TabProviders {

    private final ProxiedPlayer player;
    private final TabComplete tab;

    public TabGroupsMode(final ProxiedPlayer player,  final TabComplete tab) {
        this.player = player;
        this.tab = tab;
    }

    private MethodType getMethodType(final TabGroup group) {
        return this.tab.getMethodType(group.getMethodPath(), group.getName(), group.getPermission());
    }

    public void initTabLegacy(final List<String> commands, final String completion, final Cancellable cancelled) {

        final Set<String> suggestions = new HashSet<>();
        cancelled.setCancelled(true);

        TabGroupsMode.groups(player).forEach(group -> suggestions.addAll(this.legacy(this.getMethodType(group), completion, suggestions, group.getSuggestions(), commands, cancelled)));

        if(!suggestions.isEmpty()) {
            commands.addAll(Tab.PARTIAL_MATCHES.getBoolean() ?
                    CStringUtils.copyPartialMatches(completion, suggestions) :
                    suggestions);
            return;
        }

        if(completion.contains(" ")) return;
        commands.clear();
        cancelled.setCancelled(true);
    }

    public void initTabWaterfall(final Set<String> suggestions) {
        final Set<String> newsuggestions = new HashSet<>();

        TabGroupsMode.groups(player).forEach(group -> newsuggestions.addAll(this.childrens(new Method(this.getMethodType(group), group.getSuggestions(), null),
                newsuggestions,
                new ArrayList<>(suggestions))));

        final Iterator<String> iterator = suggestions.iterator();
        while (iterator.hasNext()) {
            final String command = iterator.next();
            if(!newsuggestions.isEmpty() && newsuggestions.contains(command)) continue;
            iterator.remove();
        }
    }

    public void initTabChildren(final Collection<CommandNode<?>> childrens) {
        final Set<String> suggestions = new HashSet<>();

        final List<String> childrensName = new ArrayList<>();
        for(final CommandNode<?> node : childrens) childrensName.add(node.getName());
        TabGroupsMode.groups(player).forEach(group -> suggestions.addAll(this.childrens(new Method(this.getMethodType(group), group.getSuggestions(), null), suggestions, childrensName)));

        childrens.removeIf(node -> {
            for(final String suggestion : suggestions) {
                if(node.getName().equalsIgnoreCase(suggestion)) return false;
            }
            return true;
        });
    }

    private static Collection<String> enabledGroups() {
        final Collection<String> groups = new HashSet<>();
        final List<String> enabled_groups = Tab.GROUPS_MODE_ENABLED_GROUPS.getStringList();
        if(enabled_groups.contains("*")) {
            groups.addAll(Tab.GROUPS_MODE_GROUPS.getSection());
            return groups;
        }
        groups.addAll(enabled_groups);
        return groups;
    }

    public static List<TabGroup> groups(final ProxiedPlayer player) {
        final BetterSecurityBungee plugin = BetterSecurityBungee.getInstance();
        final List<TabGroup> groups = new ArrayList<>();
        final Map<Integer, TabGroup> map = new HashMap<>();
        for(final String egroup : enabledGroups()) {
            final TabGroup group = new TabGroup(plugin, egroup, player, new TabComplete(plugin, player));
            if(hasDuplications(plugin, group, map)) break;
            map.put(group.getPriority(), group);
        }
        for(final Integer priority : map.keySet()) groups.add(map.get(priority));
        Collections.reverse(groups);
        return groups;
    }

    private static boolean hasDuplications(final BetterSecurityBungee plugin, final TabGroup group, final Map<Integer, TabGroup> map) {
        final int priority = group.getPriority();
        if(!map.containsKey(priority)) return false;
        plugin.getLogger().severe("Group \"" + group.getName() + "\" contains a duplicate priority (" + priority + "). Please set a different priority for all groups.");
        return true;
    }

}
