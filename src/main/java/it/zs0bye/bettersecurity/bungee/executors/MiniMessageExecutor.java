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
import it.zs0bye.bettersecurity.bungee.files.readers.Config;
import it.zs0bye.bettersecurity.common.utils.CStringUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class MiniMessageExecutor extends Executors {

    private final BetterSecurityBungee plugin;
    private final String execute;
    private final ProxiedPlayer player;

    public MiniMessageExecutor(final BetterSecurityBungee plugin, final String execute, final ProxiedPlayer player) {
        this.plugin = plugin;
        this.execute = execute;
        this.player = player;
        if (!this.execute.startsWith(this.getType())) return;
        this.apply();
    }

    protected String getType() {
        return "[MINI_MESSAGE] ";
    }

    protected void apply() {

        String msg = this.execute.replace(this.getType(), "").replace("%prefix%", Config.SETTINGS_PREFIX.getString());
        if(msg.contains("%center%")) msg = CStringUtils.center(msg.replace("%center%", ""));

        if(msg.startsWith("@")) {
            final String finalMsg = msg;
            this.plugin.getProxy().getPlayers().forEach(players -> this.run(players, finalMsg));
            return;
        }

        this.run(this.player, msg);
    }

    private void run(final ProxiedPlayer player, final String msg) {
        final Component parsed = MiniMessage.miniMessage().deserialize(msg.replaceFirst("@", ""));
        this.plugin.getAdventure().player(player).sendMessage(parsed);
    }

}