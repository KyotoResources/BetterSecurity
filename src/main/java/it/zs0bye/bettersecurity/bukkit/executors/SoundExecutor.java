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
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class SoundExecutor extends Executors {

    private final BetterSecurityBukkit plugin;
    private final String execute;
    private final Player player;

    private Sound sound;

    public SoundExecutor(final BetterSecurityBukkit plugin, final String execute, final Player player) {
        this.plugin = plugin;
        this.execute = execute;
        this.player = player;
        if (!this.execute.startsWith(this.getType())) return;
        this.apply();
    }

    protected String getType() {
        return "[SOUND] ";
    }

    protected void apply() {

        final String sound = this.execute
                .replace(this.getType(), "")
                .split(";")[0];

         try {
             this.sound = Sound.valueOf(sound
                     .replaceFirst("@", ""));
         } catch (IllegalArgumentException e) {
             this.plugin.getLogger().log(Level.SEVERE, "The sound you entered in the configuration is not compatible with the server version, or it is not an existing sound! Reason: " + e.getMessage());
         }

        final int acute = Integer.parseInt(this.execute
                .replace(this.getType(), "")
                .split(";")[1]);
        final int volume = Integer.parseInt(this.execute
                .replace(this.getType(), "")
                .split(";")[2]);

        if(sound.startsWith("@")) {
            Bukkit.getOnlinePlayers().forEach(players -> this.run(players, volume, acute));
            return;
        }

        this.run(this.player, volume, acute);
    }

    private void run(final Player player, final int volume, final int acute) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(this.plugin, () ->
                this.player.playSound(player.getLocation(), this.sound, volume, acute), 1L);
    }
}