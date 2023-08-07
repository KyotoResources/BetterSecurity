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

package it.zs0bye.bettersecurity.bungee.packet;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import it.zs0bye.bettersecurity.bungee.event.PacketEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.protocol.DefinedPacket;

import java.util.List;

public class PacketEncoder extends MessageToMessageEncoder<DefinedPacket> {

	private final ProxiedPlayer player;

	public PacketEncoder(final ProxiedPlayer player) {
		this.player = player;
	}

	@Override
	protected void encode(final ChannelHandlerContext ctx, final DefinedPacket packet, final List<Object> out) {
		final PacketEvent event = new PacketEvent(packet, this.player, this.player);
		ProxyServer.getInstance().getPluginManager().callEvent(event);
		if(event.isCancelled()) return;
		out.add(packet);
	}

}
