package it.zs0bye.bettersecurity.bungee.packet;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import it.zs0bye.bettersecurity.bungee.event.PacketEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.protocol.PacketWrapper;

import java.util.List;

public class PacketDecoder extends MessageToMessageDecoder<PacketWrapper> {

	private final ProxiedPlayer player;

	public PacketDecoder(final ProxiedPlayer player) {
		this.player = player;
	}

	@Override
	protected void decode(final ChannelHandlerContext chx, final PacketWrapper wrapper, final List<Object> out) {
		if(wrapper.packet == null) {
			out.add(wrapper);
			return;
		}

		final PacketEvent event = new PacketEvent(wrapper.packet, this.player, this.player);
		ProxyServer.getInstance().getPluginManager().callEvent(event);
		if(event.isCancelled()) return;
		out.add(wrapper);
	}

}
