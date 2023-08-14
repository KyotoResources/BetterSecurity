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

package it.zs0bye.bettersecurity.bungee.commands.subcmds;

import it.zs0bye.bettersecurity.bungee.BetterSecurityBungee;
import it.zs0bye.bettersecurity.bungee.commands.BaseCommand;
import it.zs0bye.bettersecurity.bungee.files.readers.Lang;
import net.md_5.bungee.api.CommandSender;

import java.util.HashMap;
import java.util.Set;

public class HelpSubCMD extends BaseCommand {

    private final String command;
    private CommandSender sender;
    private BetterSecurityBungee plugin;

    public HelpSubCMD(final String command, final CommandSender sender, final BetterSecurityBungee plugin) {
        this.command = command;
        this.sender = sender;
        this.plugin = plugin;
        this.execute();
    }

    public HelpSubCMD(final String command, final Set<String> tab, final CommandSender sender) {
        this.command = command;
        if(!sender.hasPermission(this.command + ".command." + this.getName())) return;
        tab.add(this.getName());
    }

    @Override
    protected String getName() {
        return "help";
    }

    @Override
    protected void execute() {

        if (!this.sender.hasPermission(this.command + ".command." + this.getName())) {
            Lang.INSUFFICIENT_PERMISSIONS.send(this.sender);
            return;
        }

        Lang.HELP_TEXTS.sendList(this.sender, new HashMap<>() {{
            put("%command%", command);
            put("%plugin%", plugin.getDescription().getName());
            put("%version%", "v" + plugin.getDescription().getVersion());
            put("%proxy%", plugin.getProxy().getName());
            put("%author%", author());
        }});

    }

    private String author() {
        return "Developed by zS0bye";
    }
}
