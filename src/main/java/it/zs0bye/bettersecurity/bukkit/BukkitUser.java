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

package it.zs0bye.bettersecurity.bukkit;

import it.zs0bye.bettersecurity.common.BetterUser;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BukkitUser implements BetterUser {

    private final CommandSender sender;

    public BukkitUser(final CommandSender sender) {
        this.sender = sender;
    }

    public BukkitUser(final String name) {
        this.sender = Bukkit.getPlayer(name);
    }

    public BukkitUser(final UUID uuid) {
        this.sender = Bukkit.getPlayer(uuid);
    }

    @Override
    public Player getPlayer() {
        return (Player) this.sender;
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
        return null;
    }

    @Override
    public String getWorldName() {
        return this.getPlayer().getWorld().getName();
    }

    @Override
    public boolean hasPermission(final String permission) {
        return this.sender.hasPermission(permission);
    }

}
