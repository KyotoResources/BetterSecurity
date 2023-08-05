package it.zs0bye.bettersecurity.bungee.listeners;

import it.zs0bye.bettersecurity.bungee.BetterSecurityBungee;
import it.zs0bye.bettersecurity.bungee.utils.StringUtils;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class UpdaterListener implements Listener {

    private final BetterSecurityBungee plugin;

    public UpdaterListener(final BetterSecurityBungee plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(final ServerConnectEvent event) {
        final ProxiedPlayer player = event.getPlayer();
        if(!player.hasPermission("bettersecurity.updatenotify")) return;
        if(this.plugin.getUpdateMsg() == null) return;
        StringUtils.send(player, this.plugin.getUpdateMsg());
    }

}
