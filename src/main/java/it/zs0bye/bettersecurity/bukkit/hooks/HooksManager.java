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

package it.zs0bye.bettersecurity.bukkit.hooks;

import it.zs0bye.bettersecurity.bukkit.BetterSecurityBukkit;
import it.zs0bye.bettersecurity.bukkit.files.enums.Config;
import it.zs0bye.bettersecurity.bukkit.hooks.enums.Hooks;
import it.zs0bye.bettersecurity.bukkit.protocols.ChatProtocol;
import it.zs0bye.bettersecurity.bukkit.protocols.TabProtocol;
import it.zs0bye.bettersecurity.bukkit.utils.StringUtils;
import lombok.Getter;

import org.bukkit.entity.Player;

@Getter
public class HooksManager {

    private final BetterSecurityBukkit plugin;

    public HooksManager(final BetterSecurityBukkit plugin) {
        this.plugin = plugin;
        this.load();
    }

    public void load() {
        Hooks.PLACEHOLDERAPI.load();
        Hooks.PROTOCOLLIB.load();
    }

    public String replacePlaceholders(final Player player, final String message) {
        if(!Hooks.PLACEHOLDERAPI.isCheck()) return StringUtils.colorize(message);
        return new HPlaceholderAPI(player).getPlaceholders(message);
    }

    public void regChatProtocol() {
        if(!Hooks.PROTOCOLLIB.isCheck()) return;
        Config.REPLACE_CUSTOM_MESSAGES.getConfigurationSection().forEach(message -> new ChatProtocol(this.plugin, message));
    }

    public void regTabProtocol() {
        if(!Hooks.PROTOCOLLIB.isCheck()) return;
        new TabProtocol(this.plugin);
    }

    public void unregisterAllProtocols() {
        if(!Hooks.PROTOCOLLIB.isCheck()) return;
        new HProtocolLib(this.plugin).unregisterAll();
    }


}
