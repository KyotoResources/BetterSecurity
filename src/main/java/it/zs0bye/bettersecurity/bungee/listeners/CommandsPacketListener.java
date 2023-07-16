package it.zs0bye.bettersecurity.bungee.listeners;

import io.netty.channel.Channel;
import it.zs0bye.bettersecurity.bungee.BetterSecurityBungee;
import it.zs0bye.bettersecurity.bungee.packet.NewPacketListener;
import it.zs0bye.bettersecurity.bungee.packet.PacketListener;
import lombok.SneakyThrows;
import net.md_5.bungee.ServerConnection;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.netty.ChannelWrapper;
import net.md_5.bungee.netty.PipelineUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class CommandsPacketListener implements Listener {

    private final BetterSecurityBungee plugin;

    public CommandsPacketListener(final BetterSecurityBungee plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(final ServerSwitchEvent event) {
        final ProxiedPlayer player = event.getPlayer();
        if(player == null || !player.isConnected()) return;
        if(player.getPendingConnection().getVersion() < 393) return;
        final ServerConnection connection = (ServerConnection) player.getServer();
        final ChannelWrapper wrapper = connection.getCh();
        wrapper.getHandle().pipeline().addAfter(PipelineUtils.PACKET_DECODER, "bs-commands-packet", new NewPacketListener(this.plugin, player));
    }

//    @SneakyThrows
//    @EventHandler
//    public void onPostLogin(final PostLoginEvent event) {
//        UserConnection player = (UserConnection) event.getPlayer();
//        Field field = player.getClass().getDeclaredField("ch");
//        field.setAccessible(true);
//        Object ch = field.get(player);
//        Method method = ch.getClass().getDeclaredMethod("getHandle");
//        Channel channel = (Channel) method.invoke(ch);
//        channel.pipeline().addAfter(PipelineUtils.PACKET_DECODER, "bs-commands-packet", new PacketListener(this.plugin, player));
//    }

}
