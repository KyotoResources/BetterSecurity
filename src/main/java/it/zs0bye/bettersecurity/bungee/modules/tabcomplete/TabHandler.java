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

package it.zs0bye.bettersecurity.bungee.modules.tabcomplete;

import com.mojang.brigadier.tree.CommandNode;
import it.zs0bye.bettersecurity.bungee.BetterSecurityBungee;
import it.zs0bye.bettersecurity.bungee.files.readers.Tab;
import it.zs0bye.bettersecurity.bungee.modules.tabcomplete.modes.advanced.AdvancedMode;
import it.zs0bye.bettersecurity.bungee.modules.tabcomplete.modes.BasicMode;
import it.zs0bye.bettersecurity.bungee.modules.tabcomplete.providers.SuggestionProvider;
import it.zs0bye.bettersecurity.common.methods.MethodType;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Cancellable;

import java.util.*;

public class TabHandler {

    private final BetterSecurityBungee plugin;
    private final ProxiedPlayer player;

    public TabHandler(final BetterSecurityBungee plugin, final ProxiedPlayer player) {
        this.plugin = plugin;
        this.player = player;
    }

    public MethodType getMethodType(final String path, String group, String permission) {
        group = group.isEmpty() ? group : "." + group;
        permission = permission.isEmpty() ? "bettersecurity.tab" + group : permission;
        for(final MethodType type : MethodType.values()) {
            if(!this.player.hasPermission(permission + "." + type.getName())) continue;
            return type;
        }
        final String configType = Tab.INSTANCE.getString(path).toUpperCase();
        if (!MethodType.getTypes().contains(configType)) {
            this.plugin.getLogger().warning("There was a problem in this path \"" + path + "\"! It can only be either WHITELIST or BLACKLIST. By default it was set to WHITELIST.");
            return MethodType.WHITELIST;
        }
        final MethodType type = MethodType.valueOf(configType);
        if(this.player.hasPermission(permission + ".INVERT")) {
            if(type == MethodType.BLACKLIST) return MethodType.WHITELIST;
            return MethodType.BLACKLIST;
        }
        return type;
    }

    private boolean bypass() {
        if (!Tab.GLOBAL_BYPASS_ENABLED.getBoolean()) return false;
        final String method = Tab.GLOBAL_BYPASS_METHOD.getString();
        final List<String> players = Tab.GLOBAL_BYPASS_PLAYERS.getStringList();
        if (method.equals("PERMISSION")) return this.player.hasPermission("bettersecurity.bypass.tab");
        if (method.equals("PLAYERS")) return players.contains(this.player.getName())
                || players.contains(this.player.getUniqueId().toString());
        return false;
    }

    private SuggestionProvider provider() {
        if (Tab.ADV_MODE_ENABLED.getBoolean()) return new AdvancedMode(this, this.plugin, this.player);
        return new BasicMode(this);
    }

    public void injectTabSuggestions(final List<String> commands, final String completion, final Cancellable cancelled) {
        if (this.bypass()) return;
        this.provider().addSuggestions(commands, completion, cancelled);
    }

    public void injectTabSuggestions(final Set<String> suggestions) {
        if (this.bypass()) return;
        this.provider().addSuggestions(suggestions);
    }

    public void injectTabSuggestions(final Collection<CommandNode<?>> childrens) {
        if (this.bypass()) return;
        this.provider().addSuggestions(childrens);
    }

}
