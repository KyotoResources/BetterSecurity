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
import it.zs0bye.bettersecurity.common.methods.Method;
import it.zs0bye.bettersecurity.common.methods.MethodType;
import it.zs0bye.bettersecurity.common.modules.tabcomplete.TabHandler;
import it.zs0bye.bettersecurity.common.modules.tabcomplete.providers.SuggestionProvider;
import it.zs0bye.bettersecurity.common.modules.tabcomplete.providers.TabProviders;
import it.zs0bye.bettersecurity.common.utils.CStringUtils;

import java.util.*;
import java.util.function.Consumer;

public class BasicMode extends TabProviders implements SuggestionProvider {

    private final TabHandler handler;
    private final List<String> suggestions;

    public BasicMode(final TabHandler handler) {
        this.handler = handler;
        this.suggestions = this.handler.reader("BASIC_MODE_LIST").getStringList();
    }

    private MethodType getMethodType() {
        return this.handler.getMethodType(this.handler.reader("BASIC_MODE_METHOD").getPath(), "", "");
    }

    @Override
    public void addSuggestions(final List<String> commands, final String completion, final Consumer<Boolean> cancelled) {

        cancelled.accept(true);
        final Set<String> suggestions = this.legacy(this.getMethodType(), completion, new HashSet<>(), this.suggestions, commands, cancelled);

        commands.addAll(this.handler.reader("PARTIAL_MATCHES").getBoolean() ?
                CStringUtils.copyPartialMatches(completion, suggestions) :
                suggestions);

        if(completion.contains(" ") || !suggestions.isEmpty()) return;
        commands.clear();
    }

    @Override
    public void addSuggestions(final Set<String> suggestions) {

        final Set<String> newsuggestions = this.childrens(new Method(this.getMethodType(), this.suggestions, null), new HashSet<>(), new ArrayList<>(suggestions));

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
        final Set<String> suggestions = this.childrens(new Method(this.getMethodType(), this.suggestions, null), new HashSet<>(), childrensName);

        childrens.removeIf(node -> {
            for(final String suggestion : suggestions) {
                if(node.getName().equalsIgnoreCase(suggestion)) return false;
            }
            return true;
        });
    }

}
