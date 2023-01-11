package it.zs0bye.bettersecurity.bungee.listeners;

import it.zs0bye.bettersecurity.bungee.TabComplete;
import it.zs0bye.bettersecurity.bungee.files.enums.Config;
import it.zs0bye.bettersecurity.common.utils.CStringUtils;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

public class BlockTabCompleteListener implements Listener {

    @EventHandler
    public void onTabComplete(final TabCompleteEvent event) {

        if(!Config.BLOCK_TAB_COMPLETE_ENABLED.getBoolean()) return;

        final ProxiedPlayer player = (ProxiedPlayer) event.getSender();

        final String completion = event.getCursor();
        final List<String> commands = event.getSuggestions();
        final List<String> blockCmds = Config.BLOCK_TAB_COMPLETE_COMMANDS.getStringList();

        final List<String> completions = TabComplete.getCompletions(player, true);
        final List<String> matches = CStringUtils.copyPartialMatches(completion, completions);

        if(new TabComplete(player).bypass()) return;

        commands.clear();

        blockCmds.forEach(blockCmd -> {
            if(!completion.startsWith("/" + blockCmd)) return;
            event.setCancelled(true);
        });

        if(completion.contains(" ")) return;
        if(completions.isEmpty()) {
            event.setCancelled(true);
            return;
        }

        if(Config.BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_PARTIAL_MATCHES.getBoolean()) {
            commands.addAll(matches);
            return;
        }
        commands.addAll(completions);
    }

}
