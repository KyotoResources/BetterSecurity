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

package it.zs0bye.bettersecurity.bungee.executors;

import it.zs0bye.bettersecurity.bungee.BetterSecurityBungee;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.Map;

public class SendExecutors {

    @SafeVarargs
    public static void send(final BetterSecurityBungee plugin, final List<String> executors, final ProxiedPlayer player, final Map<String, String>... placeholders) {
        if(executors.isEmpty()) return;
        executors.forEach(executor -> {

            for (Map<String, String> placeholder : placeholders) {
                for (String key : placeholder.keySet())
                    executor = executor.replace(key, placeholder.get(key));
            }

            new KickExecutor(executor, player);
            new ActionExecutor(plugin, executor, player);
            new ConsoleExecutor(plugin, executor, player);
            new MessageExecutor(plugin, executor, player);
            new PlayerExecutor(plugin, executor, player);
            new TitleExecutor(plugin, executor, player);
            new MiniMessageExecutor(plugin, executor, player);
        });
    }

}
