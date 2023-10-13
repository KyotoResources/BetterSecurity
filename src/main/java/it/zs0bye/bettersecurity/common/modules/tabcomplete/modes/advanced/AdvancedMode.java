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

package it.zs0bye.bettersecurity.common.modules.tabcomplete.modes.advanced;

import com.mojang.brigadier.tree.CommandNode;
import it.zs0bye.bettersecurity.common.BetterUser;
import it.zs0bye.bettersecurity.common.SoftwareType;
import it.zs0bye.bettersecurity.common.modules.methods.Method;
import it.zs0bye.bettersecurity.common.modules.methods.MethodType;
import it.zs0bye.bettersecurity.common.modules.tabcomplete.TabHandler;
import it.zs0bye.bettersecurity.common.modules.tabcomplete.providers.SuggestionProvider;
import it.zs0bye.bettersecurity.common.modules.tabcomplete.providers.TabProviders;
import it.zs0bye.bettersecurity.common.utils.CStringUtils;
import lombok.Getter;

import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Logger;

@Getter
public class AdvancedMode extends TabProviders implements SuggestionProvider {

    private final String name;
    private final Logger logger;
    private final TabHandler handler;
    private final BetterUser user;
    private final SoftwareType softwareType;

    public AdvancedMode(final TabHandler handler, final Logger logger, final BetterUser user, final SoftwareType type) {
        this.handler = handler;
        this.logger = logger;
        this.user = user;
        this.softwareType = type;
        this.name = "AdvancedMode";
    }

    private MethodType getMethodType(final TabGroup group) {
        return MethodType.INSTANCE
                .setGroup(group.getName())
                .setPermission(group.getPermission())
                .getTypeByPath(this.handler, group.getMethodPath());
    }

    private List<TabGroup> groups() {
        final List<TabGroup> groups = new ArrayList<>();
        final Map<Integer, TabGroup> map = new HashMap<>();
        for(final String egroup : this.enabledGroups()) {
            final TabGroup group = new TabGroup(this.logger, egroup, this.user, new TabHandler(this.logger, this.user, this.handler.getReader(), this.softwareType));
            if(this.hasDuplications(group, map)) break;
            map.put(group.getPriority(), group);
        }
        for(final Integer priority : map.keySet()) groups.add(map.get(priority));
        Collections.reverse(groups);
        return groups;
    }

    private Collection<String> enabledGroups() {
        final Collection<String> groups = new HashSet<>();
        final List<String> enabled_groups = this.handler.reader("ADV_MODE_ENABLED_GROUPS").getStringList();
        if(enabled_groups.contains("*")) {
            groups.addAll(this.handler.reader("ADV_MODE_GROUPS").getSection());
            return groups;
        }
        groups.addAll(enabled_groups);
        return groups;
    }

    private boolean hasDuplications(final TabGroup group, final Map<Integer, TabGroup> map) {
        final int priority = group.getPriority();
        if(!map.containsKey(priority)) return false;
        this.logger.severe("Group \"" + group.getName() + "\" contains a duplicate priority (" + priority + "). Please set a different priority for all groups.");
        return true;
    }

    @Override
    public void addSuggestions() {
        this.groups().forEach(group -> this.result(new Method(this.getMethodType(group), group.getSuggestions(), null), group.getSuggestions(), this.getWhitelisted()));
    }

    @Override
    public void addSuggestions(final List<String> commands, final String completion, final Consumer<Boolean> cancelled) {

        final Set<String> suggestions = new HashSet<>();
        cancelled.accept(true);

        this.groups().forEach(group -> suggestions.addAll(this.legacy(
                this.getMethodType(group),
                completion,
                suggestions,
                group.getSuggestions(),
                commands,
                cancelled)));

        this.merge(suggestions, completion, commands, cancelled);
        if(!suggestions.isEmpty()) {
            this.getWhitelisted().addAll(suggestions);
            commands.addAll(this.handler.reader("PARTIAL_MATCHES").getBoolean() ?
                    CStringUtils.copyPartialMatches(completion, suggestions) :
                    suggestions);
            return;
        }

        if(completion.contains(" ")) return;
        commands.clear();
        cancelled.accept(true);
    }

    @Override
    public void addSuggestions(final Set<String> suggestions) {
        final Set<String> newsuggestions = new HashSet<>();

        this.groups().forEach(group -> newsuggestions.addAll(this.result(
                new Method(this.getMethodType(group), group.getSuggestions(), null),
                new ArrayList<>(suggestions), newsuggestions)));
        this.merge(new ArrayList<>(suggestions), newsuggestions);
        this.getWhitelisted().addAll(newsuggestions);

        final Iterator<String> iterator = suggestions.iterator();
        while (iterator.hasNext()) {
            final String command = iterator.next();
            if(!newsuggestions.isEmpty() && newsuggestions.contains(command)) continue;
            iterator.remove();
        }
    }

    @Override
    public void addSuggestions(final Collection<CommandNode<?>> childrens) {
        final Set<String> suggestions = new HashSet<>();

        final List<String> childrensName = new ArrayList<>();
        for (final CommandNode<?> node : childrens) childrensName.add(node.getName());
        this.groups().forEach(group -> suggestions.addAll(this.result(
                new Method(this.getMethodType(group), group.getSuggestions(), null),
                childrensName, suggestions)));
        this.merge(childrensName, suggestions);
        this.getWhitelisted().addAll(suggestions);

        childrens.removeIf(node -> {
            for (final String suggestion : suggestions) {
                if (node.getName().equalsIgnoreCase(suggestion)) return false;
            }
            return true;
        });
    }

    @Override
    public void addChildrens(String completion, List<String> childrens, final Consumer<Boolean> cancelled) {
        this.groups().forEach(group -> this.childrens(this.getMethodType(group), completion, group.getSuggestions(), childrens, cancelled));
    }

}
