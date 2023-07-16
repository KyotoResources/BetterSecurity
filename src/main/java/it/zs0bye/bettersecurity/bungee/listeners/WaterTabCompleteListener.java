package it.zs0bye.bettersecurity.bungee.listeners;

import io.github.waterfallmc.waterfall.event.ProxyDefineCommandsEvent;
import it.zs0bye.bettersecurity.bungee.BetterSecurityBungee;
import it.zs0bye.bettersecurity.bungee.tabcomplete.TabComplete;
import it.zs0bye.bettersecurity.bungee.files.enums.Config;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Map;

public class WaterTabCompleteListener implements Listener {

     private final BetterSecurityBungee plugin;

     public WaterTabCompleteListener(final BetterSecurityBungee plugin) {
          this.plugin = plugin;
     }

     @EventHandler
    public void onProxyDefine(final ProxyDefineCommandsEvent event) {
         if(!Config.MANAGE_TAB_COMPLETE_ENABLED.getBoolean()) return;

         final ProxiedPlayer player = (ProxiedPlayer) event.getReceiver();
         if(player.hasPermission("bettersecurity.bypass.tab")) return;

         final Map<String, Command> commands = event.getCommands();
         new TabComplete(this.plugin, player).applyTabWaterfall(commands.keySet().iterator());
     }

}
