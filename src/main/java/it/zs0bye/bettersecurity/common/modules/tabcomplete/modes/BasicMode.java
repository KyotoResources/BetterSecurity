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

package it.zs0bye.bettersecurity.common.modules.tabcomplete.modes;

import com.mojang.brigadier.tree.CommandNode;
import it.zs0bye.bettersecurity.common.modules.methods.Method;
import it.zs0bye.bettersecurity.common.modules.methods.MethodType;
import it.zs0bye.bettersecurity.common.modules.tabcomplete.TabHandler;
import it.zs0bye.bettersecurity.common.modules.tabcomplete.providers.SuggestionProvider;
import it.zs0bye.bettersecurity.common.modules.tabcomplete.providers.TabProviders;
import it.zs0bye.bettersecurity.common.utils.CStringUtils;
import lombok.Getter;

import java.util.*;
import java.util.function.Consumer;

public class BasicMode extends TabProviders implements SuggestionProvider {

    @Getter
    private final String name;
    private final TabHandler handler;
    private final List<String> suggestions;

    public BasicMode(final TabHandler handler) {
        this.handler = handler;
        this.name = "BasicMode";
        this.suggestions = this.handler.reader("BASIC_MODE_LIST").getStringList();
    }

    private MethodType getMethodType() {
        return MethodType.INSTANCE.getTypeByPath(this.handler, this.handler.reader("BASIC_MODE_METHOD").getPath());
    }

    @Override
    public void addSuggestions() {
        this.suggestions.forEach(suggestion -> this.result(false, new Method(this.getMethodType(), this.suggestions, null), suggestion, this.getWhitelisted(), this.getBlacklisted()));
    }

    @Override
    public void addSuggestions(final List<String> commands, final String completion, final Consumer<Boolean> cancelled) {

        cancelled.accept(true);
        Set<String> suggestions = this.legacy(this.getMethodType(), completion, new HashSet<>(), this.suggestions, commands, cancelled);
        this.merge(suggestions, completion, commands, cancelled);

        suggestions = this.handler.sort(suggestions);
        this.getWhitelisted().addAll(suggestions);
        commands.addAll(this.handler.reader("PARTIAL_MATCHES").getBoolean() ?
                CStringUtils.copyPartialMatches(completion, suggestions) :
                suggestions);

        if(completion.contains(" ") || !suggestions.isEmpty()) return;
        commands.clear();
    }

    @Override
    public void addSuggestions(final Set<String> suggestions) {

        final Set<String> newsuggestions = this.result(new Method(this.getMethodType(), this.suggestions, null), new ArrayList<>(suggestions), new HashSet<>());
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

        final List<String> childrensName = new ArrayList<>();
        for(final CommandNode<?> node : childrens) childrensName.add(node.getName());
        final Set<String> suggestions = this.result(new Method(this.getMethodType(), this.suggestions, null), childrensName, new HashSet<>());
        this.merge(childrensName, suggestions);

        this.getWhitelisted().addAll(suggestions);
        childrens.removeIf(node -> {
            for(final String suggestion : suggestions) {
                if(node.getName().equalsIgnoreCase(suggestion)) return false;
            }
            return true;
        });
    }

    @Override
    public void addChildrens(final String completion, final List<String> childrens, final Consumer<Boolean> cancelled) {
        this.childrens(this.getMethodType(), completion, this.suggestions, childrens, cancelled);
    }

}
