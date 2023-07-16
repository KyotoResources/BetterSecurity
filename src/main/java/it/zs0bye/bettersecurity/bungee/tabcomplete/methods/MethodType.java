package it.zs0bye.bettersecurity.bungee.tabcomplete.methods;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public enum MethodType {
    BLACKLIST("BLACKLIST"),
    WHITELIST("WHITELIST");

    @Getter
    private final String name;

    MethodType(final String name) {
        this.name = name;
    }

    public static List<String> getTypes() {
        final List<String> types = new ArrayList<>();
        for (final MethodType type : MethodType.values()) types.add(type.getName());
        return types;
    }
}