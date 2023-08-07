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

import it.zs0bye.bettersecurity.bungee.TabComplete;
import it.zs0bye.bettersecurity.bungee.files.enums.Config;
import it.zs0bye.bettersecurity.common.utils.CStringUtils;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

public class BlockTabCompleteListener implements Listener {

    @EventHandler
    public void onTabComplete(final TabCompleteEvent event) {

        if(!Config.BLOCK_TAB_COMPLETE_ENABLED.getBoolean()) return;

        final ProxiedPlayer player = (ProxiedPlayer) event.getSender();

        final String completion = event.getCursor();
        final List<String> commands = event.getSuggestions();
        final List<String> blockCmds = Config.BLOCK_TAB_COMPLETE_BLACKLISTED_SUGGESTIONS.getStringList();

        final List<String> completions = new TabComplete(player).getCompletions(true);
        final List<String> matches = CStringUtils.copyPartialMatches(completion, completions);

        if(new TabComplete(player).bypass()) return;

        blockCmds.forEach(blockCmd -> {
            if(!completion.startsWith("/" + blockCmd)) return;
            commands.clear();
            event.setCancelled(true);
        });

        if(!completion.startsWith("/") || completion.contains(" ")) return;
        if(completions.isEmpty()) {
            commands.clear();
            event.setCancelled(true);
            return;
        }

        completions.forEach(command -> {
            if(completion.startsWith("/" + command)) return;
            commands.clear();
        });

        if(Config.BLOCK_TAB_COMPLETE_WHITELISTED_SUGGESTIONS_PARTIAL_MATCHES.getBoolean()) {
            commands.addAll(matches);
            return;
        }
        commands.addAll(completions);
    }

}
