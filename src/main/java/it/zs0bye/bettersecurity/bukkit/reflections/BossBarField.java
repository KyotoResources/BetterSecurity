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

package it.zs0bye.bettersecurity.bukkit.reflections;

import it.zs0bye.bettersecurity.bukkit.BetterSecurityBukkit;
import it.zs0bye.bettersecurity.bukkit.utils.VersionUtils;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class BossBarField {

    private final BetterSecurityBukkit plugin;
    private final Player player;
    private final String msg;
    private final String color;
    private final String style;
    private final double progress;
    private final int seconds;

    private final static Map<Player, BukkitTask> task = new HashMap<>();

    public BossBarField(final BetterSecurityBukkit plugin, final Player player, final String msg, final String color, final String style, final double progress, final int seconds) {
        this.plugin = plugin;
        this.player = player;
        this.msg = msg;
        this.color = color;
        this.style = style;
        this.progress = progress;
        this.seconds = seconds;
        this.send();
    }

    private void send() {

        if (VersionUtils.checkVersion(1.8)) return;
        if (task.containsKey(this.player)) return;

        final BossBar boss = Bukkit.createBossBar(this.msg, BarColor.valueOf(this.color), BarStyle.valueOf(this.style));
        boss.addPlayer(this.player);
        boss.setProgress(this.progress);
        boss.setVisible(true);

        if (seconds < 1) return;

        task.put(this.player, new BukkitRunnable() {
            @Override
            public void run() {
                boss.removePlayer(player);

                this.cancel();
                task.remove(player);
            }
        }.runTaskLaterAsynchronously(this.plugin, this.seconds * 20L));
    }

}
