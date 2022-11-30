package it.zs0bye.bettersecurity.bukkit.listeners;

import it.zs0bye.bettersecurity.bukkit.TabComplete;
import it.zs0bye.bettersecurity.bukkit.files.enums.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;

import java.util.ArrayList;
import java.util.List;

public class BlockTabCompleteListener implements Listener {

    @EventHandler
    public void onPlayerCommandSend(final PlayerCommandSendEvent event) {

        if(!Config.BLOCK_TAB_COMPLETE_ENABLED.getBoolean()) return;

        final Player player = event.getPlayer();
        final List<String> commands = new ArrayList<>(event.getCommands());
        final List<String> completions = TabComplete.getCompletions(player, false);

        final TabComplete tabComplete = new TabComplete(player);
        if(tabComplete.bypass()) return;

        commands.forEach(command -> {
            if (completions.contains(command)) return;
            event.getCommands().remove(command);
        });
    }

}
