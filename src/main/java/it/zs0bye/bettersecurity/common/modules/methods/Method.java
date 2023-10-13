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

package it.zs0bye.bettersecurity.common.modules.methods;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class Method {

    private MethodType type;
    private List<String> suggestions;
    private String command;

    public boolean contains() {
        return this.getIdentifiers().contains(this.command);
    }

    public List<String> getIdentifiers() {
        final List<String> identifiers = new ArrayList<>();
        this.suggestions.forEach(suggestion -> identifiers.add(suggestion.contains(" ") ? suggestion.split(" ")[0] : suggestion));
        return identifiers;
    }

}
