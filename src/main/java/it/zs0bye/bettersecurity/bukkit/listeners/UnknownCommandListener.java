package it.zs0bye.bettersecurity.bukkit.listeners;

import it.zs0bye.bettersecurity.bukkit.BetterSecurityBukkit;
import it.zs0bye.bettersecurity.bukkit.executors.SendExecutors;
import it.zs0bye.bettersecurity.bukkit.files.enums.Config;
import it.zs0bye.bettersecurity.bukkit.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Map;

public class UnknownCommandListener implements Listener {

    private final BetterSecurityBukkit plugin;
    private final Map<String, String> placeholders;

    public UnknownCommandListener(final BetterSecurityBukkit plugin) {
        this.plugin = plugin;
        this.placeholders = this.plugin.getCmdsPlaceholders();
    }

    @EventHandler
    public void onCommandPreProcess(final PlayerCommandPreprocessEvent event) {

        if(!Config.UNKNOWN_COMMAND_ENABLED.getBoolean()) return;

        final Player player = event.getPlayer();
        final String command = event.getMessage()
                .split(" ")[0]
                .toLowerCase();

        if (Bukkit.getHelpMap().getHelpTopic(command) != null) return;
        event.setCancelled(true);

        this.placeholders.put("%player%", player.getName());
        this.placeholders.put("%command%", command);

        if(Config.UNKNOWN_COMMAND_USE_SPIGOT_MESSAGE.getBoolean()) {
            player.sendMessage(StringUtils.colorize(this.plugin.getSpigotFile().getConfig().getString("messages.unknown-command")));
            return;
        }

        SendExecutors.send(this.plugin, Config.UNKNOWN_COMMAND_EXECUTORS.getStringList(), player, this.placeholders);
    }


}
