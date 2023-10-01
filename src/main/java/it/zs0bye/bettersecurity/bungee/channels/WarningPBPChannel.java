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

package it.zs0bye.bettersecurity.bungee.channels;

import it.zs0bye.bettersecurity.bungee.BetterSecurityBungee;
import it.zs0bye.bettersecurity.common.channels.ChannelRegistrar;
import lombok.AllArgsConstructor;
import net.md_5.bungee.api.config.ServerInfo;

@AllArgsConstructor
public class WarningPBPChannel extends ChannelRegistrar {

    private final BetterSecurityBungee plugin;

    @Override
    protected String getIdentifier() {
        return "BS-WarningPBP";
    }

    @Override
    protected void receiver() {
        final String player = this.input.readUTF();
        final String port = this.input.readUTF();
        final String ip = this.input.readUTF();

        this.plugin.getProxy().getServers().keySet().forEach(server -> {
            final ServerInfo serverInfo = this.plugin.getProxy().getServerInfo(server);
            serverInfo.sendData(PLUGIN_RETURN, sendOutput(this.getIdentifier(), player, port, ip).toByteArray());
        });
    }
}
