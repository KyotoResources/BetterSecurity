package it.zs0bye.bettersecurity.bukkit.listeners;

import it.zs0bye.bettersecurity.bukkit.BetterSecurityBukkit;
import it.zs0bye.bettersecurity.bukkit.warnings.Warnings;
import it.zs0bye.bettersecurity.bukkit.executors.SendExecutors;
import it.zs0bye.bettersecurity.bukkit.files.enums.Config;
import it.zs0bye.bettersecurity.bukkit.warnings.enums.TypeWarning;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Map;

public class CmdsOnlyConsoleListener implements Listener {

    private final BetterSecurityBukkit plugin;
    private final Map<String, String> placeholders;

    public CmdsOnlyConsoleListener(final BetterSecurityBukkit plugin) {
        this.plugin = plugin;
        this.placeholders = this.plugin.getCmdsPlaceholders();
        if(!Config.COMMANDS_ONLY_CONSOLE_ENABLED.getBoolean()) return;
        this.plugin.getServer().getPluginManager().registerEvent(
                PlayerCommandPreprocessEvent.class,
                this,
                EventPriority.valueOf(Config.COMMANDS_ONLY_CONSOLE_PRIORITY.getString().toUpperCase()),
                ((listener, event) -> this.onCommandPreprocess((PlayerCommandPreprocessEvent) event)),
                this.plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCommandPreprocess(final PlayerCommandPreprocessEvent event) {

        if(!Config.COMMANDS_ONLY_CONSOLE_ENABLED.getBoolean()) return;

        final Player player = event.getPlayer();
        final String command = event.getMessage()
                .split(" ")[0]
                .toLowerCase()
                .replaceFirst("/", "");

        if(!Config.COMMANDS_ONLY_CONSOLE.getStringList().contains(command)) return;

        this.placeholders.put("%player%", player.getName());
        this.placeholders.put("%command%", "/" + command);

        SendExecutors.send(this.plugin, Config.COMMANDS_ONLY_CONSOLE_EXECUTORS.getStringList(), player, this.placeholders);
        event.setCancelled(true);

        if(!Config.COMMANDS_ONLY_CONSOLE_WARNING.getBoolean()) return;
        new Warnings(this.plugin, player, TypeWarning.COMMANDS, command);
    }

}
