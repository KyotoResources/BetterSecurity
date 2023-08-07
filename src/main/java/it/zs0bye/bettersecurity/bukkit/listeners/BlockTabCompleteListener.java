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

import it.zs0bye.bettersecurity.bukkit.TabComplete;
import it.zs0bye.bettersecurity.bukkit.files.enums.Config;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.server.TabCompleteEvent;

import java.util.ArrayList;
import java.util.List;

public class BlockTabCompleteListener implements Listener {

    @EventHandler
    public void onTabComplete(final TabCompleteEvent event) {

        if(!Config.BLOCK_TAB_COMPLETE_ENABLED.getBoolean()) return;
        if(event.getSender() instanceof ConsoleCommandSender) return;

        final Player player = (Player) event.getSender();

        final String completion = event.getBuffer();
        final List<String> suggestions = event.getCompletions();
        final List<String> completions = new TabComplete(player).getCompletions(true);

        final List<String> blockCmds = Config.BLOCK_TAB_COMPLETE_BLACKLISTED_SUGGESTIONS.getStringList();

        if(new TabComplete(player).bypass()) return;

        blockCmds.forEach(blockCmd -> {
            if(!completion.startsWith("/" + blockCmd)) return;
            suggestions.clear();
        });

        if(completions.contains(completion.split(" ")[0].toLowerCase())) return;
        suggestions.clear();
    }

    @EventHandler
    public void onPlayerCommandSend(final PlayerCommandSendEvent event) {

        if(!Config.BLOCK_TAB_COMPLETE_ENABLED.getBoolean()) return;

        final Player player = event.getPlayer();
        final List<String> commands = new ArrayList<>(event.getCommands());
        final List<String> completions = new TabComplete(player).getCompletions(false);

        final TabComplete tabComplete = new TabComplete(player);
        if(tabComplete.bypass()) return;

        commands.forEach(command -> {
            if (completions.contains(command)) return;
            event.getCommands().remove(command);
        });
    }

}
