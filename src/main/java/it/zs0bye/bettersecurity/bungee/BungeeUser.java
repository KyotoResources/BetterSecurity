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

package it.zs0bye.bettersecurity.bungee;

import it.zs0bye.bettersecurity.common.BetterUser;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class BungeeUser implements BetterUser {

    private final CommandSender sender;

    public BungeeUser(final CommandSender sender) {
        this.sender = sender;
    }

    public BungeeUser(final String name) {
        this.sender = ProxyServer.getInstance().getPlayer(name);
    }

    public BungeeUser(final UUID uuid) {
        this.sender = ProxyServer.getInstance().getPlayer(uuid);
    }

    @Override
    public ProxiedPlayer getPlayer() {
        return (ProxiedPlayer) this.sender;
    }

    @Override
    public CommandSender getSender() {
        return this.sender;
    }

    @Override
    public String getName() {
        return this.sender.getName();
    }

    @Override
    public UUID getUniqueId() {
        return this.getPlayer().getUniqueId();
    }

    @Override
    public String getServerName() {
        return this.getPlayer().getServer().getInfo().getName();
    }

    @Override
    public String getWorldName() {
        return null;
    }

    @Override
    public boolean hasPermission(final String permission) {
        return this.sender.hasPermission(permission);
    }

}
