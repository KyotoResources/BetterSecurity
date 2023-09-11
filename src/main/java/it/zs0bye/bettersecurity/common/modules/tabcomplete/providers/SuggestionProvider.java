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

import com.mojang.brigadier.tree.CommandNode;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public interface SuggestionProvider {

    void addSuggestions(final List<String> commands, final String completion, final Consumer<Boolean> cancelled);

    void addSuggestions(final Set<String> suggestions);

    void addSuggestions(final Collection<CommandNode<?>> childrens);

}
