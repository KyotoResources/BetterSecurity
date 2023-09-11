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
import it.zs0bye.bettersecurity.bungee.BungeeUser;
import it.zs0bye.bettersecurity.bungee.commands.subcmds.AboutSubCMD;
import it.zs0bye.bettersecurity.bungee.commands.subcmds.HelpSubCMD;
import it.zs0bye.bettersecurity.bungee.commands.subcmds.ReloadSubCMD;
import it.zs0bye.bettersecurity.common.BetterUser;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MainCommand extends Command implements TabExecutor {

    private final BetterSecurityBungee plugin;

    public MainCommand(final BetterSecurityBungee plugin) {
        super("bettersecuritybungee", "", "bsecuritybungee", "securitybungee", "bsbungee", "bsb");
        this.plugin = plugin;
    }

    private boolean checkArgs(final String[] args, final String... commands) {
        return Arrays.stream(commands).anyMatch(args[0]::equalsIgnoreCase);
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {

        final BetterUser user = new BungeeUser(sender);
        if(!sender.hasPermission(this.getName() + ".command")) {
            new AboutSubCMD(this.getName(), user, this.plugin);
            return;
        }

        if(args.length == 0) {
            new HelpSubCMD(this.getName(), user, this.plugin);
            return;
        }

        if(args.length == 1) {
            new ReloadSubCMD(this.getName(), args, user, this.plugin);
            new AboutSubCMD(this.getName(), args, user, this.plugin);

            if(checkArgs(args, "reload", "about")) return;
            new HelpSubCMD(this.getName(), user, this.plugin);
            return;
        }

        new HelpSubCMD(this.getName(), user, this.plugin);
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
