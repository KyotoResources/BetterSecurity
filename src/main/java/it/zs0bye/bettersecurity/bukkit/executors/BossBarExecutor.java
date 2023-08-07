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
import it.zs0bye.bettersecurity.bukkit.reflections.BossBarField;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BossBarExecutor extends Executors {

    private final BetterSecurityBukkit plugin;
    private final String execute;
    private final Player player;

    private final HooksManager hooks;

    public BossBarExecutor(final BetterSecurityBukkit plugin, final String execute, final Player player) {
        this.plugin = plugin;
        this.execute = execute;
        this.player = player;
        this.hooks = this.plugin.getHooks();
        if(!this.execute.startsWith(this.getType())) return;
        this.apply();
    }

    protected String getType() {
        return "[BOSSBAR] ";
    }

    protected void apply() {

        final String title = this.hooks.replacePlaceholders(this.player, execute
                .replace(this.getType(), "")
                .split(";")[0]);

        final String color = execute
                .replace(this.getType(), "")
                .split(";")[1]
                .toUpperCase();

        final String style = execute
                .replace(this.getType(), "")
                .split(";")[2]
                .toUpperCase();

        final double progress = Double.parseDouble(execute
                .replace(this.getType(), "")
                .split(";")[3]);

        final int times = Integer.parseInt(execute
                .replace(this.getType(), "")
                .split(";")[4]);

        if(title.startsWith("@")) {
            Bukkit.getOnlinePlayers().forEach(players -> this.run(players, title, color, style, progress, times));
            return;
        }

        this.run(this.player, title, color, style, progress, times);
    }

    private void run(final Player player, final String title, final String color, final String style, final double progress, final int times) {
        new BossBarField(this.plugin, player, title
                .replaceFirst("@", ""), color, style, progress, times);
    }
}
