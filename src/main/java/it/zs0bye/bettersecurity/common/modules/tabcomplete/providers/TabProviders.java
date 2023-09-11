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

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public abstract class TabProviders {

    protected Set<String> legacy(final MethodType methodType, final String completion, final Set<String> newsuggestions, final List<String> oldsuggestions, final List<String> commands, Consumer<Boolean> cancelled) {
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

            this.childrens(true, method, newsuggestions, suggestion);
        }

        return newsuggestions;
    }

    protected Set<String> childrens(final Method method, final Set<String> newsuggestions, final List<String> oldsuggestions) {
        for (String suggestion : oldsuggestions) {
            this.childrens(false, method, newsuggestions, suggestion);
        }
        return newsuggestions;
    }

    protected void childrens(final boolean legacy, final Method method, final Set<String> newsuggestions, String suggestion) {
        method.setCommand(suggestion);
        suggestion = legacy ? "/" + suggestion : suggestion;
        if(!method.contains()) return;
        if (method.getType() == MethodType.BLACKLIST && method.contains() || newsuggestions.contains(suggestion)) {
            newsuggestions.remove(suggestion);
            return;
        }
        newsuggestions.add(suggestion);
    }

}
