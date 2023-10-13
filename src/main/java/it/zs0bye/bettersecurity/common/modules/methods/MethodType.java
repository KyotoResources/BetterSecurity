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

import it.zs0bye.bettersecurity.common.BetterUser;
import it.zs0bye.bettersecurity.common.modules.tabcomplete.TabHandler;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum MethodType {
    INSTANCE(""),
    BLACKLIST("BLACKLIST"),
    WHITELIST("WHITELIST");

    private final String name;
    private String group = "", permission = "";

    MethodType(final String name) {
        this.name = name;
    }

    public static List<String> getTypes() {
        final List<String> types = new ArrayList<>();
        for (final MethodType type : MethodType.values()) types.add(type.getName());
        return types;
    }

    public MethodType getTypeByPath(final TabHandler handler, final String path) {
        this.group = group.isEmpty() ? this.group : "." + this.group;
        this.permission = this.permission.isEmpty() ? "bettersecurity.tab" + this.group : this.permission;
        final BetterUser user = handler.getUser();
        for(final MethodType type : MethodType.values()) {
            if(!user.hasPermission(this.permission + "." + type.getName())) continue;
            return type;
        }
        final String configType = handler.reader("INSTANCE").getString(path).toUpperCase();
        if (!MethodType.getTypes().contains(configType)) {
            handler.getLogger().warning("There was a problem in this path \"" + path + "\"! It can only be either WHITELIST or BLACKLIST. By default it was set to WHITELIST.");
            return MethodType.WHITELIST;
        }
        final MethodType type = MethodType.valueOf(configType);
        if(user.hasPermission(this.permission + ".INVERT")) {
            if(type == MethodType.BLACKLIST) return MethodType.WHITELIST;
            return MethodType.BLACKLIST;
        }
        return type;
    }

    public MethodType setGroup(final String group) {
        this.group = group;
        return this;
    }

    public MethodType setPermission(final String permission) {
        this.permission = permission;
        return this;
    }

}