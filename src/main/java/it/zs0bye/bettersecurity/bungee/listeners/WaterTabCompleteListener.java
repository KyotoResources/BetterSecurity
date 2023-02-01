package it.zs0bye.bettersecurity.bungee.listeners;

import io.github.waterfallmc.waterfall.event.ProxyDefineCommandsEvent;
import it.zs0bye.bettersecurity.bungee.TabComplete;
import it.zs0bye.bettersecurity.bungee.files.enums.Config;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class WaterTabCompleteListener implements Listener {

     @EventHandler
    public void onProxyDefine(final ProxyDefineCommandsEvent event) {
         if(!Config.BLOCK_TAB_COMPLETE_ENABLED.getBoolean()) return;

         final ProxiedPlayer player = (ProxiedPlayer) event.getReceiver();
         final Map<String, Command> commands = event.getCommands();
         final List<String> completions = new TabComplete(player).getCompletions(false);

         if(new TabComplete(player).bypass()) return;

         final Iterator<String> iterator = commands.keySet().iterator();

         while(iterator.hasNext()) {
             final String command = iterator.next();
             if (completions.contains(command)) continue;
             iterator.remove();
         }
     }

}
