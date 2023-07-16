package it.zs0bye.bettersecurity.bungee.listeners;

import it.zs0bye.bettersecurity.bungee.BetterSecurityBungee;
import it.zs0bye.bettersecurity.bungee.tabcomplete.TabComplete;
import it.zs0bye.bettersecurity.bungee.files.enums.Config;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

public class ManageTabCompleteListener implements Listener {

    private final BetterSecurityBungee plugin;

    public ManageTabCompleteListener(final BetterSecurityBungee plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onTabComplete(final TabCompleteEvent event) {
        if(!Config.MANAGE_TAB_COMPLETE_ENABLED.getBoolean()) return;

        final ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        if(player.hasPermission("bettersecurity.bypass.tab")) return;

        final String completion = event.getCursor();
        final List<String> commands = event.getSuggestions();
        new TabComplete(this.plugin, player).applyTabLegacy(commands, completion, event);
    }

}
