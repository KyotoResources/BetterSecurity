/*
 * Security plugin for your server - https://github.com/KyotoResources/BetterSecurity
 * Copyright (C) 2023 KyotoResources
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.zs0bye.bettersecurity.bungee.listeners;

import io.github.waterfallmc.waterfall.event.ProxyDefineCommandsEvent;
import it.zs0bye.bettersecurity.bungee.BetterSecurityBungee;
import it.zs0bye.bettersecurity.bungee.files.readers.Tab;
import it.zs0bye.bettersecurity.bungee.modules.Module;
import it.zs0bye.bettersecurity.bungee.modules.tabcomplete.TabComplete;
import lombok.Getter;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.Map;

public class WaterTabCompleteListener implements Listener {

     private final BetterSecurityBungee plugin;

     @Getter
     private static boolean registered;

     public WaterTabCompleteListener(final BetterSecurityBungee plugin) {
          this.plugin = plugin;
     }

     @EventHandler(priority = EventPriority.HIGHEST)
    public void onProxyDefine(final ProxyDefineCommandsEvent event) {
         if(hasDisabled()) return;
         final ProxiedPlayer player = (ProxiedPlayer) event.getReceiver();
         final Map<String, Command> commands = event.getCommands();
         new TabComplete(this.plugin, player).applyTabWaterfall(commands.keySet());
     }

     public static void register(final BetterSecurityBungee plugin) {
         try {
             Class.forName("io.github.waterfallmc.waterfall.event.ProxyDefineCommandsEvent");
             plugin.getProxy().getPluginManager().registerListener(plugin, new WaterTabCompleteListener(plugin));
             registered = true;
         } catch (final ClassNotFoundException e) {
             registered = false;
             if(hasDisabled()) return;
             if(Tab.IGNORE_WATERFALL_WARNING.getBoolean()) return;
             plugin.getLogger().warning("------------------------- TAB COMPLETE NOT SAFE -------------------------");
             plugin.getLogger().warning("You are using the regular version of bungeecord.");
             plugin.getLogger().warning("And this does not allow the complete tab to work 100%.");
             plugin.getLogger().warning("To fix this, use a waterfall (or its forks like flamecord, xcord, aegis, etc...).");
             plugin.getLogger().warning("Why is waterfall needed to work well?");
             plugin.getLogger().warning("Because thanks to waterfall you can disable the bungeecord suggestions.");
             plugin.getLogger().warning("YOUR USERS WILL NOW BE SEEING ALL THE PLUGINS YOU ARE USING IN YOUR PROXY.");
             plugin.getLogger().warning("--- To ignore this warning, set to true -> \"ignore_waterfall_warning\" ---");
         }
     }

     private static boolean hasDisabled() {
         if(Module.TAB_COMPLETE.isDisabled()) return true;
         return Tab.IGNORE_1_13_TAB_COMPLETE.getBoolean();
     }

}
