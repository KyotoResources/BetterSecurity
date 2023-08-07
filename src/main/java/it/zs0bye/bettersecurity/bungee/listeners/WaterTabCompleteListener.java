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
