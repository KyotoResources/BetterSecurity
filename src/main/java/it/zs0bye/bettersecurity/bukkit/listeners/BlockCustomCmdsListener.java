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

package it.zs0bye.bettersecurity.bukkit.listeners;

import it.zs0bye.bettersecurity.bukkit.BetterSecurityBukkit;
import it.zs0bye.bettersecurity.bukkit.files.readers.Command;
import it.zs0bye.bettersecurity.bukkit.modules.Module;
import it.zs0bye.bettersecurity.bukkit.warnings.Warnings;
import it.zs0bye.bettersecurity.bukkit.executors.SendExecutors;
import it.zs0bye.bettersecurity.bukkit.warnings.enums.TypeWarning;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BlockCustomCmdsListener implements Listener {

    private final BetterSecurityBukkit plugin;
    private final Map<String, String> placeholders;

    private String regCommand;
    private List<String> regCommands;
    private List<String> executors;
    private boolean warning;
    private String permission_required;
    private List<String> required_players;

    public BlockCustomCmdsListener(final BetterSecurityBukkit plugin) {
        this.plugin = plugin;
        this.placeholders = this.plugin.getCmdsPlaceholders();
        if(Module.COMMANDS.isDisabled()) return;
        if(!Command.BLOCK_CUSTOM_COMMANDS_ENABLED.getBoolean()) return;
        Command.BLOCK_CUSTOM_COMMANDS.getSection().forEach(command -> {
            final String path = Command.BLOCK_CUSTOM_COMMANDS.getPath() + "." + command;
            final String priority = Command.BLOCK_CUSTOM_COMMANDS_PRIORITY.getString(path).toUpperCase();
            final BlockCustomCmdsListener cmdsListener = new BlockCustomCmdsListener(this.plugin, command);
            this.plugin.getServer().getPluginManager().registerEvent(
                    PlayerCommandPreprocessEvent.class,
                    cmdsListener,
                    EventPriority.valueOf(priority),
                    (listener, event) -> cmdsListener.onCommandPreprocess((PlayerCommandPreprocessEvent) event),
                    this.plugin);
        });
    }

    public BlockCustomCmdsListener(final BetterSecurityBukkit plugin, final String command) {
        this.plugin = plugin;
        this.placeholders = this.plugin.getCmdsPlaceholders();

        if(Module.COMMANDS.isDisabled()) return;
        if(!Command.BLOCK_CUSTOM_COMMANDS_ENABLED.getBoolean()) return;

        final String path = Command.BLOCK_CUSTOM_COMMANDS.getPath() + "." + command;
        final String warning_path = path + Command.BLOCK_CUSTOM_COMMANDS_WARNING.getPath();
        final String permission_path = path + Command.BLOCK_CUSTOM_COMMANDS_PERMISSION_REQUIRED.getPath();
        final String required_players_path = path + Command.BLOCK_CUSTOM_COMMANDS_REQUIRED_PLAYERS.getPath();
        final String regCommand = path + Command.BLOCK_CUSTOM_COMMANDS_COMMAND.getPath();
        final String regCommands = path + Command.BLOCK_CUSTOM_COMMANDS_COMMANDS.getPath();

        this.regCommand = Command.INSTANCE.contains(regCommand) ? Command.INSTANCE.getString(regCommand) : "";
        this.regCommands = Command.INSTANCE.contains(regCommands) ? Command.INSTANCE.getStringList(regCommands) : new ArrayList<>();
        this.executors = Command.INSTANCE.getStringList(path + Command.BLOCK_CUSTOM_COMMANDS_EXECUTORS.getPath());

        if(this.contains(warning_path)) this.warning = Command.INSTANCE.getBoolean(warning_path);
        if(this.contains(permission_path)) this.permission_required = Command.INSTANCE.getString(permission_path);
        if(this.contains(required_players_path)) this.required_players = Command.INSTANCE.getStringList(required_players_path);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCommandPreprocess(final PlayerCommandPreprocessEvent event) {

        if(Module.COMMANDS.isDisabled()) return;
        if(!Command.BLOCK_CUSTOM_COMMANDS_ENABLED.getBoolean()) return;

        final Player player = event.getPlayer();
        final String command = event.getMessage()
                .split(" ")[0]
                .toLowerCase()
                .replaceFirst("/", "");

        if(this.permission_required != null && player.hasPermission(this.permission_required)) return;
        if(this.required_players != null && (this.required_players.contains(player.getName()) || this.required_players.contains(player.getUniqueId().toString()))) return;
        if(this.regCommand != null && !this.regCommand.isEmpty() && !this.regCommand.equalsIgnoreCase(command)) return;
        if(this.regCommands != null && !this.regCommands.isEmpty() && !this.regCommands.contains(command)) return;

        this.placeholders.put("%player%", player.getName());
        this.placeholders.put("%command%", "/" + command);

        SendExecutors.send(this.plugin, this.executors, player, this.placeholders);
        event.setCancelled(true);

        if(!this.warning) return;
        new Warnings(this.plugin, player, TypeWarning.COMMANDS, command);
    }

    private boolean contains(final String path) {
        return Command.INSTANCE.contains(path);
    }

}
