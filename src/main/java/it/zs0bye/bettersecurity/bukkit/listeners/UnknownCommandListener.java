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
import it.zs0bye.bettersecurity.bukkit.executors.SendExecutors;
import it.zs0bye.bettersecurity.bukkit.files.enums.Config;
import it.zs0bye.bettersecurity.bukkit.utils.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Map;

public class UnknownCommandListener implements Listener {

    private final BetterSecurityBukkit plugin;
    private final Map<String, String> placeholders;

    public UnknownCommandListener(final BetterSecurityBukkit plugin) {
        this.plugin = plugin;
        this.placeholders = this.plugin.getCmdsPlaceholders();
        if(!Config.UNKNOWN_COMMAND_ENABLED.getBoolean()) return;

        this.plugin.getServer().getPluginManager().registerEvent(
                PlayerCommandPreprocessEvent.class,
                this,
                EventPriority.valueOf(Config.UNKNOWN_COMMAND_PRIORITY.getString().toUpperCase()),
                ((listener, event) -> this.onCommandPreProcess((PlayerCommandPreprocessEvent) event)),
                this.plugin, true);
    }

    @EventHandler
    public void onCommandPreProcess(final PlayerCommandPreprocessEvent event) {

        if(!Config.UNKNOWN_COMMAND_ENABLED.getBoolean()) return;

        final Player player = event.getPlayer();
        final String command = event.getMessage()
                .split(" ")[0]
                .toLowerCase();

        if(Config.UNKNOWN_COMMAND_EXEMPT_COMMANDS.getStringList().contains(command
                .replaceFirst("/", ""))) return;

        if (this.plugin.getServer().getHelpMap().getHelpTopic(command) != null) return;
        event.setCancelled(true);

        this.placeholders.put("%player%", player.getName());
        this.placeholders.put("%command%", command);

        if(Config.UNKNOWN_COMMAND_USE_SPIGOT_MESSAGE.getBoolean()) {
            player.sendMessage(StringUtils.colorize(this.plugin.getSpigotFile().getConfig().getString("messages.unknown-command")));
            return;
        }

        SendExecutors.send(this.plugin, Config.UNKNOWN_COMMAND_EXECUTORS.getStringList(), player, this.placeholders);
    }


}
