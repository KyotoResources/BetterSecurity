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
import it.zs0bye.bettersecurity.bukkit.warnings.Warnings;
import it.zs0bye.bettersecurity.bukkit.executors.SendExecutors;
import it.zs0bye.bettersecurity.bukkit.files.enums.Config;
import it.zs0bye.bettersecurity.bukkit.warnings.enums.TypeWarning;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;
import java.util.Map;

public class BlocksCmdsListener implements Listener {

    private final BetterSecurityBukkit plugin;
    private final Map<String, String> placeholders;

    public BlocksCmdsListener(final BetterSecurityBukkit plugin) {
        this.plugin = plugin;
        this.placeholders = this.plugin.getCmdsPlaceholders();
        if(!Config.BLOCKS_COMMANDS_ENABLED.getBoolean()) return;
        this.plugin.getServer().getPluginManager().registerEvent(
                PlayerCommandPreprocessEvent.class,
                this,
                EventPriority.valueOf(Config.BLOCKS_COMMANDS_PRIORITY.getString().toUpperCase()),
                ((listener, event) -> this.onCommandPreprocess((PlayerCommandPreprocessEvent) event)),
                this.plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCommandPreprocess(final PlayerCommandPreprocessEvent event) {

        if(!Config.BLOCKS_COMMANDS_ENABLED.getBoolean()) return;

        final Player player = event.getPlayer();
        final String command = event.getMessage()
                .split(" ")[0]
                .toLowerCase()
                .replaceFirst("/", "");

        if(player.hasPermission("bettersecurity.bypass.blockscmds")) return;
        if(this.canBlock(command)) return;

        this.placeholders.put("%player%", player.getName());
        this.placeholders.put("%command%", "/" + command);

        SendExecutors.send(this.plugin, Config.BLOCKS_COMMANDS_EXECUTORS.getStringList(), player, this.placeholders);
        event.setCancelled(true);

        if(!Config.BLOCKS_COMMANDS_WARNING.getBoolean()) return;
        new Warnings(this.plugin, player, TypeWarning.COMMANDS, command);
    }

    private boolean canBlock(final String command) {
        final String method = Config.BLOCKS_COMMANDS_METHOD.getString();
        final List<String> commands = Config.BLOCKS_COMMANDS.getStringList();
        if(method.equals("BLACKLIST")) return !commands.contains(command);
        if(method.equals("WHITELIST")) return commands.contains(command);
        return true;
    }

}
