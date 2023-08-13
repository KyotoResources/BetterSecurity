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

import com.mojang.brigadier.tree.CommandNode;
import io.netty.channel.Channel;
import it.zs0bye.bettersecurity.bungee.BetterSecurityBungee;
import it.zs0bye.bettersecurity.bungee.event.PacketEvent;
import it.zs0bye.bettersecurity.bungee.files.readers.Tab;
import it.zs0bye.bettersecurity.bungee.modules.Module;
import it.zs0bye.bettersecurity.bungee.packet.PacketDecoder;
import it.zs0bye.bettersecurity.bungee.packet.PacketEncoder;
import it.zs0bye.bettersecurity.bungee.modules.tabcomplete.TabComplete;
import it.zs0bye.bettersecurity.common.utils.VersionUtils;
import net.md_5.bungee.ServerConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import net.md_5.bungee.netty.PipelineUtils;
import net.md_5.bungee.protocol.packet.Commands;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class PacketsListener implements Listener {

    private final BetterSecurityBungee plugin;

    private final String BS_PACKET_DECODER = "bs-packet-decoder";
    private final String BS_PACKET_ENCODER = "bs-packet-encoder";

    public PacketsListener(final BetterSecurityBungee plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onServerSwitch(final ServerSwitchEvent event) {
        if(Module.TAB_COMPLETE.isDisabled()) return;
        if(Tab.IGNORE_1_13_TAB_COMPLETE.getBoolean()) return;
        final ProxiedPlayer player = event.getPlayer();
        if(player == null || !player.isConnected()) return;
        final Channel channel = this.channel(player);
        if(channel == null) return;
        channel.pipeline().addAfter(PipelineUtils.PACKET_DECODER, this.BS_PACKET_DECODER, new PacketDecoder(player));
        channel.pipeline().addAfter(PipelineUtils.PACKET_ENCODER, this.BS_PACKET_ENCODER, new PacketEncoder(player));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPostLogin(final PostLoginEvent event) {
        if(Module.TAB_COMPLETE.isDisabled()) return;
        if(Tab.IGNORE_1_13_TAB_COMPLETE.getBoolean()) return;
        final ProxiedPlayer player = event.getPlayer();
        final Channel channel = this.channel(player);
        if(channel == null) return;
        channel.pipeline().addAfter(PipelineUtils.PACKET_DECODER, this.BS_PACKET_DECODER, new PacketDecoder(player));
        channel.pipeline().addAfter(PipelineUtils.PACKET_ENCODER, this.BS_PACKET_ENCODER, new PacketEncoder(player));
    }

    @SuppressWarnings("unchecked")
    @EventHandler
    public void onPacketEvent(final PacketEvent event) {
        if (!(event.getSender() instanceof ProxiedPlayer)) return;
        if (!(event.getPacket() instanceof Commands)) return;
        final ProxiedPlayer player = event.getPlayer();
        final Commands packet = (Commands) event.getPacket();
        final Collection<CommandNode<?>> childrens = packet.getRoot().getChildren();
        childrens.removeIf(node -> node.getName().contains(":"));
        new TabComplete(this.plugin, player).applyTabChildren(childrens);

        player.getServer().getInfo().ping((serverPing, throwable) -> {
            if(serverPing == null || serverPing.getVersion() == null) return;
            final String name = serverPing.getVersion().getName();
            if(!name.contains(" ") && !name.contains(".")) {
                this.plugin.getLogger().warning("The version of the server that player \"" + player.getName() + "\" connected to could not be detected.");
                this.plugin.getLogger().warning("It is possible that you are using a spigot fork that has changed the version name.");
                this.plugin.getLogger().warning("Ask the fork author to add the server version, like in the example: \"Spigot 1.12.2\".");
                this.plugin.getLogger().warning("If you have any doubts, feel free to contact our support via our discord server:");
                this.plugin.getLogger().warning("https://ds.kyotoresources.space");
                return;
            }
            String version = name.split(" ")[1];
            if(version.split("\\.").length == 3)
                version = version.split("\\.")[0] + "." + version.split("\\.")[1];
            if(VersionUtils.legacy(Double.parseDouble(version))) return;
            if(!WaterTabCompleteListener.isRegistered()) return;
            this.plugin.getProxy().getScheduler().schedule(this.plugin, () ->
                    player.unsafe().sendPacket(packet), 10L, TimeUnit.MILLISECONDS);
        });
    }

    private Channel channel(final ProxiedPlayer player) {
        if(player.getPendingConnection().getVersion() < 393) return null;
        final ServerConnection connection = (ServerConnection) player.getServer();
        if(connection == null) return null;
        return connection.getCh().getHandle();
    }

}
