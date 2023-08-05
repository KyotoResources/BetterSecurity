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
