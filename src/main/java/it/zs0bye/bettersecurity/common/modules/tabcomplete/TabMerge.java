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

import it.zs0bye.bettersecurity.common.methods.Method;
import it.zs0bye.bettersecurity.common.methods.MethodType;
import it.zs0bye.bettersecurity.common.modules.tabcomplete.providers.TabProviders;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public enum TabMerge {
    WHITELISTED(MethodType.WHITELIST),
    BLACKLISTED(MethodType.BLACKLIST);

    private final Set<String> suggestions = new HashSet<>();
    private final MethodType methodType;
    private TabProviders provider;

    TabMerge(final MethodType methodType) {
        this.methodType = methodType;
    }

    public void add(final List<String> suggestions) {
        this.suggestions.addAll(new HashSet<>(suggestions));
    }

    public Set<String> get() {
        return this.suggestions;
    }

    public TabMerge provider(final TabProviders provider) {
        this.provider = provider;
        return this;
    }

    public Set<String> inject(final Set<String> whitelisted, final String completion, final List<String> commands, final Consumer<Boolean> cancelled) {
        if(this.provider == null || this.get().isEmpty()) return new HashSet<>();
        this.provider.legacy(this.methodType, completion, whitelisted, new ArrayList<>(this.get()), commands, cancelled);
        return whitelisted;
    }

    public Set<String> inject(final Set<String> whitelisted, final List<String> suggestions) {
        if(this.provider == null || this.get().isEmpty()) return new HashSet<>();
        this.provider.childrens(new Method(this.methodType, new ArrayList<>(this.get()), null), suggestions, whitelisted);
        return whitelisted;
    }

}
