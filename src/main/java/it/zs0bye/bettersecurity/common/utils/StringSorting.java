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

package it.zs0bye.bettersecurity.common.utils;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class StringSorting {

    private static LinkedHashSet<String> sort(final Comparator<? super String> comparator, final Set<String> list) {
        return list.stream().sorted(comparator).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public static LinkedHashSet<String> sort(final String prefix, final Set<String> list) {
        return sort(new PrefixComparator(prefix), list);
    }

    public static LinkedHashSet<String> reverseSort(final String prefix, final Set<String> list) {
        return sort(new PrefixComparator(prefix).reversed(), list);
    }

    private static class PrefixComparator implements Comparator<String> {
        private final String prefix;

        public PrefixComparator(final String prefix) {
            this.prefix = prefix;
        }

        @Override
        public int compare(final String str1, final String str2) {
            boolean startsWithPrefix1 = str1.startsWith(prefix);
            boolean startsWithPrefix2 = str2.startsWith(prefix);

            if (startsWithPrefix1 && !startsWithPrefix2) {
                return -1;
            } else if (!startsWithPrefix1 && startsWithPrefix2) {
                return 1;
            } else {
                return str1.compareTo(str2);
            }
        }
    }

}


