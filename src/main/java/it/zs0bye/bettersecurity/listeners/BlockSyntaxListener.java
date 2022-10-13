package it.zs0bye.bettersecurity.listeners;

import it.zs0bye.bettersecurity.BetterSecurity;
import it.zs0bye.bettersecurity.warnings.Warnings;
import it.zs0bye.bettersecurity.executors.SendExecutors;
import it.zs0bye.bettersecurity.files.enums.Config;
import it.zs0bye.bettersecurity.warnings.enums.TypeWarning;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Map;

public class BlockSyntaxListener implements Listener {

    private final BetterSecurity plugin;
    private final Map<String, String> placeholders;

    public BlockSyntaxListener(final BetterSecurity plugin) {
        this.plugin = plugin;
        this.placeholders = this.plugin.getCmdsPlaceholders();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCommandPreprocess(final PlayerCommandPreprocessEvent event) {

        if(!Config.BLOCK_SYNTAX_ENABLED.getBoolean()) return;

        final Player player = event.getPlayer();
        final String command = event.getMessage()
                .split(" ")[0]
                .toLowerCase();

        if(!command.contains(":")) return;
        if(player.hasPermission("bettersecurity.bypass.blocksyntax")) return;

        this.placeholders.put("%player%", player.getName());
        this.placeholders.put("%command%", command);

        SendExecutors.send(this.plugin, Config.BLOCK_SYNTAX_EXECUTORS.getStringList(), player, this.placeholders);
        event.setCancelled(true);

        if(!Config.BLOCK_SYNTAX_WARNING.getBoolean()) return;
        new Warnings(this.plugin, player, TypeWarning.COMMANDS, command.replaceFirst("/", ""));
    }

}
