/*
 * Security plugin for your server - https://github.com/KyotoResources/BetterSecurity
 * Copyright (C) 2023 KyotoResources
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

import it.zs0bye.bettersecurity.common.utils.enums.FontUtils;

import java.util.ArrayList;
import java.util.List;

public class CStringUtils {

    public static List<String> copyPartialMatches(final String token, final Iterable<String> originals, final List<String> collection) {

        originals.forEach(original -> {
            if (!startsWithIgnoreCase(original, token)) return;
            collection.add(original);
        });

        return collection;
    }

    public static List<String> copyPartialMatches(final String token, final Iterable<String> originals) {
        return CStringUtils.copyPartialMatches(token, originals, new ArrayList<>());
    }

    private static boolean startsWithIgnoreCase(final String original, final String prefix) {
        if (original.length() < prefix.length()) return false;
        return original.regionMatches(true, 0, prefix, 0, prefix.length());
    }

    public static String center(final String message) {

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for(char c : message.toCharArray()) {
            if(c == '§'){
                previousCode = true;
            } else if(previousCode) {
                previousCode = false;
                isBold = c == 'l' || c == 'L';
            } else {
                FontUtils dFI = FontUtils.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = 154 - halvedMessageSize;
        int spaceLength = FontUtils.SPACE.getLength() + 1;
        int compensated = 0;

        final StringBuilder sb = new StringBuilder();
        while(compensated < toCompensate){
            sb.append(" ");
            compensated += spaceLength;
        }
        return sb + message;
    }

}
