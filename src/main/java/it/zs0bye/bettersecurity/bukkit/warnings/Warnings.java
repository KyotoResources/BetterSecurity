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

package it.zs0bye.bettersecurity.bukkit.warnings;

import it.zs0bye.bettersecurity.bukkit.BetterSecurityBukkit;
import it.zs0bye.bettersecurity.bukkit.executors.SendExecutors;
import it.zs0bye.bettersecurity.bukkit.files.enums.Config;
import it.zs0bye.bettersecurity.bukkit.warnings.enums.TypeWarning;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Map;

public class Warnings {

    private final BetterSecurityBukkit plugin;
    private final Player player;
    private final TypeWarning type;
    private final String[] values;
    private final String permission;

    private final Map<String, String> placeholders = new HashMap<>();

    public Warnings(final BetterSecurityBukkit plugin, final Player player, final TypeWarning type, final String... values) {
        this.plugin = plugin;
        this.player = player;
        this.type = type;
        this.values = values;
        this.permission = "bettersecurity.broadcast.warnings";
        this.placeholders.put("%player%", this.player.getName());
        this.sendWarningCmds();
        this.sendWarningPBP();
    }

    private void sendWarningCmds() {
        final String command = "/" + this.values[0];
        if(this.type != TypeWarning.COMMANDS) return;
        this.placeholders.put("%command%", command);

        if(Config.WARNINGS_LOG_CONSOLE.getBoolean()) {
            this.plugin.getLogger().info(Config.WARNINGS_FORMATS_CMDS_CONSOLE.getString()
                    .replace("%player%", this.player.getName())
                    .replace("%command%", command));
        }

        if(this.sendProxy(this.player.getName(), command)) return;
        Bukkit.getOnlinePlayers().forEach(players -> {
            if(!players.hasPermission(this.permission)) return;
            SendExecutors.send(this.plugin, Config.WARNINGS_FORMATS_CMDS_FORMAT.getStringList(), players, this.placeholders);
        });
    }

    private void sendWarningPBP() {
        if(this.type != TypeWarning.PORT_BYPASS_PREVENTION) return;
        this.placeholders.put("%port%", this.values[0]);
        this.placeholders.put("%ip%", this.values[1]);

        if(Config.WARNINGS_LOG_CONSOLE.getBoolean()) {
            this.plugin.getLogger().info(Config.WARNINGS_FORMATS_PBP_CONSOLE.getString()
                    .replace("%player%", this.player.getName())
                    .replace("%port%", this.values[0])
                    .replace("%ip%", this.values[1]));
        }

        if(this.sendProxy(this.player.getName(), this.values[0], this.values[1])) return;
        Bukkit.getOnlinePlayers().forEach(players -> {
            if(!players.hasPermission(this.permission)) return;
            SendExecutors.send(this.plugin, Config.WARNINGS_FORMATS_PBP_FORMAT.getStringList(), players, this.placeholders);
        });
    }

    @SneakyThrows
    private boolean sendProxy(final String player, final String command) {
        if(!Config.WARNINGS_PROXY.getBoolean()) return false;

        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        final DataOutputStream out = new DataOutputStream(stream);
        out.writeUTF("WarningCMD");
        out.writeUTF(player);
        out.writeUTF(command);
        this.player.sendPluginMessage(this.plugin, "bsecurity:sender", stream.toByteArray());
        return true;
    }

    @SneakyThrows
    private boolean sendProxy(final String player, final String port, final String ip) {
        if(!Config.WARNINGS_PROXY.getBoolean()) return false;

        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        final DataOutputStream out = new DataOutputStream(stream);
        out.writeUTF("WarningPBP");
        out.writeUTF(player);
        out.writeUTF(port);
        out.writeUTF(ip);
        this.player.sendPluginMessage(this.plugin, "bsecurity:sender", stream.toByteArray());
        return true;
    }

}
