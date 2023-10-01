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

package it.zs0bye.bettersecurity.bukkit.channels;

import it.zs0bye.bettersecurity.bukkit.BetterSecurityBukkit;
import it.zs0bye.bettersecurity.bukkit.executors.SendExecutors;
import it.zs0bye.bettersecurity.bukkit.files.readers.Config;
import it.zs0bye.bettersecurity.common.channels.ChannelRegistrar;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class WarningCMDChannel extends ChannelRegistrar {

    private final BetterSecurityBukkit plugin;

    @Override
    protected String getIdentifier() {
        return "BS-WarningCMD";
    }

    @Override
    protected void receiver() {
        if(!Config.WARNINGS_PROXY.getBoolean()) return;
        final String player = this.input.readUTF();
        final String command = this.input.readUTF();

        final Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%player%", player);
        placeholders.put("%command%", command);

        Bukkit.getOnlinePlayers().forEach(players -> {
            if(!players.hasPermission("bettersecurity.broadcast.warnings")) return;
            SendExecutors.send(this.plugin, Config.WARNINGS_FORMATS_CMDS_FORMAT.getStringList(), players, placeholders);
        });
    }
}
