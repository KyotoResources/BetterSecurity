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
import it.zs0bye.bettersecurity.bungee.channels.TabMergeChannel;
import it.zs0bye.bettersecurity.bungee.channels.WarningCMDChannel;
import it.zs0bye.bettersecurity.bungee.channels.WarningPBPChannel;
import it.zs0bye.bettersecurity.common.channels.ChannelHandler;
import it.zs0bye.bettersecurity.common.channels.ChannelRegistrar;
import lombok.AllArgsConstructor;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

@AllArgsConstructor
public class PluginMessageListener implements Listener {

    private final BetterSecurityBungee plugin;

    @EventHandler
    public void onPluginMessage(final PluginMessageEvent event) {
        if (!event.getTag().equalsIgnoreCase(ChannelRegistrar.PLUGIN_SENDER)) return;

        final ChannelHandler handler = new ChannelHandler(event.getData());
        handler.register(new WarningCMDChannel(this.plugin));
        handler.register(new WarningPBPChannel(this.plugin));
        handler.register(new TabMergeChannel(this.plugin));
    }

}
