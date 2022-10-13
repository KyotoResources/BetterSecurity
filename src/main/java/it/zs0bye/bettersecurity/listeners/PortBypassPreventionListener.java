package it.zs0bye.bettersecurity.listeners;

import it.zs0bye.bettersecurity.BetterSecurity;
import it.zs0bye.bettersecurity.files.enums.Config;
import it.zs0bye.bettersecurity.warnings.Warnings;
import it.zs0bye.bettersecurity.warnings.enums.TypeWarning;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.*;

public class PortBypassPreventionListener implements Listener {

    private final BetterSecurity plugin;

    public PortBypassPreventionListener(final BetterSecurity plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(final PlayerLoginEvent event) {
        if(!Config.PORT_BYPASS_PREVENTION_ENABLED.getBoolean()) return;

        final String address = event.getRealAddress().getHostAddress();
        final String message = Config.PORT_BYPASS_PREVENTION_KICK_MESSAGE.getString()
                .replace("\\n", "\n");

        final List<String> whitelisted = Config.PORT_BYPASS_PREVENTION_WHITELISTED_IPS.getStringList();

        if(Config.PORT_BYPASS_PREVENTION_SAFETY_KICKS.getBoolean() && (whitelisted.contains("0.0.0.0") || whitelisted.contains("127.0.0.1"))) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "§4§lSECURITY ISSUE!\n\n§7You have whitelisted ip: §c0.0.0.0 §7or §c127.0.0.1§7.\n§7Insert only the ip of your machine!");
            return;
        }

        if(whitelisted.contains(address)) return;
        new Warnings(this.plugin, event.getPlayer(), TypeWarning.PORT_BYPASS_PREVENTION, Bukkit.getServer().getPort() + "", address);
        event.disallow(PlayerLoginEvent.Result.KICK_OTHER, message);
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerPreLogin(final AsyncPlayerPreLoginEvent event) {
        if(!Config.PORT_BYPASS_PREVENTION_ENABLED.getBoolean()) return;
        if(!Config.PORT_BYPASS_PREVENTION_ALREADY_CONNECTED_ENABLED.getBoolean()) return;

        final String message = Config.PORT_BYPASS_PREVENTION_ALREADY_CONNECTED_MSG.getString()
                .replace("\\n", "\n");

        for(final Player player : Bukkit.getOnlinePlayers()) {
            if (!player.getName().equalsIgnoreCase(event.getName()) && !player.getUniqueId().equals(event.getUniqueId())) continue;
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, message);
        }
    }

}
