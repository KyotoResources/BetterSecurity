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

package it.zs0bye.bettersecurity.common.methods;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum MethodType {
    BLACKLIST("BLACKLIST"),
    WHITELIST("WHITELIST");

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