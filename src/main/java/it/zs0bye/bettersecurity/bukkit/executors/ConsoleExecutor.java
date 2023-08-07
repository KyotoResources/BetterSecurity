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

package it.zs0bye.bettersecurity.bukkit.executors;

import it.zs0bye.bettersecurity.bukkit.BetterSecurityBukkit;
import it.zs0bye.bettersecurity.bukkit.hooks.HooksManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ConsoleExecutor extends Executors {

    private final BetterSecurityBukkit plugin;
    private final String execute;
    private final Player player;

    private final HooksManager hooks;

    public ConsoleExecutor(final BetterSecurityBukkit plugin, final String execute, final Player player) {
        this.plugin = plugin;
        this.execute = execute;
        this.player = player;
        this.hooks = this.plugin.getHooks();
        if (!this.execute.startsWith(this.getType())) return;
        this.apply();
    }

    protected String getType() {
        return "[CONSOLE] ";
    }

    protected void apply() {

        final String command = this.hooks.replacePlaceholders(this.player, this.execute
                .replace(this.getType(), "")
                .replace("%player%", this.player.getName()));

        Bukkit.getScheduler().runTaskLater(this.plugin, () ->
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command), 2L);

    }
}