package it.zs0bye.bettersecurity.listeners;

import it.zs0bye.bettersecurity.BetterSecurity;
import it.zs0bye.bettersecurity.Warnings;
import it.zs0bye.bettersecurity.executors.SendExecutors;
import it.zs0bye.bettersecurity.files.enums.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;
import java.util.Map;

public class CmdsOnlyPlayersListener implements Listener {

    private final BetterSecurity plugin;
    private final Map<String, String> placeholders;

    public CmdsOnlyPlayersListener(final BetterSecurity plugin) {
        this.plugin = plugin;
        this.placeholders = this.plugin.getCmdsPlaceholders();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCommandPreprocess(final PlayerCommandPreprocessEvent event) {

        if(!Config.COMMANDS_ONLY_PLAYERS_ENABLED.getBoolean()) return;

        final Player player = event.getPlayer();
        final String command = event.getMessage()
                .split(" ")[0]
                .toLowerCase()
                .replaceFirst("/", "");

        if(this.canBlock(player)) return;
        if(!Config.COMMANDS_ONLY_PLAYERS.getStringList().contains(command)) return;

        this.placeholders.put("%player%", player.getName());
        this.placeholders.put("%command%", "/" + command);

        SendExecutors.send(this.plugin, Config.COMMANDS_ONLY_PLAYERS_EXECUTORS.getStringList(), player, this.placeholders);
        event.setCancelled(true);

        if(!Config.COMMANDS_ONLY_PLAYERS_WARNING.getBoolean()) return;
        new Warnings(this.plugin, player, command).warn();
    }

    private boolean canBlock(final Player player) {
        final String method = Config.COMMANDS_ONLY_PLAYERS_METHOD.getString();
        final List<String> players = Config.COMMANDS_ONLY_PLAYERS_BYPASS.getStringList();
        final List<String> uuids = Config.COMMANDS_ONLY_PLAYERS_BYPASS_UUIDS.getStringList();
        if(method.equals("PLAYERS")) return players.contains(player.getName());
        if(method.equals("UUIDS")) return uuids.contains(player.getUniqueId().toString());
        if(method.equals("BOTH")) return players.contains(player.getName()) || uuids.contains(player.getUniqueId().toString());
        return true;
    }

}
