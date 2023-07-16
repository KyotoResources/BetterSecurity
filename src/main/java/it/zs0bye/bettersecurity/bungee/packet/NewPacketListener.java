package it.zs0bye.bettersecurity.bungee.packet;

import com.mojang.brigadier.tree.CommandNode;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.MessageToMessageDecoder;
import it.zs0bye.bettersecurity.bungee.BetterSecurityBungee;
import it.zs0bye.bettersecurity.bungee.tabcomplete.TabComplete;
import net.md_5.bungee.ServerConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.Commands;

import java.util.Collection;
import java.util.List;

public class NewPacketListener extends ChannelInboundHandlerAdapter {

    final BetterSecurityBungee plugin;
    final ProxiedPlayer player;
//    final ServerConnection connection;

    public NewPacketListener(final BetterSecurityBungee plugin, final ProxiedPlayer player) {
        this.plugin = plugin;
        this.player = player;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object obj) {

        final PacketWrapper wrapper = (PacketWrapper) obj;
        boolean release = true;

        try {
            if (!(wrapper.packet instanceof Commands)) return;
            final Commands packet = (Commands) wrapper.packet;
            System.out.println("work tab complete");
            final Collection<CommandNode<?>> childrens = packet.getRoot().getChildren();
            childrens.removeIf(node -> node.getName().contains(":"));
            new TabComplete(this.plugin, player).applyTabChildren(childrens);
//            player.unsafe().sendPacket(packet);
            super.channelRead(ctx, obj);
        } catch (final Throwable e) {
            e.printStackTrace();
        }

    }

}
