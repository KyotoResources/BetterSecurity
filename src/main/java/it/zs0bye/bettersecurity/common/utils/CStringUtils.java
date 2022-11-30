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
