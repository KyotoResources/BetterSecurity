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

package it.zs0bye.bettersecurity.common.modules.tabcomplete;

import it.zs0bye.bettersecurity.common.modules.methods.Method;
import it.zs0bye.bettersecurity.common.modules.methods.MethodType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

@Getter
public class TabChildren {

    private final Method method;
    private final String[] args, childrens;
    private final String completion, identifier;
    private final Consumer<Boolean> cancelled;

    public TabChildren(final Method method, final String completion, final String suggestion, final Consumer<Boolean> cancelled) {
        this.method = method;
        this.completion = completion;
        this.cancelled = cancelled;
        this.args = suggestion.split(" ");
        this.identifier = this.args[0];
        this.childrens = suggestion.split(this.identifier)[1].split(" ");
    }

    private boolean hasMethodBlacklist() {
        return method.getType() == MethodType.BLACKLIST && method.contains();
    }

    private List<String> getResultList(final int position) {
        final List<String> result = new ArrayList<>();
        final String children = this.childrens[position];
        if(children.equals("*")) {
            if(this.hasMethodBlacklist()) cancelled.accept(true);
            return result;
        }
        if(children.startsWith("%[") && children.endsWith("]")) {
            result.addAll(Arrays.asList(children.substring(2, children.length() - 1).split(",")));
            return result;
        }
        result.add(children);
        return result;
    }

    public void addChildrens(final List<String> childrens) {
        if(!this.completion.startsWith("/" +  this.identifier)) return;
        final long position = this.completion.chars().filter(c -> c == ' ').count();
        if(position >= this.childrens.length) return;
        cancelled.accept(false);
        this.setWithMethod(childrens, this.getResultList((int) position));
    }

    private void setWithMethod(final List<String> childrens, final List<String> result) {
        if(this.hasMethodBlacklist()) {
            childrens.removeAll(result);
            return;
        }
        if(!(method.getType() == MethodType.WHITELIST && method.contains())) return;
        childrens.addAll(result);
    }

    public static List<TabChildren> getChildrens(final Method method, final String completion, final List<String> suggestions, final Consumer<Boolean> cancelled) {
        final List<TabChildren> list = new ArrayList<>();
        suggestions.forEach(suggestion -> {
            if(!suggestion.contains(" ")) return;
            list.add(new TabChildren(method, completion, suggestion, cancelled));
        });
        return list;
    }

}
