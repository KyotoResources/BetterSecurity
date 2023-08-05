package it.zs0bye.bettersecurity.bungee.event;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;
import net.md_5.bungee.protocol.DefinedPacket;

@Getter
public class PacketEvent extends Event implements Cancellable {

	@Setter
	private boolean cancelled;

	private final DefinedPacket packet;
	private final Connection sender;
	private final ProxiedPlayer player;
	
	public PacketEvent(final DefinedPacket packet, final ProxiedPlayer player, final Connection sender) {
		this.cancelled = false;
		this.packet = packet;
		this.player = player;
		this.sender = sender;
	}

}
