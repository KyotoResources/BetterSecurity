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

import it.zs0bye.bettersecurity.common.modules.methods.Method;
import it.zs0bye.bettersecurity.common.modules.methods.MethodType;
import it.zs0bye.bettersecurity.common.modules.tabcomplete.TabMerge;
import it.zs0bye.bettersecurity.common.modules.tabcomplete.TabChildren;
import lombok.Getter;

import java.util.*;
import java.util.function.Consumer;

@Getter
public abstract class TabProviders {

    private final Set<String> whitelisted = new HashSet<>();

    private final Set<String> blacklisted = new HashSet<>();

    public Set<String> legacy(final MethodType methodType, final String completion, final Set<String> whitelisted, final List<String> suggestions, final List<String> args, final Consumer<Boolean> cancelled) {
        String firstcompletion = completion.contains(" ") ? completion.split(" ")[0] : completion;
        firstcompletion = firstcompletion.replaceFirst("/", "");

        final Method method = new Method(methodType, suggestions, firstcompletion);
        for(final String suggestion : suggestions) {

            if (methodType == MethodType.BLACKLIST && method.contains()) {
                args.clear();
                cancelled.accept(true);
            }

            if (!completion.startsWith("/")) break;
            if (!completion.contains(" ")) cancelled.accept(false);
            if (completion.startsWith("/" + this.getIdentifier(suggestion)) || completion.contains(" ")) {
                if (!(methodType == MethodType.WHITELIST && method.contains())) continue;
                cancelled.accept(false);
                break;
            }

            this.result(true, method, suggestion, whitelisted, this.blacklisted);
        }

        return whitelisted;
    }

    public Set<String> result(final Method method, final List<String> suggestions, final Set<String> whitelisted) {
        suggestions.forEach(suggestion -> this.result(false, method, this.getIdentifier(suggestion), whitelisted, this.blacklisted));
        return whitelisted;
    }

    protected void result(final boolean legacy, final Method method, String suggestion, final Set<String> whitelisted, final Set<String> blacklisted) {
        final String save_suggestion = suggestion;

        suggestion = this.getIdentifier(suggestion);
        method.setCommand(suggestion);
        suggestion = legacy ? "/" + suggestion : suggestion;
        if(!method.contains()) return;
        if (method.getType() == MethodType.BLACKLIST && method.contains() || whitelisted.contains(suggestion)) {
            if(!whitelisted.contains(suggestion)) blacklisted.add(suggestion);
            if(save_suggestion.contains(" ")) return;
            whitelisted.remove(suggestion);
            return;
        }
        blacklisted.remove(suggestion);
        whitelisted.add(suggestion);
    }

    protected void merge(final Set<String> whitelisted, final String completion, final List<String> args, final Consumer<Boolean> cancelled) {
        for(final TabMerge merge : TabMerge.values()) whitelisted.addAll(merge.provider(this).inject(whitelisted, completion, args, cancelled));
    }

    protected void merge(final List<String> suggestions, final Set<String> whitelisted) {
        for(final TabMerge merge : TabMerge.values()) whitelisted.addAll(merge.provider(this).inject(whitelisted, suggestions));
    }

    protected void childrens(final MethodType methodType, final String completion, final List<String> suggestions, final List<String> args, final Consumer<Boolean> cancelled) {
        final Method method = new Method(methodType, suggestions, null);
        final List<TabChildren> childrens = TabChildren.getChildrens(method, completion, suggestions, cancelled);
        childrens.forEach(children -> {
            method.setCommand(children.getIdentifier());
            children.addChildrens(args);
        });
    }

    private String getIdentifier(final String suggestion) {
        return suggestion.contains(" ") ? suggestion.split(" ")[0] : suggestion;
    }

}
