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

package it.zs0bye.bettersecurity.bukkit.listeners;

import com.google.common.io.ByteArrayDataOutput;
import it.zs0bye.bettersecurity.bukkit.BetterSecurityBukkit;
import it.zs0bye.bettersecurity.bukkit.BukkitUser;
import it.zs0bye.bettersecurity.bukkit.files.readers.Tab;
import it.zs0bye.bettersecurity.common.BetterUser;
import it.zs0bye.bettersecurity.common.SoftwareType;
import it.zs0bye.bettersecurity.common.channels.ChannelRegistrar;
import it.zs0bye.bettersecurity.common.modules.tabcomplete.TabHandler;
import it.zs0bye.bettersecurity.common.modules.tabcomplete.providers.SuggestionProvider;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

@AllArgsConstructor
public class TabMergeListener implements Listener {

    private final BetterSecurityBukkit plugin;

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        this.plugin.getServer().getScheduler().runTaskLaterAsynchronously(this.plugin, () -> this.inject(event.getPlayer()), 20L);
    }

    @EventHandler
    public void onChangeWorld(final PlayerChangedWorldEvent event) {
        this.inject(event.getPlayer());
    }

    @SneakyThrows
    private void inject(final Player player) {
        if(!this.plugin.getSpigotFile().getConfig().getBoolean("settings.bungeecord")) return;
        final BetterUser user = new BukkitUser(player);
        final SuggestionProvider provider = new TabHandler(this.plugin.getLogger(), user, Tab.class, SoftwareType.CRAFTBUKKIT).injectTabSuggestions();
        if(provider == null) return;

        final ByteArrayDataOutput stream = ChannelRegistrar.sendOutput("BS-TabComplete",
                player.getName(),
                String.join(", ", provider.getWhitelisted()),
                String.join(", ", provider.getBlacklisted()));

        player.sendPluginMessage(this.plugin, ChannelRegistrar.PLUGIN_SENDER, stream.toByteArray());
    }

}
