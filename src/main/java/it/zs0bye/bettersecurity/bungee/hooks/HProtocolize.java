package it.zs0bye.bettersecurity.bungee.hooks;

import com.mojang.brigadier.tree.CommandNode;
import dev.simplix.protocolize.api.Direction;
import dev.simplix.protocolize.api.Protocolize;
import dev.simplix.protocolize.api.listener.AbstractPacketListener;
import dev.simplix.protocolize.api.listener.PacketReceiveEvent;
import dev.simplix.protocolize.api.listener.PacketSendEvent;
import it.zs0bye.bettersecurity.bungee.BetterSecurityBungee;
import it.zs0bye.bettersecurity.bungee.tabcomplete.TabComplete;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.protocol.packet.Commands;

import java.util.*;

public class HProtocolize extends AbstractPacketListener<Commands> {

    private final BetterSecurityBungee plugin;

    protected HProtocolize(final BetterSecurityBungee plugin) {
        super(Commands.class, Direction.UPSTREAM, 0);
        this.plugin = plugin;
        Protocolize.listenerProvider().registerListener(this);
    }

    @Override
    public void packetReceive(final PacketReceiveEvent<Commands> event) {
        //
    }

    @SuppressWarnings("unchecked")
    @Override
    public void packetSend(final PacketSendEvent<Commands> event) {
        final ProxiedPlayer player = this.plugin.getProxy().getPlayer(event.player().uniqueId());
        if(player == null || !player.isConnected()) return;
        if(player.hasPermission("bettersecurity.bypass.tab")) return;
        if(player.getPendingConnection().getVersion() < 393) return;
        final Commands packet = event.packet();

        final Collection<CommandNode<?>> childrens = packet.getRoot().getChildren();
        childrens.removeIf(node -> node.getName().contains(":"));
        new TabComplete(this.plugin, player).applyTabChildren(childrens);
    }

}
