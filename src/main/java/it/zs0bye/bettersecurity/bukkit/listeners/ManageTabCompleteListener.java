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
import it.zs0bye.bettersecurity.bukkit.BukkitUser;
import it.zs0bye.bettersecurity.bukkit.files.readers.Tab;
import it.zs0bye.bettersecurity.bukkit.modules.Module;
import it.zs0bye.bettersecurity.bukkit.utils.VersionUtils;
import it.zs0bye.bettersecurity.common.BetterUser;
import it.zs0bye.bettersecurity.common.SoftwareType;
import it.zs0bye.bettersecurity.common.modules.tabcomplete.TabHandler;
import lombok.AllArgsConstructor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.server.TabCompleteEvent;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class ManageTabCompleteListener implements Listener {

    private final BetterSecurityBukkit plugin;

    @EventHandler
    public void onTabComplete(final TabCompleteEvent event) {
        if(Module.TAB_COMPLETE.isDisabled()) return;
        if(event.getSender() instanceof ConsoleCommandSender) return;

        final BetterUser user = new BukkitUser(event.getSender());
        final String completion = event.getBuffer();
        final List<String> suggestions = event.getCompletions();
        final TabHandler handler = new TabHandler(this.plugin.getLogger(), user, Tab.class, SoftwareType.CRAFTBUKKIT);
        handler.injectTabSuggestions(suggestions, completion, event::setCancelled);
        handler.injectTabChildrens(completion, suggestions, event::setCancelled);
    }

    @EventHandler
    public void onPlayerCommandSend(final PlayerCommandSendEvent event) {
        if(Module.TAB_COMPLETE.isDisabled()) return;
        final BetterUser user = new BukkitUser(event.getPlayer());

        final Collection<String> commands = event.getCommands();
        final Set<String> completions = new HashSet<>(commands);
        new TabHandler(this.plugin.getLogger(), user, Tab.class, SoftwareType.CRAFTBUKKIT).injectTabSuggestions(completions);
        commands.removeIf(command -> !completions.contains(command));
    }

    public static void register(final BetterSecurityBukkit plugin) {
        if(VersionUtils.legacy()) return;
        plugin.getServer().getPluginManager().registerEvents(new ManageTabCompleteListener(plugin), plugin);
    }

}
