package it.zs0bye.bettersecurity.bungee.listeners;

import io.github.waterfallmc.waterfall.event.ProxyDefineCommandsEvent;
import it.zs0bye.bettersecurity.bungee.BetterSecurityBungee;
import it.zs0bye.bettersecurity.bungee.files.enums.Config;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class WaterTabCompleteListener implements Listener {

    private final ProxyServer server;

    public WaterTabCompleteListener(final BetterSecurityBungee plugin) {
        this.server = plugin.getProxy();
    }

     @EventHandler
    public void onProxyDefine(final ProxyDefineCommandsEvent event) {
         if(!this.server.getName().equals("Waterfall")) return;
         if(!Config.BLOCK_TAB_COMPLETE_ENABLED.getBoolean()) return;
         event.getCommands().clear();
     }

}
