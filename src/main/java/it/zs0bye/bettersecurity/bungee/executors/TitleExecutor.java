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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.time.Duration;

public class TitleExecutor extends Executors {

    private final BetterSecurityBungee plugin;
    private final ProxiedPlayer player;

    private String execute;

    public TitleExecutor(final BetterSecurityBungee plugin, final String execute, final ProxiedPlayer player) {
        this.plugin = plugin;
        this.player = player;
        if(!execute.startsWith(this.getType())) return;
        this.execute = execute.replace(this.getType(), "");
        this.apply();
    }

    protected String getType() {
        return "[TITLE] ";
    }

    protected void apply() {

        String title = this.execute.split(";")[0];
        String  subtitle = this.execute.split(";")[1];
        final Duration fadein = Duration.ofSeconds(Integer.parseInt(this.execute.split(";")[2]));
        final Duration stay = Duration.ofSeconds(Integer.parseInt(this.execute.split(";")[3]));
        final Duration fadeout = Duration.ofSeconds(Integer.parseInt(this.execute.split(";")[4]));

        if(subtitle.equalsIgnoreCase("none")) subtitle = "";

        if(title.replaceFirst("@", "")
                .equalsIgnoreCase("none")) title = "";

        final Title stitle = Title.title(Component.text(title), Component.text(subtitle), Title.Times.times(fadein, stay, fadeout));

        if(title.startsWith("@")) {
            this.plugin.getAdventure().players().showTitle(stitle);
            return;
        }

        this.plugin.getAdventure().player(player).showTitle(stitle);
    }

}
