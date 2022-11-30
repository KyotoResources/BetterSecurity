package it.zs0bye.bettersecurity.bukkit.listeners;

import it.zs0bye.bettersecurity.bukkit.BetterSecurityBukkit;
import it.zs0bye.bettersecurity.bukkit.utils.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UpdaterListener implements Listener {

    private final BetterSecurityBukkit plugin;

    public UpdaterListener(final BetterSecurityBukkit plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        if(!player.isOp()) return;
        if(this.plugin.getUpdateMsg() == null) return;
        StringUtils.send(player, this.plugin.getUpdateMsg());
    }

}
