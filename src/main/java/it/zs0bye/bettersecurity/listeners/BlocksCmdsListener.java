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

public class BlocksCmdsListener implements Listener {

    private final BetterSecurity plugin;
    private final Map<String, String> placeholders;

    public BlocksCmdsListener(final BetterSecurity plugin) {
        this.plugin = plugin;
        this.placeholders = this.plugin.getCmdsPlaceholders();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCommandPreprocess(final PlayerCommandPreprocessEvent event) {

        if(!Config.BLOCKS_COMMANDS_ENABLED.getBoolean()) return;

        final Player player = event.getPlayer();
        final String command = event.getMessage()
                .split(" ")[0]
                .toLowerCase()
                .replaceFirst("/", "");

        if(player.hasPermission("bettersecurity.bypass.blockscmds")) return;
        if(this.canBlock(command)) return;

        this.placeholders.put("%player%", player.getName());
        this.placeholders.put("%command%", "/" + command);

        SendExecutors.send(this.plugin, Config.BLOCKS_COMMANDS_EXECUTORS.getStringList(), player, this.placeholders);
        event.setCancelled(true);

        if(!Config.BLOCKS_COMMANDS_WARNING.getBoolean()) return;
        new Warnings(this.plugin, player, command).warn();
    }

    private boolean canBlock(final String command) {
        final String method = Config.BLOCKS_COMMANDS_METHOD.getString();
        final List<String> commands = Config.BLOCKS_COMMANDS.getStringList();
        if(method.equals("BLACKLIST")) return !commands.contains(command);
        if(method.equals("WHITELIST")) return commands.contains(command);
        return true;
    }

}
