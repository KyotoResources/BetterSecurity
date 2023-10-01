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

package it.zs0bye.bettersecurity.common.modules.tabcomplete.providers;

import it.zs0bye.bettersecurity.common.methods.Method;
import it.zs0bye.bettersecurity.common.methods.MethodType;
import it.zs0bye.bettersecurity.common.modules.tabcomplete.TabMerge;
import lombok.Getter;

import java.util.*;
import java.util.function.Consumer;

@Getter
public abstract class TabProviders {

    private final Set<String> whitelisted = new HashSet<>();

    private final Set<String> blacklisted = new HashSet<>();

    public Set<String> legacy(final MethodType methodType, final String completion, final Set<String> whitelisted, final List<String> oldsuggestions, final List<String> commands, final Consumer<Boolean> cancelled) {
        String firstcompletion = completion.contains(" ") ? completion.split(" ")[0] : completion;
        firstcompletion = firstcompletion.replaceFirst("/", "");

        final Method method = new Method(methodType, oldsuggestions, firstcompletion);
        for(final String suggestion : oldsuggestions) {

            if (methodType == MethodType.BLACKLIST && method.contains()) {
                commands.clear();
                cancelled.accept(true);
            }

            if (!completion.startsWith("/")) break;
            if (!completion.contains(" ")) cancelled.accept(false);
            if (completion.startsWith("/" + suggestion) || completion.contains(" ")) {
                if (methodType == MethodType.WHITELIST && method.contains()) cancelled.accept(false);
                break;
            }

            this.childrens(true, method, suggestion, whitelisted, this.blacklisted);
        }

        return whitelisted;
    }

    public Set<String> childrens(final Method method, final List<String> suggestions, final Set<String> whitelisted) {
        suggestions.forEach(suggestion -> this.childrens(false, method, suggestion, whitelisted, this.blacklisted));
        return whitelisted;
    }

    protected void childrens(final boolean legacy, final Method method, String suggestion, final Set<String> whitelisted, final Set<String> blacklisted) {
        method.setCommand(suggestion);
        suggestion = legacy ? "/" + suggestion : suggestion;
        if(!method.contains()) return;
        if (method.getType() == MethodType.BLACKLIST && method.contains() || whitelisted.contains(suggestion)) {
            if(!whitelisted.contains(suggestion)) blacklisted.add(suggestion);
            whitelisted.remove(suggestion);
            return;
        }
        blacklisted.remove(suggestion);
        whitelisted.add(suggestion);
    }

    protected void merge(final Set<String> whitelisted, final String completion, final List<String> commands, final Consumer<Boolean> cancelled) {
        for(final TabMerge merge : TabMerge.values()) whitelisted.addAll(merge.provider(this).inject(whitelisted, completion, commands, cancelled));
    }

    protected void merge(final List<String> suggestions, final Set<String> whitelisted) {
        for(final TabMerge merge : TabMerge.values()) whitelisted.addAll(merge.provider(this).inject(whitelisted, suggestions));
    }

}
