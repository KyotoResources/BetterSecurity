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

package it.zs0bye.bettersecurity.bungee.commands;

import it.zs0bye.bettersecurity.bungee.BetterSecurityBungee;
import it.zs0bye.bettersecurity.bungee.commands.subcmds.AboutSubCMD;
import it.zs0bye.bettersecurity.bungee.commands.subcmds.HelpSubCMD;
import it.zs0bye.bettersecurity.bungee.commands.subcmds.ReloadSubCMD;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainCommand extends Command implements TabExecutor {

    private final BetterSecurityBungee plugin;

    public MainCommand(final BetterSecurityBungee plugin) {
        super("bettersecuritybungee", "", "bsecuritybungee", "securitybungee", "bsbungee", "bsb");
        this.plugin = plugin;
    }

    private boolean checkArgs(final String args, final String check) {
        return !args.equalsIgnoreCase(check);
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {

        if(!sender.hasPermission(this.getName() + ".command")) {
            new AboutSubCMD(this.getName(), sender, this.plugin);
            return;
        }

        if(args.length == 0) {
            new HelpSubCMD(this.getName(), sender, this.plugin);
            return;
        }

        if(args.length == 1) {
            new ReloadSubCMD(this.getName(), args, sender, this.plugin);
            new AboutSubCMD(this.getName(), args, sender, this.plugin);

            if(checkArgs(args[0], "reload")
                    && checkArgs(args[0], "about"))
                new HelpSubCMD(this.getName(), sender,this.plugin);

            return;
        }

        new HelpSubCMD(this.getName(), sender, this.plugin);
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {

        final Set<String> completions = new HashSet<>();

        if(!sender.hasPermission(this.getName() + ".command")) return new ArrayList<>();

        if (args.length == 1) {
            new HelpSubCMD(this.getName(), completions, sender);
            new ReloadSubCMD(this.getName(), completions, sender);
            new AboutSubCMD(this.getName(), completions, sender);
        }

        return completions;
    }
}
