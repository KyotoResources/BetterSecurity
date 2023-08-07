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

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import it.zs0bye.bettersecurity.bungee.BetterSecurityBungee;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PluginMessageListener implements Listener {

    private final BetterSecurityBungee plugin;

    public PluginMessageListener(final BetterSecurityBungee plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("all")
    @EventHandler
    public void onPluginMessage(final PluginMessageEvent event) {
        if (!event.getTag().equalsIgnoreCase( "bsecurity:sender")) return;

        final ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
        final String subChannel = in.readUTF();
        this.initCMDChannel(subChannel, in);
        this.initPBPChannel(subChannel, in);
    }

    private void initCMDChannel(final String subChannel, final ByteArrayDataInput in) {
        if (!subChannel.equalsIgnoreCase("WarningCMD")) return;

        final String player = in.readUTF();
        final String command = in.readUTF();

        this.plugin.getProxy().getServersCopy().keySet().forEach(server -> {
            final ServerInfo serverInfo = this.plugin.getProxy().getServerInfo(server);

            final ByteArrayOutputStream stream = new ByteArrayOutputStream();
            final DataOutputStream out = new DataOutputStream(stream);
            try {
                out.writeUTF("WarningCMD");
                out.writeUTF(player);
                out.writeUTF(command);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            serverInfo.sendData("bsecurity:return", stream.toByteArray());
        });
    }

    private void initPBPChannel(final String subChannel, final ByteArrayDataInput in) {
        if (!subChannel.equalsIgnoreCase("WarningPBP")) return;

        final String player = in.readUTF();
        final String port = in.readUTF();
        final String ip = in.readUTF();

        this.plugin.getProxy().getServersCopy().keySet().forEach(server -> {
            final ServerInfo serverInfo = this.plugin.getProxy().getServerInfo(server);

            final ByteArrayOutputStream stream = new ByteArrayOutputStream();
            final DataOutputStream out = new DataOutputStream(stream);
            try {
                out.writeUTF("WarningPBP");
                out.writeUTF(player);
                out.writeUTF(port);
                out.writeUTF(ip);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            serverInfo.sendData("bsecurity:return", stream.toByteArray());
        });
    }

}
