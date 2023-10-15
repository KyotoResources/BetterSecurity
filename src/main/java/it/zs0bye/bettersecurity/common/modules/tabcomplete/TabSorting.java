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

import it.zs0bye.bettersecurity.common.utils.StringSorting;
import lombok.AllArgsConstructor;

import java.util.Set;

@AllArgsConstructor
public class TabSorting {

    private final TabHandler handler;

    protected Set<String> sort(final Set<String> list) {
        final String read = this.handler.reader("SORT_SUGGESTIONS").getString();
        try {
            final TypeSorting type = TypeSorting.valueOf(read.toUpperCase());
            if (type == TypeSorting.DEFAULT) return list;
            if (type == TypeSorting.ASCENDING) return StringSorting.sort("/", list);
            if (type == TypeSorting.DESCENDING) return StringSorting.reverseSort("/", list);
        } catch (final IllegalArgumentException e) {
            this.handler.getLogger().severe("The value '" + read + "' is not valid for sorting.");
        }
        return list;
    }

    private enum TypeSorting {
        DEFAULT(),
        ASCENDING(),
        DESCENDING()
    }

}
