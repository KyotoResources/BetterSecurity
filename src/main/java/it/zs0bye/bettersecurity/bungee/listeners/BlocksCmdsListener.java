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

package it.zs0bye.bettersecurity.bungee.listeners;

import it.zs0bye.bettersecurity.bungee.BetterSecurityBungee;
import it.zs0bye.bettersecurity.bungee.executors.SendExecutors;
import it.zs0bye.bettersecurity.bungee.files.enums.Config;
import it.zs0bye.bettersecurity.bungee.warnings.Warnings;
import it.zs0bye.bettersecurity.bungee.warnings.enums.TypeWarning;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlocksCmdsListener implements Listener {

    private final BetterSecurityBungee plugin;
    private final Map<String, String> placeholders = new HashMap<>();

    public BlocksCmdsListener(final BetterSecurityBungee plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(final ChatEvent event) {

        if(!Config.BLOCKS_COMMANDS_ENABLED.getBoolean()) return;

        String command = event.getMessage();

        if(!command.startsWith("/")) return;

        command = command
                .split(" ")[0]
                .toLowerCase()
                .replaceFirst("/", "");

        if (!(event.getSender() instanceof ProxiedPlayer)) return;

        final ProxiedPlayer player = (ProxiedPlayer) event.getSender();

        this.placeholders.put("%player%", player.getName());
        this.placeholders.put("%server%", player.getServer().getInfo().getName());
        this.placeholders.put("%command%", "/" + command);

        if(!Config.BLOCKS_COMMANDS_FORCE_CHECK.getBoolean() && event.isCancelled()) return;

        if(player.hasPermission("bettersecurity.bypass.blockscmds")) return;
        if(this.canBlock(command, player)) return;

        SendExecutors.send(this.plugin, this.getExecutors(command, player), player, this.placeholders);
        event.setCancelled(true);

        if(!Config.BLOCKS_COMMANDS_WARNING.getBoolean()) return;
        new Warnings(this.plugin, player, TypeWarning.COMMANDS, command);
    }

    private boolean canBlock(final String command, final ProxiedPlayer player, final List<String> commands) {
        final String method = this.getMethod(command, player);
        if(commands.isEmpty()) return true;
        if(method.equals("BLACKLIST")) return !commands.contains(command);
        if(method.equals("WHITELIST")) return commands.contains(command);
        return true;
    }

    private boolean canBlock(final String command, final ProxiedPlayer player) {
        return this.canBlock(command, player, this.getWhitelistedCommands(command, player));
    }

    private boolean checkDefaultBlock(final String command) {
        final String method = Config.BLOCKS_COMMANDS_METHOD.getString();
        final List<String> commands = Config.BLOCKS_COMMANDS.getStringList();
        if(commands.isEmpty()) return false;
        if(method.equals("BLACKLIST")) return commands.contains(command);
        if(method.equals("WHITELIST")) return !commands.contains(command);
        return false;
    }

    private List<String> getWhitelistedCommands(final String command, final ProxiedPlayer player) {
        final List<String> commands = new ArrayList<>(Config.BLOCKS_COMMANDS.getStringList());
        if(this.checkDefaultBlock(command)) return commands;
        if(!Config.BLOCKS_COMMANDS_SERVER_MODE_ENABLED.getBoolean()) return commands;
        final String path = this.getPath(player) + Config.BLOCKS_COMMANDS_SERVER_MODE_COMMANDS.getPath();
        if(!Config.CUSTOM.contains(path)) return commands;
        commands.addAll(Config.CUSTOM.getStringList(path));
        return commands;
    }

    private String getMethod(final String command, final ProxiedPlayer player) {
        final String method = Config.BLOCKS_COMMANDS_METHOD.getString();
        if(this.checkDefaultBlock(command)) return method;
        if(!Config.BLOCKS_COMMANDS_SERVER_MODE_ENABLED.getBoolean()) return method;
        final String path = this.getPath(player) + Config.BLOCKS_COMMANDS_SERVER_MODE_METHOD.getPath();
        if(!Config.CUSTOM.contains(path)) return method;
        return Config.CUSTOM.getString(path);
    }

    private List<String> getExecutors(final String command, final ProxiedPlayer player) {
        final List<String> defaultCmds = Config.BLOCKS_COMMANDS_EXECUTORS.getStringList();
        if(this.checkDefaultBlock(command)) return defaultCmds;
        if(!Config.BLOCKS_COMMANDS_SERVER_MODE_ENABLED.getBoolean()) return defaultCmds;
        final String path = this.getPath(player) + Config.BLOCKS_COMMANDS_SERVER_MODE_EXECUTORS.getPath();
        if(!Config.CUSTOM.contains(path)) return defaultCmds;
        return Config.CUSTOM.getStringList(path);
    }

    private String getPath(final ProxiedPlayer player) {
        final String server = player.getServer().getInfo().getName();
        return Config.BLOCKS_COMMANDS_SERVER_MODE_SERVERS.getPath() + "." + server;
    }

}
